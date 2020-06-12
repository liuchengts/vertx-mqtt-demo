package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.service.FlowService;
import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PiApplication extends AbstractVerticle {
  ApplicationContext applicationContext = new ApplicationContext();

  @Override
  public void start() {
    log.info("开始启动 *************************");
    init();
    service();
  }

  @Override
  public void stop() {
    applicationContext.getHandleAction().close();
  }

  void init() {
    applicationContext.setProcessArgs(processArgs());
    applicationContext.setVertx(vertx);
    applicationContext.setHandleAction(new HandleAction(applicationContext));
  }

  void service() {
    try {
      applicationContext.setMqttService(new MqttService(applicationContext));
    } catch (Exception e) {
      log.error("MqttService error", e);
    }
    try {
      applicationContext.setHttpService(new HttpService(applicationContext));
    } catch (Exception e) {
      log.error("HttpService error", e);
    }
    try {
      applicationContext.setFlowService(new FlowService(applicationContext));
    } catch (Exception e) {
      log.error("HttpService error", e);
    }
  }
}
