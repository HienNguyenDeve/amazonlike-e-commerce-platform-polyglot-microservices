package com.nguyenhien.auth_service.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.nguyenhien.auth_service.application.interfaces.ITokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService implements ITokenService {

  @Value("${app.security.secretKey}")
  private String secretKey;

  @Value("${app.security.accessTokenExpiration}")
  private Integer expireTime;

  @Override
  public String generateToken(Authentication authentication) {
    String roles =
        authentication.getAuthorities().stream()
            .map(Object::toString)
            .collect(Collectors.joining(","));
    return generateAccessToken(authentication.getName(), roles);
  }

  private String generateAccessToken(String name, String roles) {
    LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expireTime); // Now + 3600s from
    // application.properties => expiredAt =
    // Now + 1h
    SecretKey key =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); // Decode secretKey from Base64 to
    // SecretKey
    Date expiriation =
        Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()); // Convert LocalDateTime to
    // Date
    // Generate JWT token
    return Jwts.builder()
        .subject(name)
        .claim("roles", roles)
        .expiration(expiriation)
        .signWith(key)
        .compact();
  }

  @Override
  public Authentication getAuthentication(String token) {
    if (token == null) {
      return null;
    }

    SecretKey key =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); // Decode secretKey from Base64 to
    // SecretKey
    try {
      Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

      String roles = claims.get("roles").toString();

      Set<GrantedAuthority> authorities =
          Set.of(roles.split(",")).stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toSet());

      User priciple = new User(claims.getSubject(), "", authorities);

      return new UsernamePasswordAuthenticationToken(priciple, token, authorities);
    } catch (JwtException e) {
      return null;
    }
  }

  @Override
  public Long getRemainingExpiration(String token) {
    try {
      Claims claims =
          Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

      Date expiration = claims.getExpiration();

      long remainingMillis = expiration.getTime() - System.currentTimeMillis();

      return Math.max(remainingMillis / 1000, 0);

    } catch (IllegalArgumentException e) {
      return 0L;
    }
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
