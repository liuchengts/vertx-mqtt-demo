package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HttpService;
import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PiApplication extends AbstractVerticle {
  public static void main(String[] args) {
    service();
  }

  static void service() {
    try {
      new HttpService().start();
    } catch (Exception e) {
      log.error("HttpServer error", e);
    }
  }
}
