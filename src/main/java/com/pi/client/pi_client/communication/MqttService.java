package com.pi.client.pi_client.communication;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.ResponseDTO;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.json.Json;
import io.vertx.mqtt.MqttClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.vertx.core.buffer.Buffer;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MqttService {
  @Getter
  MqttClient mqttClient;
  static ThreadLocal<LinkedList<String>> cache = new ThreadLocal<>();
  static ReentrantLock lock = new ReentrantLock();

  public MqttService(ApplicationContext applicationContext) {
    cache.set(new LinkedList<>());
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
        Buffer buffer = pub.payload();
        log.info("Content(as string) of the message: " + buffer.toString());
//        log.info("QoS: " + pub.qosLevel());
        applicationContext.getHandleAction().handle(buffer.toJsonObject());
      });
  }


  public void publish(ResponseDTO responseDTO) {
    String json = Json.encode(responseDTO);
    try {
      lock.lock();
      mqttClient.publish("lot-pi", Buffer.buffer(json), MqttQoS.AT_LEAST_ONCE, false, false).clientId();
      LinkedList<String> cacheLocal = new LinkedList<>(cache.get());
      cacheLocal.forEach(s -> mqttClient.publish("lot-pi", Buffer.buffer(s), MqttQoS.AT_LEAST_ONCE, false, false).clientId());
      cache.get().removeAll(cacheLocal);
    } catch (Exception e) {
      cache.get().add(json);
    } finally {
      lock.unlock();
    }
  }

}
