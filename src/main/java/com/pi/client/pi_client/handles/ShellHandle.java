package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShellHandle {
  MqttService mqttService;

  public ShellHandle(ApplicationContext applicationContext) {
    this.mqttService = applicationContext.getMqttService();
  }


  /**
   * 更新脚本
   */
  void updateShell() {
    log.info("[脚本执行] 更新脚本");
    new Thread(() -> ShellUtils.exec("get-pull.sh")).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
//    String data = jsonObject.getString(KeyConstant.DATA);
    try {
      updateShell();
    } catch (Exception e) {
      log.error("指令处理失败", e);
    }
  }

}
