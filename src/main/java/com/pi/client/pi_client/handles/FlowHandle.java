package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.model.ResponseDTO;
import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
    ShellUtils.exec("flow/v2ray.sh");
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
