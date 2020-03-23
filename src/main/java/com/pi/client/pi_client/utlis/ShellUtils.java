package com.pi.client.pi_client.utlis;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ShellUtils {

  public static void exec(String path) throws IOException {
    Process process = Runtime.getRuntime().exec(path);
    BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = "";
    while ((line = input.readLine()) != null) {
      log.info(line);
    }
    input.close();
  }
}
