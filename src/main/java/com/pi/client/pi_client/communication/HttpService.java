package com.pi.client.pi_client.communication;

import com.pi.client.pi_client.ApplicationContext;
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
  static int port = 80;
  //  static String FILE_PATH = "/Users/liucheng/it/lc/shell-deployment";
  //  static String FILE_HOME_PATH = "/Users/liucheng/it/lc";
  static String FILE_PATH = ShellUtils.SHELL_ROOT;
  static String FILE_HOME_PATH = ShellUtils.SHELL_HOME;

  public HttpService(ApplicationContext applicationContext) {
    this.httpServer = applicationContext.getVertx().createHttpServer();
    Router router = Router.router(applicationContext.getVertx());
    router.get("/index").handler(req -> req.response()
      .putHeader("content-type", "application/json")
      .end("ok"));
    router.post("/post").handler(req -> req.request().bodyHandler(body -> req.response()
      .putHeader("content-type", "application/json")
      .end(Json.encode(applicationContext.getHandleAction().handle(body.toJsonObject())))));
    router.get("/pac/*").handler(req -> {
      String fileUrl = FILE_PATH + req.request().path();
      log.info("fileUrl:{}", fileUrl);
      try {
        LinkedList<String> fileList = FileUtils.readFile(fileUrl);
        if (fileList.isEmpty()) {
          log.error("读取文件失败,文件不存在 fileUrl:{}", fileUrl);
          req.response().end("Failed to read file, file does not exist");
        }
        if (!"//pac".equals(fileList.get(0))) {
          log.error("写入文件失败,文件被篡改 len 0 :{}", fileList.get(0));
          req.response().end("Writing to file failed. File has been tampered with");
        }
        String httpProxy = "localhost:1087";
        String socksProxy = "localhost:1086";
        fileList.remove(1);
        fileList.remove(1);
        fileList.add(1, "var socks_proxy = '" + socksProxy + "';");
        fileList.add(1, "var http_proxy = '" + httpProxy + "';");
        String outFile = FILE_HOME_PATH + fileUrl;
        FileUtils.outFile(outFile, fileList);
        req.response().sendFile(outFile);
      } catch (Exception e) {
        log.error("读取文件失败 fileUrl:{}", fileUrl, e);
        req.response().end("Failed to read file ,:{}", e.getMessage());
      }
    });
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
