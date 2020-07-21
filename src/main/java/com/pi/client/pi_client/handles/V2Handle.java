package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class V2Handle {
  Config config;

  public V2Handle(ApplicationContext applicationContext) {
    this.config = applicationContext.getConfig();
  }


  /**
   * 应用流量文件
   */
  void restartShell() {
    log.info("[脚本执行] 应用流量文件");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellV2ray())).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
    String data = jsonObject.getString(KeyConstant.DATA);
    try {
      FileUtils.outFile(config.getPathV2rayConfig() + config.getConfigTmp(), data);
      restartShell();
    } catch (Exception e) {
      log.error("指令处理失败", e);
    }
  }

}
