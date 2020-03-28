package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PiApplication extends AbstractVerticle {
  ApplicationContext applicationContext = new ApplicationContext();

  public static void main(String[] args) {
    new PiApplication().start();
  }

  @Override
  public void start() {
    init();
    service();
  }

  @Override
  public void stop() {
    applicationContext.getHandleAction().close();
  }

  void init() {
    applicationContext.setVertx(Vertx.vertx());
    applicationContext.setHandleAction(new HandleAction(applicationContext));
  }

  void service() {
    try {
      new MqttService(applicationContext);
    } catch (Exception e) {
      log.error("MqttService error", e);
    }
    try {
      new HttpService(applicationContext);
    } catch (Exception e) {
      log.error("HttpService error", e);
    }

  }
}
