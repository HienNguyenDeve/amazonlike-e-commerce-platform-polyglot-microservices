package com.nguyenhien.api_gateway.security;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverterImpl implements JwtAuthenticationConverter {

  @Override
  public Authentication convert(CurrentUserPrincipal principal) {
    Collection<SimpleGrantedAuthority> authorities =
        principal.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .toList();

    return new UsernamePasswordAuthenticationToken(principal, null, authorities);
  }
}
