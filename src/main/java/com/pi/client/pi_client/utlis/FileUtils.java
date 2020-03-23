package com.pi.client.pi_client.utlis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class FileUtils {

  /**
   * 按行写文件（如果文件存在会先清除文件）
   *
   * @param path     文件路径
   * @param contents list的内容写入文件
   * @throws Exception
   */
  public static void outFile(String path, List<String> contents) throws Exception {
    File file = new File(path);
    if (file.isFile()) {
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write("");
      fileWriter.flush();
      fileWriter.close();
    } else {
      file.createNewFile();
    }
    BufferedWriter writer = new BufferedWriter(new FileWriter(path));
    for (String l : contents) {
      writer.write(l + "\r\n");
    }
    writer.close();
  }
}
