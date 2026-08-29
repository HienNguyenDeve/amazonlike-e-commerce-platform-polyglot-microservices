package com.nguyenhien.auth_service.config;

import com.nguyenhien.auth_service.application.interfaces.IBlacklistedAccessTokenService;
import com.nguyenhien.auth_service.application.interfaces.ITokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
  private final ITokenService tokenService;
  private final IBlacklistedAccessTokenService blacklistedAccessTokenService;

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String bearerToken = httpServletRequest.getHeader("Authorization");
    String jwtToken = null;

    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      jwtToken = bearerToken.substring(7);
    }

    if (jwtToken != null && blacklistedAccessTokenService.isBlacklisted(jwtToken)) {
      SecurityContextHolder.clearContext();
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    Authentication authentication = tokenService.getAuthentication(jwtToken);

    if (authentication != null) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }
}
