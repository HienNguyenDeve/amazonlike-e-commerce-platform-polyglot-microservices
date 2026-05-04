package com.nguyenhien.auth_service.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nguyenhien.auth_service.api.request.CreateRefreshTokenRequest;
import com.nguyenhien.auth_service.application.interfaces.IRefreshTokenService;
import com.nguyenhien.auth_service.common.exception.TokenRefreshException;
import com.nguyenhien.auth_service.domain.model.RefreshToken;
import com.nguyenhien.auth_service.domain.repository.IRefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService implements IRefreshTokenService {
    private final IRefreshTokenRepository refreshTokenRepository;

    @Value("${app.security.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    @Override
    public RefreshToken createRefreshToken(CreateRefreshTokenRequest request) {
        // Check hiện tại userId có đang có RefreshToken nào đang tồn tại không
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(request.getUserId());
        if (!tokens.isEmpty()) {
            refreshTokenRepository.deleteAll(tokens);
        }

        String newRefreshTokenStr = UUID.randomUUID().toString();
        RefreshToken newRefreshToken = RefreshToken
                .builder()
                .token(newRefreshTokenStr)
                .userId(request.getUserId())
                .username(request.getUsername())
                .roles(request.getRoles())
                .expiration(refreshTokenExpiration)
                .build();
        refreshTokenRepository.save(newRefreshToken);
        return newRefreshToken;
    }

    @Override
    public void delete(String token) {
        // Find token
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token is invalid or not found"));

        // if token found
        refreshTokenRepository.deleteById(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {
            throw new TokenRefreshException(null, "Refresh token was expired. Please sign in again.");
        }

        return token;
    }

}
