package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HttpService;
import com.pi.client.pi_client.handles.MqttService;
import com.pi.client.pi_client.model.ResponseDTO;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PiApplication extends AbstractVerticle {
  static final Vertx vertx = Vertx.vertx();

  public static void main(String[] args) {
    service();
  }

  @Override
  public void start() {
    service();
  }

  static void service() {
    MqttService mqttService = null;
    try {
      mqttService = new MqttService(vertx);
    } catch (Exception e) {
      log.error("MqttService error", e);
    }
    try {
      new HttpService(vertx, mqttService);
    } catch (Exception e) {
      log.error("HttpService error", e);
    }

  }
}
