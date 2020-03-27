package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import io.vertx.core.Vertx;
import lombok.Data;

@Data
public class ApplicationContext {
  HandleAction handleAction;
  MqttService mqttService;
  HttpService httpService;
  Vertx vertx;
}
