package com.nguyenhien.user_service.utils;

public final class JwtTestUtils {
  private JwtTestUtils() {}

  public static String userToken() {

    return "Bearer fake-user-token";
  }

  public static String adminToken() {

    return "Bearer fake-admin-token";
  }
}
