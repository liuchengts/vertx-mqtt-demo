package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.model.ResponseDTO;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的处理器
 */
@Slf4j
public class HandleAction {
  @Getter
  ApplicationContext applicationContext;
  WifiHandle wifiHandle;
  V2Handle v2Handle;
  ShellHandle shellHandle;
  RinetdHandle rinetdHandle;
  PacHandle pacHandle;
  PortHandle portHandle;
  ForwardHandle forwardHandle;

  public HandleAction(ApplicationContext applicationContext) throws Exception {
    this.applicationContext = applicationContext;
    shellHandle = new ShellHandle(applicationContext);
    wifiHandle = new WifiHandle(applicationContext);
    v2Handle = new V2Handle(applicationContext);
    rinetdHandle = new RinetdHandle(applicationContext);
    pacHandle = new PacHandle(applicationContext);
    portHandle = new PortHandle(applicationContext);
    forwardHandle = new ForwardHandle(applicationContext);
  }

  public void close() {
    applicationContext.getHttpService().getHttpServer().close();
    applicationContext.getMqttService().getMqttClient().disconnect();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   * @return 出参
   */
  public ResponseDTO<Object> handle(JsonObject jsonObject) {
    ResponseDTO<Object> responseDTO = new ResponseDTO<>();
    responseDTO.setType(ResponseDTO.Type.OK);
    responseDTO.setMsg("");
    String device_no_local = applicationContext.getId();
    String device_no_msg = jsonObject.containsKey(KeyConstant.DEVICE_NO) ? jsonObject.getString(KeyConstant.DEVICE_NO) : null;
    if (null == jsonObject) {
      log.warn("没有任何内容");
      return responseDTO;
    }
    if (null != device_no_msg && !device_no_local.equals(device_no_msg)) {
      log.warn("不是自己的设备消息 device_no_local:{}  device_no_msg:{}", device_no_local, device_no_msg);
      return responseDTO;
    }
    if (!KeyConstant.OK.equals(jsonObject.getString(KeyConstant.TYPE))) {
      responseDTO.setType(ResponseDTO.Type.OK);
      responseDTO.setMsg("响应失败");
      return responseDTO;
    }
    if (KeyConstant.WIFI.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      responseDTO = wifiHandle.handle(jsonObject);
    } else if (KeyConstant.CLOSE.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      close();
    } else if (KeyConstant.V2.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      v2Handle.handle(jsonObject);
    } else if (KeyConstant.SHELL_PULL.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      shellHandle.handle(jsonObject);
    } else if (KeyConstant.RINETD.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      rinetdHandle.handle(jsonObject);
    } else if (KeyConstant.PAC_FILE.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      pacHandle.handle(jsonObject);
    } else if (KeyConstant.PORT.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      portHandle.handle(jsonObject);
    } else if (KeyConstant.FORWARD.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      forwardHandle.handle(jsonObject);
    } else {
      responseDTO.setType(ResponseDTO.Type.OK);
      responseDTO.setMsg("未知的请求类型");
    }
    return responseDTO;
  }
}
