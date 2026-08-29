package com.nguyenhien.api_gateway.security;

import io.jsonwebtoken.Claims;

public interface JwtValidator {
  Claims validate(String token);
}
