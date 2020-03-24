package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.handles.WifiHandle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpService {
  HttpServer httpServer;
  WifiHandle wifiHandle;
  static int port = 8888;

  public HttpServer getHttpServer() {
    return httpServer;
  }

  public HttpService(ApplicationContext applicationContext) {
    this.httpServer = applicationContext.getVertx().createHttpServer();
    this.wifiHandle = new WifiHandle(applicationContext);
    Router router = Router.router(applicationContext.getVertx());
    router.get("/index").handler(req -> req.response()
      .putHeader("content-type", "application/json")
      .end("ok"));
    router.post("/post").handler(req -> req.request().bodyHandler(body -> req.response()
      .putHeader("content-type", "application/json")
      .end(Json.encode(wifiHandle.handle(body.toJsonObject())))));
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
