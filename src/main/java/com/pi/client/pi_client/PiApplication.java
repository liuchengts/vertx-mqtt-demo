package com.pi.client.pi_client;

import com.pi.client.pi_client.handles.HttpHandle;
import io.vertx.core.AbstractVerticle;

public class PiApplication extends AbstractVerticle {
  public static void main(String[] args) throws Exception {
    service();
  }

  static void service() throws Exception {
    new HttpHandle().start();
  }
}
