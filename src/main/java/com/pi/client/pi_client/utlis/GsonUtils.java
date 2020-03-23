package com.pi.client.pi_client.utlis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class GsonUtils {
  static Gson gson = null;

  static {
    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
  }

  public static Object jsonToObject(String json, Class<?> type) {
    return gson.fromJson(json, type);
  }

  public static String objectToJson(Object obj) {
    return gson.toJson(obj);
  }

  public static String maptoJsonstr(Map map) {
    return gson.toJson(map);
  }

  public static Object jsonToObject(Map map, Class<?> type) {
    String json = maptoJsonstr(map);
    return gson.fromJson(json, type);
  }

  public static Object jsonToObject(Object obj, Class<?> type) {
    String json = objectToJson(obj);
    return gson.fromJson(json, type);
  }
}
