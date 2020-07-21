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
public class ResponseDTO<T> {
  /**
   * 类型
   *
   * @see Type
   */
  Type type;
  /**
   * 业务类型
   *
   * @see Type
   */
  ServiceType serviceType;
  /**
   * 数据
   */
  T t;
  /**
   * 消息
   */
  String msg;

  public enum Type {
    OK, ERROR
  }

  public enum ServiceType {
    NET, FLOW, HEARTBEAT, PORT
  }
}
