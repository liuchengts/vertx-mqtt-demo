package com.pi.client.pi_client;

import com.pi.client.pi_client.communication.KafkaService;
import com.pi.client.pi_client.communication.SocketService;
import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.service.FlowService;
import com.pi.client.pi_client.communication.HttpService;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.service.HeartbeatService;
import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ApplicationContext {

  @Getter
  List<String> processArgs;
  @Getter
  Config config;
  @Getter
  HandleAction handleAction;
  @Getter
  MqttService mqttService;
  @Getter
  KafkaService kafkaService;
  @Getter
  HttpService httpService;
  @Getter
  FlowService flowService;
  @Getter
  HeartbeatService heartbeatService;
  @Getter
  SocketService socketService;
  @Getter
  Vertx vertx;
  @Getter
  String id;
  @Getter
  @Setter
  boolean pacPort;

  protected void setSocketService(SocketService socketService) {
    this.socketService = socketService;
  }

  protected void setKafkaService(KafkaService kafkaService) {
    this.kafkaService = kafkaService;
  }

  protected void setHeartbeatService(HeartbeatService heartbeatService) {
    this.heartbeatService = heartbeatService;
  }

  protected void setProcessArgs(List<String> processArgs) {
    this.processArgs = processArgs;
  }

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
    if (null == vertx) vertx = Vertx.vertx();
    this.vertx = vertx;
  }

  protected void setId(String id) {
    this.id = id;
  }

  public void setConfig(Config config) {
    this.config = config;
  }
}
