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
//  public static void main(String[] args) {
//    exec("flow/test.sh");
//    exec("/Users/liucheng/it/lc/pi-client/src/main/resources/flow/test.sh");
//  }

  public static String getPathTmp(String shellPath) {
    return Thread.class.getResource("/" + shellPath).getPath();
  }

  /**
   * 执行shell并且打印执行结果
   *
   * @param path 要执行的脚本路径
   * @return 按行返回执行结果
   * @throws IOException
   */
  public static List<String> exec(String path, String... args) {
    if (!File.separator.equals(path.substring(0, 1))) {
      path = getPathTmp(path);
//      tmpShellCopy(path);
    }
    List<String> list = new ArrayList<>();
    BufferedReader input = null;
    try {
      String cmd = "sh " + path + " " + args;
      String[] envp = new String[]{"/usr/bin/env bash", "/usr/bin/env sh"};
      File dir = new File("/usr/bin");
      Process process = Runtime.getRuntime().exec(cmd, envp, dir);
      int status = process.waitFor();
      if (status != 0) log.error("Failed to call shell's command =>> status:{}", status);
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

  /**
   * 将本地jar中的shell文件 复制一份到liunx文件目录下再执行
   *
   * @param tmpShellPath 本地jar中的shell文件路径
   * @return 复制后的文件路径
   */
  static String tmpShellCopy(String tmpShellPath) {
    //本地jar包含的文件
    String name = tmpShellPath.substring(tmpShellPath.lastIndexOf(File.separator) + 1);
    File tmpFile = new File("/tmp/shell/" + name);
    //将要执行的本地文件
    try {
      FileUtils.outFile(tmpFile.getPath(), FileUtils.readFile(getPathTmp(tmpShellPath)));
    } catch (Exception e) {
      throw new RuntimeException("shell文件读取转换异常", e);
    }
    return tmpFile.getPath();
  }
}
