package com.pi.client.pi_client.utlis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

  /**
   * 按行写文件（如果文件存在会先清除文件）
   *
   * @param path     文件路径
   * @param contents list的内容写入文件
   * @throws Exception
   */
  public static void outFile(String path, List<String> contents) throws Exception {
    mkdir(path);
    File file = new File(path);
    if (file.isFile()) {
      try (FileWriter fileWriter = new FileWriter(file)) {
        fileWriter.write("");
      }
    } else {
      file.createNewFile();
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      for (String l : contents) {
        writer.write(l + "\r\n");
      }
    }
  }

  /**
   * 写文件（如果文件存在会先清除文件）
   *
   * @param path    文件路径
   * @param content 内容写入文件
   * @throws Exception
   */
  public static void outFile(String path, String content) throws Exception {
    mkdir(path);
    File file = new File(path);
    if (file.isFile()) {
      try (FileWriter fileWriter = new FileWriter(file)) {
        fileWriter.write("");
      }
    } else {
      file.createNewFile();
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      writer.write(content);
    }
  }

  /**
   * 循环创建父文件夹，以及空文件
   *
   * @param path 文件绝对路径
   */
  static void mkdir(String path) {
    String name = path.substring(path.lastIndexOf(File.separator) + 1);
    String mkdir = path.substring(0, path.length() - name.length());
    new File(mkdir).mkdirs();
    File file = new File(path);
    try {
      if (!file.isDirectory()) file.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
