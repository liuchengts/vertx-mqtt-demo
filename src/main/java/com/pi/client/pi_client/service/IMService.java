package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.communication.KafkaService;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.model.ResponseDTO;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class IMService {
  ApplicationContext applicationContext;

  public IMService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  void d() {

  }

}
