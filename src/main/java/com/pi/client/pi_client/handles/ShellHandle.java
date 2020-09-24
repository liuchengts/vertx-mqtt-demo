package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ShellHandle {
  ApplicationContext applicationContext;
  Config config;

  public ShellHandle(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.config = applicationContext.getConfig();
    updateShell();
    networkIp();
  }


  /**
   * 获取外网ip脚本
   */
  public void networkIp() {
    log.info("[脚本执行] 获取外网ip脚本");
    Thread thread = new Thread(() -> {
      List<String> list = ShellUtils.exec(config.getPathShellRoot(), config.getShellIp());
      this.config.setNetworkIp(Json.decodeValue(String.join("", list), HashMap.class).get(KeyConstant.IP_ORIGIN).toString());
      applicationContext.setConfig(this.config);
    });
    thread.start();
  }

  /**
   * 更新脚本
   */
  public void updateShell() {
    log.info("[脚本执行] 更新脚本");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellGitPull()), config.getPathShellRoot()).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
    log.info("shell handle");
    try {
      updateShell();
    } catch (Exception e) {
      log.error("指令处理失败", e);
    }
  }

}
