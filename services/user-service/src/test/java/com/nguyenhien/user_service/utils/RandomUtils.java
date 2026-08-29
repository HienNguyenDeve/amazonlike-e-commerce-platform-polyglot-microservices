package com.nguyenhien.user_service.utils;

import java.util.UUID;

public final class RandomUtils {
  private RandomUtils() {}

  public static UUID uuid() {
    return UUID.randomUUID();
  }

  public static String email() {

    return "user-" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
  }

  public static String phone() {

    return "09" + System.currentTimeMillis() % 100000000;
  }

  public static String string(int length) {

    return UUID.randomUUID().toString().replace("-", "").substring(0, length);
  }
}
