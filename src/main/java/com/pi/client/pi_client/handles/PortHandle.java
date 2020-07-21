package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PortHandle {
  Config config;
  ApplicationContext applicationContext;

  public PortHandle(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.config = applicationContext.getConfig();
  }

  /**
   * 增加端口流量统计
   */
  void addPort(String port) {
    log.info("[脚本执行] 增加端口流量统计");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellFlowAddPort(), port, port)).start();
  }

  /**
   * 删除端口流量统计
   */
  void delPort(String port) {
    log.info("[脚本执行] 删除端口流量统计");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellFlowDelPort(), port, port)).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
    HashMap<String, String> map = Json.decodeValue(jsonObject.getString(KeyConstant.DATA), HashMap.class);
    Arrays.stream(map.get(KeyConstant.PORT_OPERATION_ADD).split(",")).forEach(this::addPort);
    Arrays.stream(map.get(KeyConstant.PORT_OPERATION_DEL).split(",")).forEach(this::delPort);
    applicationContext.setPacPort(true);
  }
}
