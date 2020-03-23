package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.model.RequestDTO;
import com.pi.client.pi_client.model.ResponseDTO;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.GsonUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpHandle {
  HttpServer httpServer;

  public void start() {
    httpServer = Vertx.vertx().createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "application/json")
        .end(GsonUtils.objectToJson(handle(req.params())));
    }).listen(8080, http -> {
      if (http.succeeded()) {
        log.info("HTTP server started on port 8888");
      }
    });
  }

  /**
   * 处理器
   *
   * @param params 入参
   * @return 出参
   */
  ResponseDTO handle(MultiMap params) {
    ResponseDTO.Type type = ResponseDTO.Type.OK;
    String msg = "";
    try {
      RequestDTO requestDTO = getRequestDTO(params);
      log.info("requestDTO:{}", requestDTO);
      if (RequestDTO.Type.WIFI == requestDTO.getType()) {
        configFlie(params.get(KeyConstant.SSID), params.get(KeyConstant.PWD));
      } else if (RequestDTO.Type.CLOSE == requestDTO.getType()) {
        httpServer.close();
      } else {
        type = ResponseDTO.Type.OK;
        msg = "未知的请求类型";
      }
    } catch (Exception e) {
      type = ResponseDTO.Type.ERROR;
      msg = e.getMessage();
      log.error("处理异常:", e);
    }
    return ResponseDTO.builder()
      .type(type)
      .msg(msg)
      .build();
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
   * 入参解析
   *
   * @param params 入参
   * @return 返回dto参数
   */
  RequestDTO getRequestDTO(MultiMap params) {
    return (RequestDTO) GsonUtils.jsonToObject(params, RequestDTO.class);
  }

}
