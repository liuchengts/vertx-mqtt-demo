package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.model.ResponseDTO;
import io.vertx.core.json.JsonObject;
import lombok.Getter;

/**
 * 默认的处理器
 */
public class HandleAction {
  @Getter
  ApplicationContext applicationContext;
  WifiHandle wifiHandle;

  public HandleAction(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    wifiHandle = new WifiHandle(applicationContext);
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   * @return 出参
   */
  public ResponseDTO handle(JsonObject jsonObject) {
    ResponseDTO responseDTO;
    if (1 == 1) {
      responseDTO = wifiHandle.handle(jsonObject);
    }
    return responseDTO;
  }
}
