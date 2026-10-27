package com.nguyenhien.auth_service.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.nguyenhien.auth_service.api.request.CreateRefreshTokenRequest;
import com.nguyenhien.auth_service.api.request.LoginRequest;
import com.nguyenhien.auth_service.api.request.LogoutRequest;
import com.nguyenhien.auth_service.api.request.RefreshTokenRequest;
import com.nguyenhien.auth_service.api.request.RegisterRequest;
import com.nguyenhien.auth_service.api.response.JwtResponse;
import com.nguyenhien.auth_service.api.response.MessageResponse;
import com.nguyenhien.auth_service.application.event.message.UserRegisteredEvent;
import com.nguyenhien.auth_service.application.event.producer.AuthEventProducer;
import com.nguyenhien.auth_service.application.interfaces.IBlacklistedAccessTokenService;
import com.nguyenhien.auth_service.application.interfaces.IRefreshTokenService;
import com.nguyenhien.auth_service.application.interfaces.ITokenService;
import com.nguyenhien.auth_service.application.service.AuthService;
import com.nguyenhien.auth_service.domain.enums.UserRole;
import com.nguyenhien.auth_service.domain.model.RefreshToken;
import com.nguyenhien.auth_service.domain.repository.IRefreshTokenRepository;
import com.nguyenhien.auth_service.infrastructure.persistence.entity.UserEntity;
import com.nguyenhien.auth_service.infrastructure.persistence.repository.JpaUserRepository;
import com.nguyenhien.auth_service.infrastructure.security.UserDetailsImpl;
import com.nguyenhien.shared.exception.base.BusinessException;
import com.nguyenhien.shared.exception.model.ErrorCode;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
  // Mock
  @Mock private JpaUserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private ITokenService tokenService;

  @Mock private AuthenticationManagerBuilder authenticationManagerBuilder;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private IRefreshTokenRepository refreshTokenRepository;

  @Mock private IRefreshTokenService refreshTokenService;

  @Mock private IBlacklistedAccessTokenService blacklistedAccessTokenService;

  @Mock private AuthEventProducer authEventProducer;

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  // Inject Mocks
  @InjectMocks private AuthService authService;

  // Before each

  // Test
  // Register successfully
  @Test
  void register_shouldRegister_whenNotExistUsernameAndEmail() {
    // Arrange
    RegisterRequest request =
        RegisterRequest.builder()
            .username("NguyenHien")
            .email("hiennv@gmail.com")
            .password("abcd@1234")
            .build();

    when(userRepository.existsByUsername("NguyenHien")).thenReturn(false);
    when(userRepository.existsByEmail("hiennv@gmail.com")).thenReturn(false);
    when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
    UUID userId = UUID.randomUUID();
    when(userRepository.save(any(UserEntity.class)))
        .thenAnswer(
            invocation -> {
              UserEntity user = invocation.getArgument(0);
              user.setId(userId);
              return user;
            });

    // Act
    MessageResponse result = authService.register(request);

    // Assert
    assertNotNull(result);

    assertTrue(result.isSuccess());
    assertEquals("Regisgter Successfully", result.getMessage());
    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(userCaptor.capture());
    assertEquals(request.getUsername(), userCaptor.getValue().getUsername());
    assertEquals(request.getEmail(), userCaptor.getValue().getEmail());
    assertEquals("encoded-password", userCaptor.getValue().getPasswordHash());
    verify(passwordEncoder).encode(request.getPassword());
    ArgumentCaptor<UserRegisteredEvent> eventCaptor =
        ArgumentCaptor.forClass(UserRegisteredEvent.class);
    verify(authEventProducer).publishUserRegistered(eventCaptor.capture());
    assertEquals(userId, eventCaptor.getValue().authUserId());
    assertEquals(request.getEmail(), eventCaptor.getValue().email());
    assertNotNull(eventCaptor.getValue().eventId());
    assertNotNull(eventCaptor.getValue().occurredAt());

    verify(userRepository).existsByUsername("NguyenHien");
    verify(userRepository).existsByEmail("hiennv@gmail.com");
  }

  // Register failure when username is exist
  @Test
  void register_shouldThrowException_WhenUsernameAlreadyExist() {
    // Arrange
    RegisterRequest request =
        RegisterRequest.builder()
            .username("NguyenHien")
            .email("hiennv@gmail.com")
            .password("abcd@1234")
            .build();

    when(userRepository.existsByUsername("NguyenHien")).thenReturn(true);

    // Act
    BusinessException ex =
        assertThrows(BusinessException.class, () -> authService.register(request));

    // Assert
    assertEquals(ErrorCode.RESOURCE_ALREADY_EXISTS, ex.getErrorCode());
    verify(userRepository, never()).existsByEmail(anyString());

    verify(userRepository, never()).save(any(UserEntity.class));
    verifyNoInteractions(passwordEncoder, authEventProducer);
  }

  // Register failure when email is exist
  @Test
  void register_shouldThrowException_whenEmailAlreadyExists() {
    // Arrange
    RegisterRequest request =
        RegisterRequest.builder()
            .username("john")
            .email("john@gmail.com")
            .password("123456")
            .build();

    when(userRepository.existsByUsername("john")).thenReturn(false);
    when(userRepository.existsByEmail("john@gmail.com")).thenReturn(true);

    // Act
    BusinessException ex =
        assertThrows(BusinessException.class, () -> authService.register(request));

    // Assert
    assertEquals(ErrorCode.RESOURCE_ALREADY_EXISTS, ex.getErrorCode());

    verify(userRepository, never()).save(any(UserEntity.class));
    verifyNoInteractions(passwordEncoder, authEventProducer);
  }

  // Login succefully
  @Test
  void login_shouldReturnJwtResponse_whenCredentialsAreValid() {
    // Arrange
    UUID userId = UUID.randomUUID();

    LoginRequest request = LoginRequest.builder().username("john").password("123456").build();

    UserEntity user =
        UserEntity.builder()
            .id(userId)
            .username("john")
            .email("john@gmail.com")
            .passwordHash("encoded-password")
            .role(UserRole.USER)
            .build();

    UserDetailsImpl principal = UserDetailsImpl.build(user);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    RefreshToken refreshToken =
        RefreshToken.builder()
            .token("refresh-token")
            .userId(userId)
            .username("john")
            .role("USER")
            .expiration(2592000L)
            .build();

    when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    when(tokenService.generateToken(authentication)).thenReturn("access-token");
    when(refreshTokenService.createRefreshToken(any(CreateRefreshTokenRequest.class)))
        .thenReturn(refreshToken);

    // Act
    JwtResponse result = authService.login(request);

    // Assert
    assertNotNull(result);
    assertEquals("access-token", result.getAccessToken());
    assertEquals("refresh-token", result.getRefreshToken());
    assertEquals(userId, result.getUserInfo().getId());
    assertEquals("john", result.getUserInfo().getUsername());
    assertEquals("john@gmail.com", result.getUserInfo().getEmail());

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(tokenService).generateToken(authentication);
    verify(refreshTokenService).createRefreshToken(any(CreateRefreshTokenRequest.class));
  }

  @Test
  void refreshToken_shouldReturnNewJwtResponse_whenRefreshTokenValid() {
    // Arrange
    UUID userId = UUID.randomUUID();

    RefreshToken oldRefreshToken =
        RefreshToken.builder()
            .token("old-refresh-token")
            .userId(userId)
            .username("john")
            .role("USER")
            .expiration(2592000L)
            .build();

    RefreshToken newRefreshToken =
        RefreshToken.builder()
            .token("new-refresh-token")
            .userId(userId)
            .username("john")
            .role("USER")
            .expiration(2592000L)
            .build();

    RefreshTokenRequest request =
        RefreshTokenRequest.builder().refreshToken("old-refresh-token").build();

    when(refreshTokenRepository.findByToken("old-refresh-token"))
        .thenReturn(Optional.of(oldRefreshToken));

    when(tokenService.generateToken(any(Authentication.class))).thenReturn("new-access-token");

    when(refreshTokenService.createRefreshToken(any(CreateRefreshTokenRequest.class)))
        .thenReturn(newRefreshToken);

    // Act
    JwtResponse result = authService.refreshToken(request);

    // Assert
    assertNotNull(result);
    assertEquals("new-access-token", result.getAccessToken());
    assertEquals(userId, result.getUserInfo().getId());
    assertEquals("john", result.getUserInfo().getUsername());

    verify(refreshTokenRepository).findByToken("old-refresh-token");
    verify(tokenService).generateToken(any(Authentication.class));
    verify(refreshTokenService).createRefreshToken(any(CreateRefreshTokenRequest.class));
  }

  @Test
  void refreshToken_shouldThrowException_whenRefreshTokenInvalid() {
    // Arrange
    RefreshTokenRequest request =
        RefreshTokenRequest.builder().refreshToken("invalid-token").build();

    when(refreshTokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());

    // Act + Assert
    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> authService.refreshToken(request));

    assertEquals("Token is invalid or expired", ex.getMessage());

    verify(refreshTokenRepository).findByToken("invalid-token");
    verify(tokenService, never()).generateToken(any(Authentication.class));
    verify(refreshTokenService, never()).createRefreshToken(any(CreateRefreshTokenRequest.class));
  }

  // Revoke token
  @Test
  void revokeToken_shouldDeleteRefreshTokenAndReturnSuccessMessage() {
    // Arrange
    String token = "refresh-token";
    when(refreshTokenService.delete(token)).thenReturn(true);

    // Act
    MessageResponse result = authService.revokeToken(token, "logout");

    // Assert
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals("Token successfully revoke", result.getMessage());

    verify(refreshTokenService).delete(token);
  }

  // logout
  @Test
  void logout_shouldDeleteRefreshTokenAndBlacklistAccessToken_whenRequestNotNull() {
    // Arrange
    LogoutRequest request =
        LogoutRequest.builder().refreshToken("refresh-token").accessToken("access-token").build();

    // Act
    MessageResponse result = authService.logout(request);

    // Assert
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals("Logout successfully", result.getMessage());

    verify(refreshTokenRepository).deleteById("refresh-token");
    verify(blacklistedAccessTokenService).blacklist("access-token");
  }

  @Test
  void logout_shouldReturnSuccess_whenRequestIsNull() {
    // Act
    MessageResponse result = authService.logout(null);

    // Assert
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals("Logout successfully", result.getMessage());

    verify(refreshTokenRepository, never()).deleteById(anyString());
    verify(blacklistedAccessTokenService, never()).blacklist(anyString());
  }
}
