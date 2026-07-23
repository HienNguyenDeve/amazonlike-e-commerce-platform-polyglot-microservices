package com.nguyenhien.api_gateway.security;

import io.jsonwebtoken.Claims;

public interface JwtTokenProvider {
    Claims parseClaims(String token);

    CurrentUserPrincipal getPrincipal(Claims token);
}
