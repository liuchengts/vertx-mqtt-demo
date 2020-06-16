package com.pi.client.pi_client.model;

public interface KeyConstant {
  /**
   * 配置文件路径
   */
  String WPA_SUPPLICANT_PATH = "/etc/wpa_supplicant/wpa_supplicant.conf";
  String INTERFACES_PATH = "/etc/network/interfaces";
  /**
   * 脚本执行路径
   */
  String ROOT = System.getProperty("user.dir");
  String SHELL_PATH_WIFI = ROOT + "/wifi.sh";
  String SHELL_PATH_AP = ROOT + "/ap.sh";
  /**
   * 网络请求路径
   */
  String CHECK_NETWORK_URL = "https://httpbin.org/ip";
  /**
   * keys
   */
  String IP_ORIGIN = "origin";
  String SSID = "ssid";
  String PWD = "pwd";
  String WIFI = "wifi";
  String CLOSE = "close";
  String TYPE = "type";
  String DATA = "t";
  String SERVICE_TYPE = "serviceType";
  String DEVICE_NO = "deviceNo";
  // Type
  String OK = "OK";
  String ERROR = "ERROR";
  // ServiceType
  String NET = "NET"; // 网络相关
  String FLOW = "FLOW"; //流量相关
  String SHELL_PULL = "SHELL_PULL"; //更新脚本
}
