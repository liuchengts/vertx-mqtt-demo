package com.pi.client.pi_client.utlis;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShellUtils {

  /**
   * 执行shell并且打印执行结果
   *
   * @param path 要执行的脚本路径
   * @return 按行返回执行结果
   * @throws IOException
   */
  public static List<String> exec(String path) {
    List<String> list = new ArrayList<>();
    BufferedReader input = null;
    try {
      Process process = Runtime.getRuntime().exec(path);
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
