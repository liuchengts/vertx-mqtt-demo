package com.pi.client.pi_client.communication;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
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
  ApplicationContext applicationContext;
  @Getter
  MqttClient mqttClient;
  Config config;
  static ThreadLocal<LinkedList<String>> cache = new ThreadLocal<>();
  static ReentrantLock lock = new ReentrantLock();

  public MqttService(ApplicationContext applicationContext) {
    this.config = applicationContext.getConfig();
    this.applicationContext = applicationContext;
    client();
  }

  private void client() {
    MqttClientOptions mqttClientOptions = new MqttClientOptions();
    mqttClientOptions.setMaxInflightQueue(9999);
    mqttClient = MqttClient.create(applicationContext.getVertx(), mqttClientOptions);
    mqttClient.connect(config.getMqttPort(), config.getMqttIp(), c -> {
      if (c.succeeded()) {
        log.info("Connected to a server");
        mqttClient.subscribe(config.getMqttSubscribe(), MqttQoS.AT_LEAST_ONCE.value());
      } else {
        log.error("Failed to connect to a server");
        log.error("error", c.cause());
      }
    }).publishHandler(pub -> {
        Buffer buffer = pub.payload();
        log.info("Content(as string) of the message: " + buffer.toString());
        applicationContext.getHandleAction().handle(buffer.toJsonObject());
      });
  }

  public Boolean publish(ResponseDTO responseDTO) {
    boolean fag;
    String json = Json.encode(responseDTO);
    try {
      lock.lock();
      if (!mqttClient.isConnected()) {
       log.info("重新创建mqtt客户端实例");
       client();
      }
      fag = mqttClient.publish(config.getMqttPublish(), Buffer.buffer(json), MqttQoS.AT_LEAST_ONCE, false, false).isConnected();
      if (fag) {
        LinkedList<String> cacheLocal = new LinkedList<>(getCache());
        cacheLocal.forEach(s -> mqttClient.publish(config.getMqttPublish(), Buffer.buffer(s), MqttQoS.AT_LEAST_ONCE, false, false).clientId());
        getCache().removeAll(cacheLocal);
      } else {
        getCache().add(json);
        log.warn("mqtt 消息发送不成功");
      }
    } catch (Exception e) {
      fag = false;
      getCache().add(json);
      log.warn("mqtt 消息发送失败");
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
