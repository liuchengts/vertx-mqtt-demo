package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.ResponseDTO;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.json.Json;
import io.vertx.mqtt.MqttClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.vertx.core.buffer.Buffer;

@Slf4j
public class MqttService {
  @Getter
  MqttClient mqttClient;


  public MqttService(ApplicationContext applicationContext) {
    mqttClient = MqttClient.create(applicationContext.getVertx());
    mqttClient.connect(1883, "mqtt.ayouran.com", c -> {
      if (c.succeeded()) {
        log.info("Connected to a server");
        mqttClient.subscribe("lot-admin", MqttQoS.AT_LEAST_ONCE.value());
      } else {
        log.error("Failed to connect to a server");
        log.error("error", c.cause());
      }
    })
      .subscribeCompletionHandler(sub -> {
//        log.info("messageId: " + sub.messageId());
//        log.info("grantedQoSLevels: " + sub.grantedQoSLevels());
      })
      .publishHandler(pub -> {
//        log.info("There are new message in topic: " + pub.topicName());
//        log.info("Content(as string) of the message: " + pub.payload().toString());
//        log.info("QoS: " + pub.qosLevel());
        applicationContext.getHandleAction().handle(pub.payload().toJsonObject());
      });
  }

  public void publish(ResponseDTO responseDTO) {
    String json = Json.encode(responseDTO);
//    log.info("send ->>> :{}", json);
    mqttClient.publish("lot-pi", Buffer.buffer(json), MqttQoS.AT_LEAST_ONCE, false, false);
  }

}
