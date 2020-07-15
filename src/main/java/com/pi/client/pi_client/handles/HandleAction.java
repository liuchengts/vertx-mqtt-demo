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
  FlowHandle flowHandle;
  ShellHandle shellHandle;

  public HandleAction(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    wifiHandle = new WifiHandle(applicationContext);
    flowHandle = new FlowHandle(applicationContext);
    shellHandle = new ShellHandle();
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
    } else if (KeyConstant.FLOW.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      flowHandle.handle(jsonObject);
    } else if (KeyConstant.SHELL_PULL.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      shellHandle.handle(jsonObject);
    } else if (KeyConstant.RINETD.equals(jsonObject.getString(KeyConstant.SERVICE_TYPE))) {
      //todo 转发
    } else {
      responseDTO.setType(ResponseDTO.Type.OK);
      responseDTO.setMsg("未知的请求类型");
    }
    return responseDTO;
  }
}
