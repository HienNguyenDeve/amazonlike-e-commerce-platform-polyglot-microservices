package com.nguyenhien.auth_service.application.interfaces;

import com.nguyenhien.auth_service.api.request.LoginRequest;
import com.nguyenhien.auth_service.api.request.LogoutRequest;
import com.nguyenhien.auth_service.api.request.RefreshTokenRequest;
import com.nguyenhien.auth_service.api.request.RegisterRequest;
import com.nguyenhien.auth_service.api.response.JwtResponse;
import com.nguyenhien.auth_service.api.response.MessageResponse;

public interface IAuthService {
  MessageResponse register(RegisterRequest request);

  JwtResponse login(LoginRequest request);

  JwtResponse refreshToken(RefreshTokenRequest token);

  MessageResponse revokeToken(String token, String reason);

  MessageResponse logout(LogoutRequest request);
}
