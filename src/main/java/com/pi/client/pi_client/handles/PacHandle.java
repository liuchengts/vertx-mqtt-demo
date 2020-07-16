package com.pi.client.pi_client.handles;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import com.pi.client.pi_client.model.KeyConstant;
import com.pi.client.pi_client.utlis.FileUtils;
import com.pi.client.pi_client.utlis.ShellUtils;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j
public class PacHandle {
  Config config;

  public PacHandle(ApplicationContext applicationContext) {
    this.config = applicationContext.getConfig();
  }


  /**
   * 处理器
   *
   * @param jsonObject 入参
   */
  public void handle(JsonObject jsonObject) {
    String pacConfigHome = config.getPathHome() + config.getPathPacHome();
    JsonObject dataJson = JsonObject.mapFrom(jsonObject.getString(KeyConstant.DATA));
    if (dataJson.getBoolean(KeyConstant.PAC_DEL)) {
      //删除pac文件
      FileUtils.remove(pacConfigHome + dataJson.getString(KeyConstant.PAC_NAME));
    } else {
      String pacTemplate = config.getPathShellRoot() + config.getPathPacTemplate();
      try {
        LinkedList<String> fileList = FileUtils.readFile(pacTemplate);
        if (fileList.isEmpty()) {
          log.error("读取文件失败,模板文件不存在 pacTemplate:{}", pacTemplate);
          return;
        }
        if (!config.getPacPrefix().equals(fileList.get(0))) {
          log.error("写入文件失败,模板文件被篡改 len 0 :{}", fileList.get(0));
          return;
        }
        String httpProxy = config.getNetworkIp() + ":" + dataJson.getString(KeyConstant.PAC_HTTP_PORT);
        String socksProxy = config.getNetworkIp() + ":" + dataJson.getString(KeyConstant.PAC_SOCKS_PORT);
        fileList.remove(1);
        fileList.remove(1);
        fileList.add(1, "var socks_proxy = '" + socksProxy + "';");
        fileList.add(1, "var http_proxy = '" + httpProxy + "';");
        String outFile = pacConfigHome + dataJson.getString(KeyConstant.PAC_NAME);
        FileUtils.outFile(outFile, fileList);
      } catch (Exception e) {
        log.error("读取文件失败 pacTemplate:{}", pacTemplate, e);
      }
    }
  }

}
