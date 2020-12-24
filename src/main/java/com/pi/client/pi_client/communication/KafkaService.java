package com.pi.client.pi_client.communication;

//import com.pi.client.pi_client.ApplicationContext;
//import com.pi.client.pi_client.Config;
//import com.pi.client.pi_client.model.ResponseDTO;
//import io.vertx.core.json.Json;
//import io.vertx.core.json.JsonObject;
//import io.vertx.kafka.client.consumer.KafkaConsumer;
//import io.vertx.kafka.client.producer.KafkaProducer;
//import io.vertx.kafka.client.producer.KafkaProducerRecord;
//import io.vertx.kafka.client.serialization.JsonObjectDeserializer;
//import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

//import java.util.LinkedList;
//import java.util.Properties;
//import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class KafkaService {
//  Config config;
//  @Getter
//  KafkaConsumer<String, String> consumer;
//  @Getter
//  KafkaProducer<String, String> producer;
//  static ThreadLocal<LinkedList<String>> cache = new ThreadLocal<>();
//  static ReentrantLock lock = new ReentrantLock();
//  static Properties consumerConfig = new Properties();
//  static Properties producerConfig = new Properties();
//
//  static {
//
//    consumerConfig.put("key.deserializer", JsonObjectDeserializer.class);
//    consumerConfig.put("value.deserializer", JsonObjectDeserializer.class);
//    consumerConfig.put("group.id", "lot_group");
//    consumerConfig.put("auto.offset.reset", "earliest");
//    consumerConfig.put("enable.auto.commit", "false");
//
//
//    producerConfig.put("key.serializer", JsonObjectDeserializer.class);
//    producerConfig.put("value.serializer", JsonObjectDeserializer.class);
//    producerConfig.put("acks", "1");
//  }
//
//  public KafkaService(ApplicationContext applicationContext) {
//    this.config = applicationContext.getConfig();
//    consumerConfig.put("bootstrap.servers", config.getKafkaIpAndPort());
//    producerConfig.put("bootstrap.servers", config.getKafkaIpAndPort());
//    consumer = KafkaConsumer.create(applicationContext.getVertx(), consumerConfig);
//    consumer.subscribe(config.getKafkaSubscribe());
//    consumer.handler(record -> {
//      log.info("Processing key=" + record.key() + ",value=" + record.value() +
//        ",partition=" + record.partition() + ",offset=" + record.offset());
//      applicationContext.getHandleAction().handle(JsonObject.mapFrom(record.value()));
//    });
//    producer = KafkaProducer.createShared(applicationContext.getVertx(), applicationContext.getId(), producerConfig);
//  }
//
//
//  public void publish(ResponseDTO responseDTO) {
//    String json = Json.encode(responseDTO);
//    lock.lock();
//    try {
//      KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(config.getKafkaPublish(), json);
//      producer.write(record, done -> {
//        if (done.succeeded()) {
//          LinkedList<String> cacheLocal = new LinkedList<>(getCache());
//          cacheLocal.forEach(s -> {
//            producer.write(KafkaProducerRecord.create(config.getKafkaPublish(), json));
//            getCache().remove(s);
//          });
//        } else {
//          getCache().add(json);
//          log.warn("kafka消息发送失败，当前缓存待发送消息条数:" + getCache().size());
//        }
//      });
//    } catch (Exception e) {
//      getCache().add(json);
//      log.warn("kafka消息发送失败，当前缓存待发送消息条数:" + getCache().size());
//    } finally {
//      lock.unlock();
//    }
//    log.info("当前kafka 待发送消息数:" + getCache().size());
//  }
//
//
//  LinkedList<String> getCache() {
//    if (null == cache.get()) cache.set(new LinkedList<>());
//    return cache.get();
//  }

}
