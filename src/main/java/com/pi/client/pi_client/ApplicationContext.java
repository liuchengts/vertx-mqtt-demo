package com.pi.client.pi_client;

import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import lombok.Data;

@Data
public class ApplicationContext {
  MqttService mqttService;
  HttpService httpService;
  Vertx vertx;
}
