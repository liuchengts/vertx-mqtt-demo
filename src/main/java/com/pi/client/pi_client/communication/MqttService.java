package com.pi.client.pi_client.communication;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.ResponseDTO;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MqttService {
  @Getter
  MqttClient mqttClient;
  static ThreadLocal<LinkedList<String>> cache = new ThreadLocal<>();
  static ReentrantLock lock = new ReentrantLock();

  public MqttService(ApplicationContext applicationContext) {
    MqttClientOptions mqttClientOptions = new MqttClientOptions();
    mqttClientOptions.setMaxInflightQueue(9999);
    mqttClient = MqttClient.create(applicationContext.getVertx(), mqttClientOptions);
    mqttClient.connect(1883, "mqtt.ayouran.com", c -> {
      if (c.succeeded()) {
        log.info("Connected to a server");
        mqttClient.subscribe("lot-admin", MqttQoS.AT_LEAST_ONCE.value());
      } else {
        log.error("Failed to connect to a server");
        log.error("error", c.cause());
      }
    })
      .publishHandler(pub -> {
        Buffer buffer = pub.payload();
        log.info("Content(as string) of the message: " + buffer.toString());
        applicationContext.getHandleAction().handle(buffer.toJsonObject());
      });
  }

  public Boolean publish(ResponseDTO responseDTO) {
    Boolean fag = true;
    String json = Json.encode(responseDTO);
    try {
      lock.lock();
      mqttClient.publish("lot-pi", Buffer.buffer(json), MqttQoS.AT_LEAST_ONCE, false, false).clientId();
      if (!getCache().isEmpty()) {
        LinkedList<String> cacheLocal = new LinkedList<>(getCache());
        cacheLocal.forEach(s -> mqttClient.publish("lot-pi", Buffer.buffer(s), MqttQoS.AT_LEAST_ONCE, false, false).clientId());
        getCache().removeAll(cacheLocal);
      }
    } catch (Exception e) {
      fag = false;
      getCache().add(json);
      log.warn("mqtt 消息发送失败，当前缓存待发送消息条数:" + getCache().size());
    } finally {
      lock.unlock();
    }
    log.info("当前 mqtt 待发送消息数:" + getCache().size());
    return fag;
  }

  LinkedList<String> getCache() {
    if (null == cache.get()) cache.set(new LinkedList<>());
    return cache.get();
  }

}
