package com.nguyenhien.api_gateway.security;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {
  public static final String BEARER = "Bearer ";

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String TOKEN_PREFIX = "Bearer ";

  public static final String USER_ID_HEADER = "X-User-Id";

  public static final String EMAIL_HEADER = "X-Email";

  public static final String USERNAME = "X-Username";

  public static final String ROLE_HEADER = "X-Roles";

  public static final List<String> PUBLIC_ENDPOINTS = List.of(
    "/api/v1/auth/login",
    "/api/v1/auth/register",
    "/api/v1/auth/refresh-token",
    "/actuator/health",
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/actuator/**"
  );
}
