package com.pi.client.pi_client.model;

public interface KeyConstant {
  String WPA_SUPPLICANT_PATH = "/etc/wpa_supplicant/wpa_supplicant.conf";
  String INTERFACES_PATH = "/etc/network/interfaces";
  String SSID = "ssid";
  String PWD = "pwd";

  String ROOT = System.getProperty("user.dir");
  String SHELL_PATH_WIFI = ROOT + "/wifi.sh";
  String SHELL_PATH_AP = ROOT + "/ap.sh";
}
