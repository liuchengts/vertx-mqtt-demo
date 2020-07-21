package com.pi.client.pi_client.communication;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.handles.HandleAction;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class HttpService {
  @Getter
  HttpServer httpServer;
  Config config;

  public HttpService(ApplicationContext applicationContext) {
    this.httpServer = applicationContext.getVertx().createHttpServer();
    this.config = applicationContext.getConfig();
    Router router = Router.router(applicationContext.getVertx());
    router.get("/index").handler(req -> req.response()
      .putHeader("content-type", "application/json")
      .end("ok"));
    router.post("/post").handler(req -> req.request().bodyHandler(body -> req.response()
      .putHeader("content-type", "application/json")
      .end(Json.encode(applicationContext.getHandleAction().handle(body.toJsonObject())))));
    router.get("/pac/*").handler(req -> {
      String fileUrl = config.getPathHome() + req.request().path();
      log.info("fileUrl:{}", fileUrl);
      if (!new File(fileUrl).exists()) {
        log.warn("没有此文件");
        req.response().end("No files");
        return;
      }
      try {
        req.response().sendFile(fileUrl);
      } catch (Exception e) {
        log.error("读取文件失败 fileUrl:{}", fileUrl, e);
        req.response().end("Failed to read file ,:{}", e.getMessage());
      }
    });
    this.httpServer
      .requestHandler(router)
      .listen(config.getHttpServerPort(), http -> {
        if (http.succeeded()) {
          log.info("HTTP server started on port:{}", config.getHttpServerPort());
        } else {
          log.error("error", http.cause());
        }
      });
  }

}
