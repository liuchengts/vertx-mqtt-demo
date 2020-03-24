package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.handles.WifiHandle;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttClient;
import lombok.extern.slf4j.Slf4j;
import io.vertx.core.buffer.Buffer;

@Slf4j
public class MqttService {
  MqttClient client;
  WifiHandle wifiHandle;

  public MqttClient getClient() {
    return client;
  }

  public MqttService(ApplicationContext applicationContext) {
    client = MqttClient.create(applicationContext.getVertx());
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
        this.wifiHandle = new WifiHandle(applicationContext);
        wifiHandle.handle(pub.payload().toJsonObject());
      });
  }

  public void publish(Buffer payload) {
    client.publish("lot-pi", payload, MqttQoS.AT_LEAST_ONCE, false, false);
  }

}
