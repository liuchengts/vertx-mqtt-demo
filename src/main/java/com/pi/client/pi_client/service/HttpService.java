package com.pi.client.pi_client.service;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.handles.HandleAction;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpService {
  @Getter
  HttpServer httpServer;
  static int port = 8888;

  public HttpService(ApplicationContext applicationContext) {
    this.httpServer = applicationContext.getVertx().createHttpServer();
    Router router = Router.router(applicationContext.getVertx());
    router.get("/index").handler(req -> req.response()
      .putHeader("content-type", "application/json")
      .end("ok"));
    router.post("/post").handler(req -> req.request().bodyHandler(body -> req.response()
      .putHeader("content-type", "application/json")
      .end(Json.encode(applicationContext.getHandleAction().handle(body.toJsonObject())))));
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
