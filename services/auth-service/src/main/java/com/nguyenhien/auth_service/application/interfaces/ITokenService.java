package com.nguyenhien.auth_service.application.interfaces;

import org.springframework.security.core.Authentication;

public interface ITokenService {
  String generateToken(Authentication authentication);

  Authentication getAuthentication(String token);

  Long getRemainingExpiration(String token);
}
