package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShellHandle {
  ApplicationContext applicationContext;
  Config config;

  public ShellHandle(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.config = applicationContext.getConfig();
    updateShell();
  }


  /**
   * 更新脚本
   */
  public void updateShell() {
    log.info("[脚本执行] 更新脚本");
    new Thread(() -> ShellUtils.exec(config.getPathShellRoot(), config.getShellGitPull())).start();
  }

  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
//    String data = jsonObject.getString(KeyConstant.DATA);
    try {
      updateShell();
    } catch (Exception e) {
      log.error("指令处理失败", e);
    }
  }

}
