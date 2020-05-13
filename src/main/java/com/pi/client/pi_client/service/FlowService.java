package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.commom.FlowDTO;
import com.pi.client.pi_client.utlis.ShellUtils;
import com.pi.client.pi_client.utlis.date.DateUtil;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
public class FlowService {
  final static String DEVICE_PATH_ROOT = "/root";
  final static String FLOW = "flow.txt";
  MqttService mqttService;

  public FlowService(ApplicationContext applicationContext) {
    this.mqttService = applicationContext.getMqttService();
    task();
  }

  /**
   * 统计流量
   */
  void checkShell() {
    try {
      ShellUtils.exec("flow/check.sh");
    } catch (IOException e) {
      log.error("run flow check.sh error", e);
    }
  }

  /**
   * 清除流量统计
   */
  void clearShell() {
    try {
      ShellUtils.exec("flow/clear.sh");
    } catch (IOException e) {
      log.error("run flow clear.sh error", e);
    }
  }

  void task() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 18); //小时
    calendar.set(Calendar.MINUTE, 29);//分钟
    calendar.set(Calendar.SECOND, 0);//秒
    Date date = calendar.getTime(); //第一次执行定时任务的时间
    //如果第一次执行定时任务的时间 小于当前的时间
    //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
    if (date.before(new Date())) date = DateUtil.addDay(date, 1);
    new Timer(System.currentTimeMillis() + "").schedule(new TimerTask() {
      @Override
      public void run() {
        log.info("定时任务开始执行...");
        checkShell();
        mqttService.publish(Json.encode(handleFlowText()));
        clearShell();
      }
    }, date, 24 * 60 * 60 * 1000);
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
      return null;
    }
    FileInputStream inputStream = null;
    BufferedReader bufferedReader = null;
    Map<String, FlowDTO> map = new HashMap<>();
    String deviceNo = getDeviceNo(file.getAbsolutePath());
    try {
      inputStream = new FileInputStream(file);
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
          map.put(key, dto);
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
          map.put(key, dto);
        }
      }
    } catch (Exception e) {
      log.error("解析流量统计文件异常", e);
    } finally {
      if (null != inputStream) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (null != bufferedReader) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
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
   * 获取设备编号
   * 需要注意 文件的最后的底层目录就是设备编号
   * 例如  /root/abcdefg/flow.txt 设备编号为 abcdefg
   *
   * @param path 文件路径
   * @return 返回设备编号
   */
  String getDeviceNo(String path) {
    val ph = path.substring(0, path.lastIndexOf("/"));
    return ph.substring(ph.lastIndexOf("/") + 1);
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
    AtomicReference<Optional<File>> fileAtomicReference = new AtomicReference<>();
    Arrays.stream(Objects.requireNonNull(new File(DEVICE_PATH_ROOT).listFiles()))
      .filter(File::isDirectory)
      .filter(f -> !".".equals(f.getName().substring(0, 1)))
      .filter(f -> f.listFiles() != null)
      .forEach(f -> {
        Optional<File> optionalFile = Arrays.stream(Objects.requireNonNull(f.listFiles()))
          .filter(fi -> FLOW.equals(fi.getName()))
          .findFirst();
        if (optionalFile.isPresent()) fileAtomicReference.set(optionalFile);
      });
    if (fileAtomicReference.get().isPresent()) return fileAtomicReference.get().get();
    return null;
  }
}
