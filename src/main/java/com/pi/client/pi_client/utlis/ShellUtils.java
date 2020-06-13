package com.pi.client.pi_client.utlis;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShellUtils {

  public static String getPathTmp(String shellPath) {
    return Thread.class.getResource("/") + shellPath;
  }

  /**
   * 执行shell并且打印执行结果
   *
   * @param path 要执行的脚本路径
   * @return 按行返回执行结果
   * @throws IOException
   */
  public static List<String> exec(String path, String... args) {
    if (!File.separator.equals(path.substring(0, 1))) path = getPathTmp(path);
    List<String> list = new ArrayList<>();
    BufferedReader input = null;
    try {
      String cmd = "sh " + path + " " + args;
      Process process = Runtime.getRuntime().exec(cmd);
      input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = "";
      while ((line = input.readLine()) != null) {
        log.info(line);
        list.add(line);
      }
    } catch (Exception e) {
      log.error("run [" + path + " ]shell error", e);
    } finally {
      if (null != input) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return list;
  }
}
