package com.nguyenhien.auth_service.unit.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.nguyenhien.auth_service.api.request.CreateRefreshTokenRequest;
import com.nguyenhien.auth_service.application.service.RefreshTokenService;
import com.nguyenhien.auth_service.common.exception.TokenRefreshException;
import com.nguyenhien.auth_service.domain.model.RefreshToken;
import com.nguyenhien.auth_service.domain.repository.IRefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
        // Mock
        @Mock
        private IRefreshTokenRepository refreshTokenRepository;

        // Inject Mocks
        @InjectMocks
        private RefreshTokenService refreshTokenService;

        private static final Long TTL = 2592000L;

        // Before Each
        @BeforeEach
        void setUp() {
                ReflectionTestUtils.setField(
                                refreshTokenService,
                                "refreshTokenExpiration",
                                TTL);
        }

        // Test
        @Test
        void createRefreshToken_shouldCreate_whenNoExistToken() {
                // Arrange
                UUID userId = UUID.randomUUID();
                Set<String> roles = Set.of("ROLE_USER");

                CreateRefreshTokenRequest request = CreateRefreshTokenRequest
                                .builder()
                                .userId(userId)
                                .username("Nguyen")
                                .roles(roles)
                                .build();

                when(refreshTokenRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

                when(refreshTokenRepository.save(any(RefreshToken.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                RefreshToken result = refreshTokenService.createRefreshToken(request);

                // Assert
                assertNotNull(result);
                assertNotNull(result.getToken());
                assertEquals(userId, result.getUserId());
                assertEquals("Nguyen", result.getUsername());
                assertEquals(roles, result.getRoles());
                assertEquals(TTL, result.getExpiration());

                verify(refreshTokenRepository).findAllByUserId(userId);
                verify(refreshTokenRepository, never()).deleteAll(anyList());
                verify(refreshTokenRepository).save(any(RefreshToken.class));
        }

        @Test
        void createRefreshToken_shouldDeleteOldTokens_whenExistingTokens() {
                // Arrange
                UUID userId = UUID.randomUUID();
                Set<String> roles = Set.of("ROLE_USER");

                CreateRefreshTokenRequest request = CreateRefreshTokenRequest.builder()
                                .userId(userId)
                                .username("john")
                                .roles(roles)
                                .build();

                RefreshToken old = RefreshToken.builder()
                                .token("old-token")
                                .userId(userId)
                                .username("john")
                                .roles(roles)
                                .expiration(TTL)
                                .build();

                when(refreshTokenRepository.findAllByUserId(userId))
                                .thenReturn(List.of(old));

                when(refreshTokenRepository.save(any(RefreshToken.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                RefreshToken result = refreshTokenService.createRefreshToken(request);

                // Assert
                assertNotNull(result);
                assertNotEquals("old-token", result.getToken());

                verify(refreshTokenRepository).findAllByUserId(userId);
                verify(refreshTokenRepository).deleteAll(List.of(old));
                verify(refreshTokenRepository).save(any(RefreshToken.class));
        }

        @Test
        void delete_shouldDelete_whenTokenExists() {
                // Arrange
                String token = "valid-token";

                RefreshToken entity = RefreshToken.builder()
                                .token(token)
                                .userId(UUID.randomUUID())
                                .username("john")
                                .roles(Set.of("ROLE_USER"))
                                .expiration(TTL)
                                .build();

                when(refreshTokenRepository.findByToken(token))
                                .thenReturn(Optional.of(entity));

                // Act
                refreshTokenService.delete(token);

                // Assert
                verify(refreshTokenRepository).findByToken(token);
                verify(refreshTokenRepository).deleteById(token);
        }

        @Test
        void delete_shouldThrow_whenTokenNotFound() {
                // Arrange
                String token = "invalid";

                when(refreshTokenRepository.findByToken(token))
                                .thenReturn(Optional.empty());

                // Act + Assert
                RuntimeException ex = assertThrows(
                                RuntimeException.class,
                                () -> refreshTokenService.delete(token));

                assertEquals("Token is invalid or not found", ex.getMessage());

                verify(refreshTokenRepository).findByToken(token);
                verify(refreshTokenRepository, never()).deleteById(anyString());
        }

        @Test
        void verifyExpiration_shouldReturn_whenTokenNotNull() {
                // Arrange
                RefreshToken token = RefreshToken.builder()
                                .token("ok")
                                .userId(UUID.randomUUID())
                                .username("john")
                                .roles(Set.of("ROLE_USER"))
                                .expiration(TTL)
                                .build();

                // Act
                RefreshToken result = refreshTokenService.verifyExpiration(token);

                // Assert
                assertSame(token, result);
        }

        @Test
        void verifyExpiration_shouldThrow_whenTokenNull() {
                // Act + Assert
                assertThrows(
                                TokenRefreshException.class,
                                () -> refreshTokenService.verifyExpiration(null));
        }
}
