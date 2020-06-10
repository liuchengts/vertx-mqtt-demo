package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.service.FlowService;
import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import io.vertx.core.Vertx;
import lombok.Data;
import lombok.Getter;

public class ApplicationContext {
  @Getter
  HandleAction handleAction;
  @Getter
  MqttService mqttService;
  @Getter
  HttpService httpService;
  @Getter
  FlowService flowService;
  @Getter
  Vertx vertx;
  @Getter
  String id;

  protected void setHandleAction(HandleAction handleAction) {
    this.handleAction = handleAction;
  }

  protected void setMqttService(MqttService mqttService) {
    this.mqttService = mqttService;
  }

  protected void setHttpService(HttpService httpService) {
    this.httpService = httpService;
  }

  protected void setFlowService(FlowService flowService) {
    this.flowService = flowService;
  }

  protected void setVertx(Vertx vertx) {
    this.vertx = vertx;
  }

  protected void setId(String id) {
    this.id = id;
  }
}
