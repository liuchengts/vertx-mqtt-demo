package com.pi.client.pi_client.commom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 流量的类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlowDTO {

  /**
   * 本条数据的唯一标记 deviceNo + writeTime（时间戳）
   */
  String id;
  /**
   * 设备编号
   */
  String deviceNo;

  /**
   * 协议
   */
  String protocol;

  /**
   * 端口
   */
  Long port;

  /**
   * 入口流量(单位字节)
   */
  Long flowPut;

  /**
   * 出口流量(单位字节)
   */
  Long flowOut;

  /**
   * 统计时间
   */
  Date writeTime;
}
