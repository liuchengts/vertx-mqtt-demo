package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.model.ResponseDTO;
import com.pi.client.pi_client.service.HttpService;
import com.pi.client.pi_client.service.MqttService;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class WifiHandle {
  MqttService mqttService;
  HttpService httpService;
  Vertx vertx;

  public WifiHandle(ApplicationContext applicationContext) {
    this.httpService = applicationContext.getHttpService();
    this.mqttService = applicationContext.getMqttService();
    this.vertx = applicationContext.getVertx();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   * @return 出参
   */
  public ResponseDTO handle(JsonObject jsonObject) {
    ResponseDTO responseDTO = new ResponseDTO();
    responseDTO.setType(ResponseDTO.Type.OK);
    responseDTO.setMsg("");
    try {
      configFlie(jsonObject.getString(KeyConstant.SSID), jsonObject.getString(KeyConstant.PWD));
      toMqtt();
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
  public void configFlie(String ssid, String pwd) throws Exception {
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
  public void toMqtt() {
    Map<String, Object> map = new HashMap<>();
    map.put("ip", checkNetwork());
    mqttService.publish(ResponseDTO.builder()
      .type(ResponseDTO.Type.OK)
      .serviceType(ResponseDTO.ServiceType.NET)
      .msg("入网")
      .t(map)
      .build());
  }

  /**
   * 检测网络ip
   *
   * @return 返回外网ip
   */
  public String checkNetwork() {
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
