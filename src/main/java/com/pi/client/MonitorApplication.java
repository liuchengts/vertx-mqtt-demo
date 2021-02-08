package com.pi.client.pi_client;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 创建一个收取mqtt的消息处理器
 */
@Slf4j
public class MonitorApplication extends AbstractVerticle {
  static boolean dev = false;
  static Vertx vertx;
  static MqttClient mqttClient;
  static final Integer MQTT_PORT = 1883;
  static final String MQTT_IP = "mqtt.ayouran.com";
  static final String MQTT_SUBSCRIBE = "test-mqtt";
  static final String MQTT_PUBLISH = "test-mqtt";

  public static void main(String[] args) {
    dev = true;
    Vertx.vertx().deployVerticle(MonitorApplication.class.getName());
    vertx = Vertx.vertx();
    log.info(" JVM running for ok");
  }

  @Override
  public void start() {
    log.info("开始启动 *************************运行模式:{}", (dev ? "本地开发模式" : "jar运行模式"));
    try {
      init();
      service();
    } catch (Exception e) {
      log.error("启动异常", e);
    }
    log.info(" JVM running for ok");

    //test  发送大量字节
    try {
      Thread.sleep(3000l);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
   //todo 这里增减发送内容会出现不一样的情况，请在此处尝试
    publish("@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x" +
      "@The Vert.x core APIs contain the backbone for writing Vert.x applications and low-level support for HTTP, TCP, UDP, file system, asynchronous streams and many other building blocks. It is also used by many other components of Vert.x");

  }

  @Override
  public void stop() {
    vertx.close();
  }

  void init() throws Exception {
    log.info("propertyNames:{}", System.getProperties().stringPropertyNames());
    vertx = Vertx.vertx();
  }

  void service() {
    try {
      MqttClientOptions mqttClientOptions = new MqttClientOptions();
//    mqttClientOptions.setMaxInflightQueue(9999);
      mqttClientOptions.setAutoKeepAlive(true);
      mqttClient = MqttClient.create(vertx, mqttClientOptions);
      mqttClient.connect(MQTT_PORT, MQTT_IP, c -> {
        if (c.succeeded()) {
          mqttClient.subscribe(MQTT_SUBSCRIBE, 2);
          log.info("连接 mqtt 服务器 成功");
        } else {
          log.error("连接 mqtt 服务器 失败", c.cause());
        }
      })
        .publishHandler(pub -> {
          String msg = pub.payload().toString();
          log.info("从连接收到消息 message:{}", msg);
          log.info("从连接收到消息 message 的长度:{}", msg.length());
        });
    } catch (Exception e) {
      log.error("error", e);
    }
    log.info("********************* service ************************************");
  }

  static ThreadLocal<LinkedList<String>> cache = new ThreadLocal<>();
  static ReentrantLock lock = new ReentrantLock();

  public Boolean publish(String json) {
    log.info("发送的 msg消息长度 :{} ", json.length());
    log.info("发送的 msg消息字节 :{} ", json.getBytes().length);
    boolean fag;
    try {
      lock.lock();
      if (!mqttClient.isConnected()) {
        log.warn("重新创建mqtt客户端实例");
        service();
      }
      fag = mqttClient.publish(MQTT_PUBLISH, Buffer.buffer(json), MqttQoS.AT_LEAST_ONCE, false, false).isConnected();
      if (fag) {
        LinkedList<String> cacheLocal = new LinkedList<>(getCache());
        cacheLocal.forEach(s -> mqttClient.publish(MQTT_PUBLISH, Buffer.buffer(s), MqttQoS.AT_LEAST_ONCE, false, false));
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
