package com.pi.client.pi_client.commom;


public class Config {
  Boolean dev;
  /**
   * 流量处理的v2配置文件
   */
  String flowHandleConfigPath;
  /**
   * 流量处理的v2临时写入的文件名
   */
  String flowHandleConfigPathTmpName;

  protected Boolean getDev() {
    return dev;
  }

  protected void setDev(Boolean dev) {
    this.dev = dev;
  }

  protected String getFlowHandleConfigPath() {
    if (dev) flowHandleConfigPath = "/Users/liucheng/it/config.json";
    else flowHandleConfigPath = "/etc/v2ary/config.json";
    return flowHandleConfigPath;
  }

  public String getFlowHandleConfigPathTmpName() {
    return ".tmp";
  }

}
