package com.pi.client.pi_client;


public class Config {
  /**
   * 启动指定的参数值
   */
  private String argsKey = "deviceNo";
  /**
   * 运行环境 dev为true表示本地环境
   */
  private Boolean dev;
  /**
   * 可执行脚本的根目录
   */
  private String pathShellRoot = "/root/shell-deployment/";
  /**
   * 临时文件目录
   */
  private String pathHome = "/home/";

  /**
   * 流量文件名
   */
  private String flowFile = "flow.txt";
  /**
   * 增加端口统计流量的相对脚本
   */
  private String shellFlowAddPort = "flow/addPort.sh";

  /**
   * 统计流量的相对脚本
   */
  private String shellFlowCheck = "flow/check.sh";

  /**
   * 清除统计流量的相对脚本
   */
  private String shellFlowClear = "flow/clear.sh";

  /**
   * 更新shell项目的相对脚本
   */
  private String shellGitPull = "git-pull.sh";

  /**
   * 更新shell项目的相对脚本
   */
  private String pathV2rayConfig = "/etc/v2ray/config.json";

  /**
   * 重启v2ray的相对脚本
   */
  private String shellV2ray = "flow/v2ray.sh";

  /**
   * v2ray临时配置文件的后缀
   */
  private String v2rayConfigTmp = ".tmp";
  /**
   * mqtt地址
   */
  private String mqttIp = "mqtt.ayouran.com";
  /**
   * mqtt端口
   */
  private Integer mqttPort = 1883;
  /**
   * mqtt订阅的主题
   */
  private String mqttSubscribe = "lot-admin";
  /**
   * mqtt发送消息的主题
   */
  private String mqttPublish = "lot-pi";
  /**
   * kafka地址
   */
  private String kafkaIpAndPort = "kafka.ayouran.com:9092";
  /**
   * kafka订阅的主题
   */
  private String kafkaSubscribe = "lot-admin";
  /**
   * kafka发送消息的主题
   */
  private String kafkaPublish = "lot-pi";
  /**
   * http服务端口
   */
  private Integer httpServerPort = 8080;
  /**
   * PAC文件的第一行
   */
  private String pacPrefix = "//pac";

  public Boolean getDev() {
    return dev;
  }

  public String getPathShellRoot() {
    return pathShellRoot;
  }

  public String getPathHome() {
    return pathHome;
  }

  public String getFlowFile() {
    return flowFile;
  }

  public String getShellFlowAddPort() {
    return shellFlowAddPort;
  }

  public String getShellFlowCheck() {
    return shellFlowCheck;
  }

  public String getShellFlowClear() {
    return shellFlowClear;
  }

  public String getShellGitPull() {
    return shellGitPull;
  }

  public String getPathV2rayConfig() {
    return pathV2rayConfig;
  }

  public String getShellV2ray() {
    return shellV2ray;
  }

  public String getV2rayConfigTmp() {
    return v2rayConfigTmp;
  }

  public String getMqttIp() {
    return mqttIp;
  }

  public Integer getMqttPort() {
    return mqttPort;
  }

  public String getMqttSubscribe() {
    return mqttSubscribe;
  }

  public String getMqttPublish() {
    return mqttPublish;
  }

  public String getKafkaIpAndPort() {
    return kafkaIpAndPort;
  }

  public String getKafkaSubscribe() {
    return kafkaSubscribe;
  }

  public String getKafkaPublish() {
    return kafkaPublish;
  }

  public Integer getHttpServerPort() {
    return httpServerPort;
  }

  public String getPacPrefix() {
    return pacPrefix;
  }

  public String getArgsKey() {
    return argsKey;
  }

  protected void setDev(Boolean dev) {
    this.dev = dev;
  }

  protected void setPathShellRoot(String pathShellRoot) {
    this.pathShellRoot = pathShellRoot;
  }

  protected void setPathHome(String pathHome) {
    this.pathHome = pathHome;
  }

  protected void setPathV2rayConfig(String pathV2rayConfig) {
    this.pathV2rayConfig = pathV2rayConfig;
  }

  protected Config() {
  }

  protected Config(Boolean dev) {
    this.dev = dev;
  }
}
