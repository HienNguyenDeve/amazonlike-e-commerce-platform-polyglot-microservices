package com.nguyenhien.api_gateway.security;

import java.util.Arrays;

import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;

import com.nguyenhien.api_gateway.common.exceptions.InvalidTokenException;
import com.nguyenhien.api_gateway.common.exceptions.MissingTokenException;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtValidator validator;

    private final JwtTokenProvider provider;

    private final JwtAuthenticationConverter converter;

    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (isPublicEndpoint(exchange)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        Claims claims = validator.validate(token);

        CurrentUserPrincipal principal = provider.getPrincipal(claims);

        Authentication authentication = converter.convert(principal);

        ServerWebExchange mutatedExchange = mutateHeaders(exchange, principal);

        return chain.filter(mutatedExchange)
                .contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private ServerWebExchange mutateHeaders(ServerWebExchange exchange, CurrentUserPrincipal principal) {
        ServerHttpRequest request = exchange.getRequest()

                .mutate()

                .header(
                        SecurityConstants.USER_ID_HEADER,
                        principal.getUserId().toString())

                .header(
                        SecurityConstants.USERNAME,
                        principal.getUsername())

                .header(
                        SecurityConstants.EMAIL_HEADER,
                        principal.getEmail())

                .header(
                        SecurityConstants.ROLE_HEADER,
                        String.join(",", principal.getRoles()))

                .build();

        return exchange.mutate()

                .request(request)

                .build();
    }

    private String extractToken(ServerWebExchange exchange) {
        String authorizationHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(SecurityConstants.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authorizationHeader)) {

            throw new MissingTokenException();

        }

        if (!authorizationHeader.startsWith(SecurityConstants.BEARER)) {

            throw new InvalidTokenException();

        }

        return authorizationHeader.substring(
                SecurityConstants.BEARER.length());
    }

    private boolean isPublicEndpoint(ServerWebExchange exchange) {
        String requestPath = exchange.getRequest()
                .getURI()
                .getPath();

        return Arrays.stream(SecurityConstants.PUBLIC_ENDPOINTS)
                .map(PATH_PATTERN_PARSER::parse)
                .anyMatch(pattern -> pattern.matches(PathContainer.parsePath(requestPath)));
    }

}
