package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.commom.FlowDTO;
import com.pi.client.pi_client.communication.KafkaService;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.model.ResponseDTO;
import com.pi.client.pi_client.utlis.ShellUtils;
import com.pi.client.pi_client.utlis.date.DateUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流量业务
 */
@Slf4j
public class FlowService {
  KafkaService kafkaService;
  MqttService mqttService;
  ApplicationContext applicationContext;
  Config config;

  public FlowService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.mqttService = applicationContext.getMqttService();
    this.kafkaService = applicationContext.getKafkaService();
    this.config = applicationContext.getConfig();
    new Thread(this::task).start();
  }

  /**
   * 开始统计流量
   */
  Thread checkShell() {
    log.info("[脚本执行] 开始统计流量");
    return new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellFlowCheck()));
  }

  /**
   * 清除统计流量
   */
  Thread clearShell() {
    log.info("[脚本执行] 清除统计流量");
    return new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellFlowClear()));
  }

  /**
   * 增加端口流量统计
   */
  void addPort() {
    log.info("[脚本执行] 增加端口流量统计");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellFlowAddPort(), "12028", "12040")).start();
  }

  void task() {
    addPort();
    new Timer(System.currentTimeMillis() + "").schedule(new TimerTask() {
      @Override
      public void run() {
        log.info("定时[" + this.getClass().getName() + "]任务开始执行...");
        Thread checkThread = checkShell();
        checkThread.start();
        try {
          checkThread.join();
        } catch (InterruptedException e) {
          log.warn("统计流量线程异常", e);
          checkThread.interrupt();
        }
        Collection<FlowDTO> flowDTOS = handleFlowText();
        if (flowDTOS.isEmpty()) {
          log.warn("没有需要上报的数据");
          return;
        }
        Boolean fag = mqttService.publish(ResponseDTO.builder()
          .type(ResponseDTO.Type.OK)
          .serviceType(ResponseDTO.ServiceType.FLOW)
          .msg("流量上报")
          .t(flowDTOS)
          .build());
        if (!fag) {
          log.warn("消息发送失败，不清空消息列表");
          return;
        }
        Thread clearThread = clearShell();
        clearThread.start();
        try {
          clearThread.join();
        } catch (InterruptedException e) {
          log.warn("清除流量线程异常", e);
          clearThread.interrupt();
        }
      }
    }, new Date(), 1 * 60 * 1000);
  }

  /**
   * 处理流量文件
   *
   * @return 返回处理结果
   */
  Collection<FlowDTO> handleFlowText() {
    File file = getFile();
    if (null == file) {
      log.error("没有找到文件");
      return new ArrayList<>();
    }
    Map<String, FlowDTO> map = new HashMap<>();
    String deviceNo = applicationContext.getId();
    try (FileInputStream inputStream = new FileInputStream(file);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      String str = null;
      Date writeTime = null;
      Boolean fag = null;
      while ((str = bufferedReader.readLine()) != null) {
        if (StringUtil.isNullOrEmpty(str)) continue;
        if (str.contains("Flow write at")) {
          writeTime = DateUtil.StringToDate(str.replace("Flow write at ", ""));
        }
        if (str.contains("Chain INPUT")) {
          fag = true;
        } else if (str.contains("Chain OUTPUT")) {
          fag = false;
        }
        if (fag == null || writeTime == null) continue;
        List<String> strs = Arrays.stream(str.split(" ")).filter(s -> !s.equals("")).collect(Collectors.toList());
        if (strs.size() < 10) continue;
        val id = deviceNo + writeTime.getTime();
        val quantity = getLquantity(strs.get(1));
        String protocol = strs.get(2);
        String prots = strs.get(9);
        if (fag) {
          //input数据
          val port = Long.parseLong(prots.replace("dpt:", ""));
          //根据唯一标记 找出对应的数据
          String key = getKey(id, protocol, port);
          FlowDTO dto = map.get(key);
          if (dto == null) {
            dto = new FlowDTO(id, deviceNo, protocol, port, quantity, null, writeTime);
          } else {
            dto.setFlowPut(quantity);
          }
          if ((null != dto.getFlowPut() && dto.getFlowPut() > 0)
            || (null != dto.getFlowPut() && dto.getFlowPut() > 0)) map.put(key, dto);
        } else {
          //out数据
          val port = Long.parseLong(prots.replace("spt:", ""));
          //根据唯一标记 找出对应的数据
          String key = getKey(id, protocol, port);
          FlowDTO dto = map.get(key);
          if (dto == null) {
            dto = new FlowDTO(id, deviceNo, protocol, port, null, quantity, writeTime);
          } else {
            dto.setFlowOut(quantity);
          }
          if ((null != dto.getFlowPut() && dto.getFlowPut() > 0)
            || (null != dto.getFlowPut() && dto.getFlowPut() > 0)) map.put(key, dto);
        }
      }
    } catch (Exception e) {
      log.error("解析流量统计文件异常", e);
    }
    return map.values();
  }

  /**
   * 获取一个文件下的同一个批次的唯一标记
   *
   * @param id       文件唯一批次
   * @param protocol 协议
   * @param port     端口
   * @return 返回唯一的key
   */
  String getKey(String id, String protocol, Long port) {
    return id + protocol + port;
  }


  /**
   * 单位转换
   *
   * @param quantity 数据
   * @return 转换成字节  单位  B
   */
  Long getLquantity(String quantity) {
    var lquantity = 0L;
    try {
      lquantity = Long.parseLong(quantity);
    } catch (Exception e) {
      if (quantity.contains("K")) {
        lquantity = Long.parseLong(quantity.replace("K", "")) * 1024;
      } else if (quantity.contains("M")) {
        lquantity = Long.parseLong(quantity.replace("M", "")) * 1024 * 1024;
      } else if (quantity.contains("G")) {
        lquantity = Long.parseLong(quantity.replace("G", "")) * 1024 * 1024 * 1024;
      } else {
        log.warn("未知单位 :{}", quantity);
      }
    }
    return lquantity;
  }

  /**
   * 获取流量文件
   *
   * @return 返回文件
   */
  File getFile() {
    File file = new File(config.getPathHome() + config.getFlowFile());
    if (!file.exists()) return null;
    return file;
  }
}
