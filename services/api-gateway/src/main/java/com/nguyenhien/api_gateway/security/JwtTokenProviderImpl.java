package com.nguyenhien.api_gateway.security;

import com.nguyenhien.api_gateway.common.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {
  private final JwtProperties jwtProperties;

  private SecretKey getSigningKey() {

    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
  }

  @Override
  public Claims parseClaims(String token) {

    return Jwts.parser()
        .verifyWith(getSigningKey())
        .requireIssuer(jwtProperties.getIssuer())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  @Override
  public CurrentUserPrincipal getPrincipal(Claims claims) {

    return CurrentUserPrincipal.builder()
        .userId(extractUserId(claims))
        .username(extractUsername(claims))
        .email(extractEmail(claims))
        .roles(extractRoles(claims))
        .build();
  }

  /** userId claim */
  private UUID extractUserId(Claims claims) {

    String userId = claims.get("userId", String.class);

    if (!StringUtils.hasText(userId)) {
      throw new InvalidTokenException("Missing claim: userId");
    }

    return UUID.fromString(userId);
  }

  /** JWT Subject */
  private String extractUsername(Claims claims) {

    String username = claims.getSubject();

    if (!StringUtils.hasText(username)) {
      throw new InvalidTokenException("Missing JWT subject");
    }

    return username;
  }

  /** email claim */
  private String extractEmail(Claims claims) {

    String email = claims.get("email", String.class);

    if (!StringUtils.hasText(email)) {
      throw new InvalidTokenException("Missing claim: email");
    }

    return email;
  }

  /** roles claim */
  @SuppressWarnings("unchecked")
  private List<String> extractRoles(Claims claims) {

    Object value = claims.get("roles");

    if (value == null) {
      return Collections.emptyList();
    }

    if (!(value instanceof List<?> roles)) {
      throw new InvalidTokenException("Invalid roles claim");
    }

    return roles.stream().map(Object::toString).toList();
  }
}
