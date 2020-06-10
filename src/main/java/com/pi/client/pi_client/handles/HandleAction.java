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

  public HandleAction(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    wifiHandle = new WifiHandle(applicationContext);
    flowHandle = new FlowHandle(applicationContext);
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
  public ResponseDTO<String> handle(JsonObject jsonObject) {
    ResponseDTO<String> responseDTO = new ResponseDTO<>();
    responseDTO.setType(ResponseDTO.Type.OK);
    responseDTO.setMsg("");
    if (null == jsonObject) return responseDTO;
    if (jsonObject.containsKey(KeyConstant.DEVICE_NO)
      && !applicationContext.getId().equals(jsonObject.getString(KeyConstant.DEVICE_NO)))
      return responseDTO;

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
    } else {
      responseDTO.setType(ResponseDTO.Type.OK);
      responseDTO.setMsg("未知的请求类型");
    }
    return responseDTO;
  }
}
