package com.pi.client.pi_client.handles;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import lombok.extern.slf4j.Slf4j;
import io.vertx.core.buffer.Buffer;

@Slf4j
public class MqttService {
  static MqttClient client;

  public static void start() {
    client = MqttClient.create(Vertx.vertx());
    client.connect(1883, "mqtt.ayouran.com", s -> {
      client.disconnect();
    });
    client.publishHandler(s -> {
      log.info("There are new message in topic: " + s.topicName());
      log.info("Content(as string) of the message: " + s.payload().toString());
      log.info("QoS: " + s.qosLevel());
    }).subscribe("lot-admin", MqttQoS.AT_LEAST_ONCE.value());
  }

  public static void publish(Buffer payload) {
    if (null == client) start();
    client.publish("lot-pi", payload, MqttQoS.AT_LEAST_ONCE, false, false);
  }

}
