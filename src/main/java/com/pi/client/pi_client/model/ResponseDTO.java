package com.pi.client.pi_client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应出参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
  /**
   * 类型
   *
   * @see Type
   */
  Type type;
  /**
   * 数据
   */
  Object data;
  /**
   * 消息内容
   */
  String msg;

  public enum Type {
    OK, ERROR, MQTT;
  }
}
