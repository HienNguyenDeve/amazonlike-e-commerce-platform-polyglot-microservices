package com.nguyenhien.auth_service.config;

import com.nguyenhien.auth_service.application.interfaces.IBlacklistedAccessTokenService;
import com.nguyenhien.auth_service.application.interfaces.ITokenService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  @Value("${app.frontend.url}")
  private String frontendUrl;

  private final ITokenService tokenService;
  private final IBlacklistedAccessTokenService blacklistedAccessTokenService;

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    // Allow specific origin: clientUrl
    config.setAllowedOrigins(Collections.singletonList(frontendUrl));
    // Allow all headers: Authorization, Content-Type, ...
    config.addAllowedHeader("*");
    // Allow all methods: GET, POST, PUT, DELETE,...
    config.addAllowedMethod("*");

    // Apply the configuration to all paths
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Disable CSRF
    http.csrf(csrf -> csrf.disable())
        // Add CORS filter
        .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
        // Add Jwt filter
        .addFilterBefore(
            new JwtFilter(tokenService, blacklistedAccessTokenService),
            UsernamePasswordAuthenticationFilter.class)
        // Filter requests
        .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/v1/auth/**")
                    .permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
