package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.model.ResponseDTO;
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
  MqttService mqttService;
  HttpServer httpServer;
  Vertx vertx;
  static int port = 8080;

  public HttpService(Vertx vertx, MqttService mqttService) {
    this.vertx = vertx;
    this.mqttService = mqttService;
    httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.post("/post").handler(req -> {
      req.request().bodyHandler(body -> {
        req.response()
          .putHeader("content-type", "application/json")
          .end(Json.encode(handle(body.toJsonObject())));
      });
    });
    httpServer.requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          log.info("HTTP server started on port ", port);
        }
      });
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   * @return 出参
   */
  ResponseDTO handle(JsonObject jsonObject) {
    ResponseDTO responseDTO = new ResponseDTO();
    responseDTO.setType(ResponseDTO.Type.OK);
    responseDTO.setMsg("");
    try {
      if (null == jsonObject) return responseDTO;
      log.info("jsonObject:{}", jsonObject.toString());
      if (KeyConstant.WIFI.equals(jsonObject.getString(KeyConstant.TYPE))) {
        configFlie(jsonObject.getString(KeyConstant.SSID), jsonObject.getString(KeyConstant.PWD));
        toMqtt();
      } else if (KeyConstant.CLOSE.equals(jsonObject.getString(KeyConstant.TYPE))) {
        httpServer.close();
      } else {
        responseDTO.setType(ResponseDTO.Type.OK);
        responseDTO.setMsg("未知的请求类型");
      }
    } catch (Exception e) {
      responseDTO.setType(ResponseDTO.Type.ERROR);
      responseDTO.setMsg(e.getMessage());
      log.error("处理异常:", e);
    }
    return responseDTO;
  }

  /**
   * 配置wifi
   *
   * @param ssid ssid
   * @param pwd  密码
   * @throws Exception
   */
  void configFlie(String ssid, String pwd) throws Exception {
    List<String> contents = new ArrayList<>();
    contents.add("country=CN\r\n");
    contents.add("ctrl_interface=DIR=/var/run/wpa_supplicant  GROUP=netdev\r\n");
    contents.add("update_config=1\r\n");
    contents.add("network={\r\n");
    contents.add("    ssid=" + ssid + "\r\n");
    contents.add("    psk=" + pwd + "\r\n");
    contents.add("    priority=100\r\n");
    contents.add("}");
    contents.add("network={\r\n");
    contents.add("    ssid=\"lc\"\r\n");
    contents.add("    psk=\"liucheng\"\r\n");
    contents.add("    priority=99\r\n");
    contents.add("}");
    FileUtils.outFile(KeyConstant.WPA_SUPPLICANT_PATH, contents);
    contents.clear();
    contents.add("auto lo\r\n");
    contents.add("iface lo inet loopback\r\n");
    contents.add("auto wlan0\r\n");
    contents.add("allow-hotplug wlan0\r\n");
    contents.add("iface wlan0 inet dhcp\r\n");
    contents.add("wpa_conf /etc/wpa_supplicant/wpa_supplicant.conf\r\n");
    contents.add("auto eth0\r\n");
    contents.add("iface eth0 inet dhcp\r\n");
    FileUtils.outFile(KeyConstant.INTERFACES_PATH, contents);
    ShellUtils.exec(KeyConstant.SHELL_PATH_WIFI);


  }

  /**
   * 发送mqtt消息
   */
  void toMqtt() {
    Map<String, Object> map = new HashMap<>();
    map.put("ip", checkNetwork());
    if (null == mqttService) this.mqttService = new MqttService(vertx);
    mqttService.publish(Buffer.buffer(Json.encode(ResponseDTO.builder()
      .type(ResponseDTO.Type.MQTT)
      .msg("入网")
      .data(Json.encode(map))
      .build())));
  }

  /**
   * 检测网络ip
   *
   * @return 返回外网ip
   */
  String checkNetwork() {
    AtomicReference<String> ipAtomic = new AtomicReference<>();
    WebClient.create(vertx).getAbs(KeyConstant.CHECK_NETWORK_URL).send(handle -> {
      // 处理响应的结果
      if (handle.succeeded())
        ipAtomic.set(Json.decodeValue(handle.result().body(), Map.class).get(KeyConstant.IP_ORIGIN).toString());
    });
    while (null == ipAtomic.get()) {
    }
    return ipAtomic.get();
  }
}
