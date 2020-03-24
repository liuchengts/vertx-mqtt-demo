package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.handles.WifiConfig;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.model.ResponseDTO;
import com.pi.client.pi_client.service.MqttService;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class HttpService {
  HttpServer httpServer;
  WifiConfig wifiConfig;
  static int port = 8888;

  public HttpServer getHttpServer() {
    return httpServer;
  }

  public HttpService(ApplicationContext applicationContext) {
    this.httpServer = applicationContext.getVertx().createHttpServer();
    this.wifiConfig = new WifiConfig(applicationContext);
    Router router = Router.router(applicationContext.getVertx());
    router.get("/index").handler(req -> req.response()
      .putHeader("content-type", "application/json")
      .end("ok"));
    router.post("/post").handler(req -> req.request().bodyHandler(body -> req.response()
      .putHeader("content-type", "application/json")
      .end(Json.encode(wifiConfig.handle(body.toJsonObject())))));
    this.httpServer
      .requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          log.info("HTTP server started on port:{}", port);
        } else {
          log.error("error", http.cause());
        }
      });
  }

}
