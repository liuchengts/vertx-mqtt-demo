package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Slf4j
public class RinetdHandle {
  Config config;

  public RinetdHandle(ApplicationContext applicationContext) {
    this.config = applicationContext.getConfig();
  }


  /**
   * 应用 rinetd 文件
   */
  void restartShell() {
    log.info("[脚本执行] 应用 rinetd 文件");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellRinetd())).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
    log.info("rinetd handle");
    String data = jsonObject.getString(KeyConstant.DATA);
    try {
      FileUtils.outFile(config.getPathRinetdConfig() + config.getConfigTmp(), data);
      restartShell();
    } catch (Exception e) {
      log.error("指令处理失败", e);
    }
  }

}
