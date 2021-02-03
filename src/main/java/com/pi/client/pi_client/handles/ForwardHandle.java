package com.pi.client.pi_client.handles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.commom.ForwardDTO;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.JacksonCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ForwardHandle {
  Config config;

  public ForwardHandle(ApplicationContext applicationContext) {
    this.config = applicationContext.getConfig();
  }


  /**
   * 增加转发规则
   * 从 A 转发到 B
   *
   * @param portA 接受端口
   * @param portB 被接受端口
   */
  void addShell(String portA, String portB) {
    log.info("[脚本执行] 增加端口转发");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellForward(), "add", portA, portB)).start();
  }

  /**
   * 删除转发规则
   * 从 A 转发到 B
   *
   * @param portA 接受端口
   * @param portB 被接受端口
   */
  void delShell(String portA, String portB) {
    log.info("[脚本执行] 删除端口转发");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellForward(), "del", portA, portB)).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
    log.info("forward handle");
    try {
      jsonObject.getJsonObject(KeyConstant.DATA)
        .stream()
        .map(d -> {
          ForwardDTO dto = new ForwardDTO();
          if (d.getKey().contains("portA")) dto.setPortA(Long.valueOf(d.getValue().toString()));
          else if (d.getKey().contains("portB")) dto.setPortB(Long.valueOf(d.getValue().toString()));
          else if (d.getKey().contains("forward")) dto.setForward(d.getValue().toString());
          return dto;
        })
        .forEach(dto -> {
          if (StringUtil.isNullOrEmpty(dto.getForward())
            || null == dto.getPortA() || null == dto.getPortB()) return;
          if ("add".equals(dto.getForward())) {
            addShell(dto.getPortA().toString(), dto.getPortB().toString());
          } else if ("del".equals(dto.getForward())) {
            delShell(dto.getPortA().toString(), dto.getPortB().toString());
          } else {
            log.warn("无法解析的转发规则:{}", dto);
          }
        });
//      String dataJson = jsonObject.getString(KeyConstant.DATA);
//      JacksonCodec.decodeValue(dataJson, new TypeReference<List<ForwardDTO>>() {
//      }).forEach(dto -> {
//        if (StringUtil.isNullOrEmpty(dto.getForward())
//          || null == dto.getPortA() || null == dto.getPortB()) return;
//        if ("add".equals(dto.getForward())) {
//          addShell(dto.getPortA().toString(), dto.getPortB().toString());
//        } else if ("del".equals(dto.getForward())) {
//          delShell(dto.getPortA().toString(), dto.getPortB().toString());
//        } else {
//          log.warn("无法解析的转发规则:{}", dto);
//        }
//      });
    } catch (Exception e) {
      log.error("指令处理失败", e);
    }
  }
}
