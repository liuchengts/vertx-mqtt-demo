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

  public HandleAction(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    wifiHandle = new WifiHandle(applicationContext);
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
  public ResponseDTO handle(JsonObject jsonObject) {
    ResponseDTO responseDTO = new ResponseDTO();
    responseDTO.setType(ResponseDTO.Type.OK);
    responseDTO.setMsg("");
    if (null == jsonObject) return responseDTO;
    log.info("jsonObject:{}", jsonObject.toString());
    if (KeyConstant.WIFI.equals(jsonObject.getString(KeyConstant.TYPE))) {
      responseDTO = wifiHandle.handle(jsonObject);
    } else if (KeyConstant.CLOSE.equals(jsonObject.getString(KeyConstant.TYPE))) {
      close();
    } else {
      responseDTO.setType(ResponseDTO.Type.OK);
      responseDTO.setMsg("未知的请求类型");
    }
    return responseDTO;
  }
}
