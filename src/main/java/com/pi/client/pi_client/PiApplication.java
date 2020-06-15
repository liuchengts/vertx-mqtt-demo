package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.service.FlowService;
import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PiApplication extends AbstractVerticle {
  static ApplicationContext applicationContext = new ApplicationContext();
  static boolean dev = false;
  //启动指定的参数值
  static final String ARGS_KEY = "deviceNo";

  public static void main(String[] args) {
    dev = true;
    Vertx.vertx().deployVerticle(PiApplication.class.getName());
  }

  @Override
  public void start() {
    log.info("开始启动 *************************运行模式:{}", (dev ? "本地开发模式" : "jar运行模式"));
    init();
    service();
  }

  @Override
  public void stop() {
    applicationContext.getHandleAction().close();
  }

  void init() {
    applicationContext.setId(System.getProperty(ARGS_KEY));
    if (StringUtil.isNullOrEmpty(applicationContext.getId())) throw new RuntimeException("必要的启动参数不存在");
    if (!dev) {
      applicationContext.setProcessArgs(processArgs());
      log.info("processArgs:{}", applicationContext.getProcessArgs());
    }
    applicationContext.setVertx(vertx);
    applicationContext.setHandleAction(new HandleAction(applicationContext));
  }

  void service() {
    try {
      applicationContext.setMqttService(new MqttService(applicationContext));
    } catch (Exception e) {
      log.error("MqttService error", e);
    }
    try {
      applicationContext.setHttpService(new HttpService(applicationContext));
    } catch (Exception e) {
      log.error("HttpService error", e);
    }
    try {
      applicationContext.setFlowService(new FlowService(applicationContext));
    } catch (Exception e) {
      log.error("HttpService error", e);
    }
    log.info("********************* ok ************************************");
  }
}
