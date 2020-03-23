package com.pi.client.pi_client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
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

  public enum Type {
    //配置wifi、关闭当前程序
    WIFI, CLONE;
  }
}
