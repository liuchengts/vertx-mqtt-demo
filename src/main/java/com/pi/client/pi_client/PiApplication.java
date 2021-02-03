package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.service.FlowService;
import com.pi.client.pi_client.communication.HttpService;
import com.pi.client.pi_client.communication.MqttService;
import com.pi.client.pi_client.service.HeartbeatService;
import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PiApplication extends AbstractVerticle {
  static ApplicationContext applicationContext = new ApplicationContext();
  static boolean dev = false;

  public static void main(String[] args) {
    dev = true;
    Vertx.vertx().deployVerticle(PiApplication.class.getName());
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
  }

  @Override
  public void stop() {
    applicationContext.getHandleAction().close();
  }

  void init() throws Exception {
    log.info("propertyNames:{}", System.getProperties().stringPropertyNames());
    Config config = new Config(dev);
    applicationContext.setId(System.getProperty(config.getArgsKey()));
    if (StringUtil.isNullOrEmpty(applicationContext.getId())) throw new RuntimeException("必要的启动参数不存在");
    if (!dev) {
      applicationContext.setProcessArgs(processArgs());
      log.info("processArgs:{}", applicationContext.getProcessArgs());
    } else {
      config.setPathShellRoot("/Users/liucheng/it/lc/shell-deployment/");
      config.setPathHome("/Users/liucheng/it/lc/");
      config.setPathRinetdConfig("/Users/liucheng/it/lc/rinetd.conf");
      config.setMqttIp("localhost");
    }
    applicationContext.setVertx(vertx);
    applicationContext.setConfig(config);
    applicationContext.setHandleAction(new HandleAction(applicationContext));
  }

  void service() {
    try {
      applicationContext.setMqttService(new MqttService(applicationContext));
    } catch (Exception e) {
      log.error("MqttService error", e);
    }

//    try {
//      applicationContext.setKafkaService(new KafkaService(applicationContext));
//    } catch (Exception e) {
//      log.error("KafkaService error", e);
//    }

    try {
      applicationContext.setHttpService(new HttpService(applicationContext));
    } catch (Exception e) {
      log.error("HttpService error", e);
    }
//    try {
//      applicationContext.setSocketService(new SocketService(applicationContext));
//    } catch (Exception e) {
//      log.error("SocketService error", e);
//    }
    try {
      applicationContext.setHeartbeatService(new HeartbeatService(applicationContext));
    } catch (Exception e) {
      log.error("HeartbeatService error", e);
    }
    if (!dev) {
      try {
        applicationContext.setFlowService(new FlowService(applicationContext));
      } catch (Exception e) {
        log.error("FlowService error", e);
      }
    }
    log.info("********************* ok ************************************");
  }
}
