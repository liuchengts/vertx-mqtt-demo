package com.pi.client.pi_client.handles;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import lombok.extern.slf4j.Slf4j;
import io.vertx.core.buffer.Buffer;

@Slf4j
public class MqttService {
  static MqttClient client;
  static Vertx vertx = Vertx.vertx();

  public static void start() {
    client = MqttClient.create(vertx);
    client.connect(1883, "mqtt.ayouran.com", c -> {
      if (c.succeeded()) {
        log.info("Connected to a server");
        client.subscribe("lot-admin", MqttQoS.AT_LEAST_ONCE.value());
      } else {
        log.error("Failed to connect to a server");
        log.error("error", c.cause());
      }
    })
      .subscribeCompletionHandler(sub -> {
        log.info("messageId: " + sub.messageId());
        log.info("grantedQoSLevels: " + sub.grantedQoSLevels());
      })
      .publishHandler(pub -> {
        log.info("There are new message in topic: " + pub.topicName());
        log.info("Content(as string) of the message: " + pub.payload().toString());
        log.info("QoS: " + pub.qosLevel());
      });
  }

  public static void publish(Buffer payload) {
    client.publish("lot-pi", payload, MqttQoS.AT_LEAST_ONCE, false, false);
  }

}
