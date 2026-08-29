package com.nguyenhien.api_gateway.security;

import org.springframework.security.core.Authentication;

public interface JwtAuthenticationConverter {
  Authentication convert(CurrentUserPrincipal principal);
}
