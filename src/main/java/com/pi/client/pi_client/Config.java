package com.pi.client.pi_client;


public class Config {
  /**
   * 外网ip
   */
  private String networkIp;
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
   * pathHome
   */
  private String flowFile = "flow.txt";
  /**
   * 增加端口统计流量的相对脚本
   * pathShellRoot
   */
  private String shellFlowAddPort = "flow/addPort.sh";
  /**
   * 删除端口统计流量的相对脚本
   * pathShellRoot
   */
  private String shellFlowDelPort = "flow/delPort.sh";
  /**
   * 统计流量的相对脚本
   * pathShellRoot
   */
  private String shellFlowCheck = "flow/check.sh";

  /**
   * 清除统计流量的相对脚本
   * pathShellRoot
   */
  private String shellFlowClear = "flow/clear.sh";

  /**
   * 更新shell项目的相对脚本
   * pathShellRoot
   */
  private String shellGitPull = "git-pull.sh";

  /**
   * 更新shell项目的相对脚本
   */
  private String pathV2rayConfig = "/etc/v2ray/config.json";

  /**
   * 重启v2ray的相对脚本
   * pathShellRoot
   */
  private String shellV2ray = "flow/v2ray.sh";

  /**
   * 临时配置文件的后缀
   */
  private String configTmp = ".tmp";
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
  /**
   * pac 临时文件目录
   * pathHome
   */
  private String pathPacHome = "pac/";
  /**
   * rinetd的配置文件路径
   */
  private String pathRinetdConfig = "/etc/rinetd/rinetd.conf";

  /**
   * rinetd的配置文件路径
   * pathShellRoot
   */
  private String shellRinetd = "rinetd/rinetd.sh";

  /**
   * pac模板文件路径
   * pathShellRoot
   */
  private String pathPacTemplate = "pac/template.pac";

  /**
   * 获取外网ip的相对脚本
   * pathShellRoot
   */
  private String shellIp = "ip.sh";

  public String getPathPacHome() {
    return pathPacHome;
  }

  public String getShellIp() {
    return shellIp;
  }

  public String getPathPacTemplate() {
    return pathPacTemplate;
  }

  public String getShellRinetd() {
    return shellRinetd;
  }

  public String getPathRinetdConfig() {
    return pathRinetdConfig;
  }

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

  public String getConfigTmp() {
    return configTmp;
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

  public String getNetworkIp() {
    return networkIp;
  }

  public String getShellFlowDelPort() {
    return shellFlowDelPort;
  }

  protected void setPathShellRoot(String pathShellRoot) {
    this.pathShellRoot = pathShellRoot;
  }

  protected void setPathHome(String pathHome) {
    this.pathHome = pathHome;
  }

  public void setNetworkIp(String networkIp) {
    this.networkIp = networkIp;
  }

  public void setMqttIp(String mqttIp) {
    this.mqttIp = mqttIp;
  }

  public void setPathV2rayConfig(String pathV2rayConfig) {
    this.pathV2rayConfig = pathV2rayConfig;
  }

  public void setPathRinetdConfig(String pathRinetdConfig) {
    this.pathRinetdConfig = pathRinetdConfig;
  }

  protected Config() {
  }

  protected Config(Boolean dev) {
    this.dev = dev;
  }
}
