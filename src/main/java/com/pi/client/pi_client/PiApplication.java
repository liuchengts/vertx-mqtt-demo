package com.pi.client.pi_client;

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
    new PiApplication().service();
  }

  @Override
  public void start() {
    service();
  }

  @Override
  public void stop() {
    applicationContext.getHttpService().getHttpServer().close();
    applicationContext.getMqttService().getClient().disconnect();
  }

  void service() {
    applicationContext.setVertx(Vertx.vertx());
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

  }
}
