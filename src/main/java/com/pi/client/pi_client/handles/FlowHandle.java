package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowHandle {
  MqttService mqttService;
  static final String CONFIG_PATH = "/tmp/v2ray/config.json";
  static final String TMP_CONFIG_NAME = ".tmp";

  public FlowHandle(ApplicationContext applicationContext) {
    this.mqttService = applicationContext.getMqttService();
  }


  /**
   * 应用流量文件
   */
  void restartShell() {
    log.info("[脚本执行] 应用流量文件");
    new Thread(() -> ShellUtils.exec("flow/v2ray.sh")).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
    String data = jsonObject.getString(KeyConstant.DATA);
//    log.info("flow 收到指令 :{}", data);
    log.info("flow 收到指令");
    try {
      FileUtils.outFile(CONFIG_PATH + TMP_CONFIG_NAME, data);
      restartShell();
    } catch (Exception e) {
      log.error("指令处理失败", e);
    }
  }

}
