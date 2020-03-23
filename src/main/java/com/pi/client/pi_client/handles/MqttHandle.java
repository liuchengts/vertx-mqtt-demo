package com.pi.client.pi_client.handles;

import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttHandle {
  MqttClient client = MqttClient.create(Vertx.vertx());

  public void start() {
    client.connect(1883, "mqtt.eclipse.org", s -> {
      client.disconnect();
    });
    client.publishHandler(s -> {
      log.info("There are new message in topic: " + s.topicName());
      log.info("Content(as string) of the message: " + s.payload().toString());
      log.info("QoS: " + s.qosLevel());
    }).subscribe("lot-admin", 2);
  }


}
