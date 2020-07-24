package com.pi.client.pi_client.commom;

import lombok.Data;

/**
 * 流量转发的类
 */
@Data
public class ForwardDTO {

  /**
   * 端口
   */
  Long portA;

  /**
   * 入口流量(单位字节)
   */
  Long portB;

  /**
   * 操作类型 add 、del
   */
  String forward;

}
