package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.commom.FlowDTO;
import com.pi.client.pi_client.communication.KafkaService;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.model.ResponseDTO;
import com.pi.client.pi_client.utlis.ShellUtils;
import com.pi.client.pi_client.utlis.date.DateUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class HeartbeatService {
  KafkaService kafkaService;
  MqttService mqttService;
  ApplicationContext applicationContext;

  public HeartbeatService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.mqttService = applicationContext.getMqttService();
    this.kafkaService = applicationContext.getKafkaService();
    new Thread(this::task).start();
  }

  void task() {
    new Timer(System.currentTimeMillis() + "").schedule(new TimerTask() {
      @Override
      public void run() {
        log.info("定时[" + this.getClass().getName() + "]任务开始执行...");
        mqttService.publish(ResponseDTO.builder()
          .type(ResponseDTO.Type.OK)
          .serviceType(ResponseDTO.ServiceType.HEARTBEAT)
          .msg("心跳")
          .t(applicationContext.getId())
          .build());
      }
    }, new Date(), 1 * 60 * 1000);
  }

}
