package com.nguyenhien.auth_service.application.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nguyenhien.auth_service.api.request.CreateRefreshTokenRequest;
import com.nguyenhien.auth_service.api.request.LoginRequest;
import com.nguyenhien.auth_service.api.request.LogoutRequest;
import com.nguyenhien.auth_service.api.request.RefreshTokenRequest;
import com.nguyenhien.auth_service.api.request.RegisterRequest;
import com.nguyenhien.auth_service.api.response.JwtResponse;
import com.nguyenhien.auth_service.api.response.MessageResponse;
import com.nguyenhien.auth_service.api.response.UserResponse;
import com.nguyenhien.auth_service.application.interfaces.IAuthService;
import com.nguyenhien.auth_service.application.interfaces.IBlacklistedAccessTokenService;
import com.nguyenhien.auth_service.application.interfaces.IRefreshTokenService;
import com.nguyenhien.auth_service.application.interfaces.ITokenService;
import com.nguyenhien.auth_service.domain.enums.UserRole;
import com.nguyenhien.auth_service.domain.model.RefreshToken;
import com.nguyenhien.auth_service.domain.repository.IRefreshTokenRepository;
import com.nguyenhien.auth_service.infrastructure.persistence.entity.UserEntity;
import com.nguyenhien.auth_service.infrastructure.persistence.repository.JpaUserRepository;
import com.nguyenhien.auth_service.infrastructure.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService, UserDetailsService {
        private final JpaUserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final ITokenService tokenService;
        private final AuthenticationManagerBuilder authenticationManager;
        private final IRefreshTokenRepository refreshTokenRepository;
        private final IRefreshTokenService refreshTokenService;
        private final IBlacklistedAccessTokenService blacklistedAccessTokenService;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // Find user by username and password
                UserEntity user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return UserDetailsImpl.build(user);
        }

        @Override
        public MessageResponse register(RegisterRequest request) {
                if (userRepository.existsByUsername(request.getUsername())) {
                        throw new RuntimeException("Username already exists");
                }

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("Email already exists");
                }

                UserEntity user = UserEntity.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .passwordHash(passwordEncoder.encode(request.getPassword()))
                                .build();
                userRepository.save(user);

                return MessageResponse.builder()
                                .message("Regisgter Successfully")
                                .success(true)
                                .build();
        }

        @Override
        public JwtResponse login(LoginRequest request) {
                try {
                        // UsernamPasswordAuthentication
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                        request.getUsername(), request.getPassword());

                        // AuthenticationManager
                        Authentication authentication = authenticationManager
                                        .getObject()
                                        .authenticate(authenticationToken);

                        // SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // Generate JWT
                        String accessToken = tokenService.generateToken(authentication);

                        // Generate Refresh token
                        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                        UserEntity user = userDetails.getUser();
                        Set<String> roleStrings = user.getRoles().stream().map(UserRole::toString).collect(Collectors.toSet());
                        CreateRefreshTokenRequest createRefreshTokenRequest = CreateRefreshTokenRequest
                                        .builder()
                                        .userId(user.getId())
                                        .username(user.getUsername())
                                        .roles(roleStrings)
                                        .build();
                        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(createRefreshTokenRequest);

                        List<String> roles = userDetails.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList();

                        UserResponse userInfor = UserResponse
                                        .builder()
                                        .id(user.getId())
                                        .username(user.getUsername())
                                        .email(user.getEmail())
                                        .build();

                        return JwtResponse.builder()
                                        .accessToken(accessToken)
                                        .refreshToken(newRefreshToken.getToken())
                                        .userInfo(userInfor)
                                        .build();
                } catch (Exception e) {
                        throw e;
                }
        }

        // refresh token
        @Override
        public JwtResponse refreshToken(RefreshTokenRequest request) {
                // Check if token is existed in Redis
                var oldRefreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                                .orElseThrow(() -> new RuntimeException("Token is invalid or expired"));

                List<SimpleGrantedAuthority> authorities = oldRefreshToken.getRoles().stream()
                                .map(SimpleGrantedAuthority::new).toList();

                User principal = new User(oldRefreshToken.getUsername(), "", authorities);
                Authentication authentication = new 
                        UsernamePasswordAuthenticationToken(principal, null, authorities);

                // Generate new access and refresh token
                String newAccessToken = tokenService.generateToken(authentication);
                CreateRefreshTokenRequest createRefreshTokenRequest = CreateRefreshTokenRequest
                                .builder()
                                .userId(oldRefreshToken.getUserId())
                                .username(oldRefreshToken.getUsername())
                                .roles(oldRefreshToken.getRoles())
                                .build();
                RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(createRefreshTokenRequest);
                UserResponse userInfo = UserResponse
                                .builder()
                                .id(oldRefreshToken.getUserId())
                                .username(oldRefreshToken.getUsername())
                                .build();

                // Return JwtResponse
                return JwtResponse
                                .builder()
                                .accessToken(newAccessToken)
                                .refreshToken(newRefreshToken.getToken())
                                .userInfo(userInfo)
                                .build();
        }

        @Override
        public MessageResponse revokeToken(String token, String reason) {
                refreshTokenService.delete(token);

                // return
                return MessageResponse
                                .builder()
                                .message("Token successfully revoke")
                                .success(true)
                                .build();
        }

        @Override
        public MessageResponse logout(LogoutRequest request) {
                if (request != null) {
                        refreshTokenRepository.deleteById(request.getRefreshToken());
                        blacklistedAccessTokenService.blacklist(request.getAccessToken());
                }
                return MessageResponse
                                .builder()
                                .message("Logout successfully")
                                .success(true)
                                .build();
        }

}
