package com.nguyenhien.auth_service.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenhien.auth_service.api.request.LoginRequest;
import com.nguyenhien.auth_service.api.request.LogoutRequest;
import com.nguyenhien.auth_service.api.request.RefreshTokenRequest;
import com.nguyenhien.auth_service.api.request.RegisterRequest;
import com.nguyenhien.auth_service.api.request.RevokeTokenRequest;
import com.nguyenhien.auth_service.api.response.JwtResponse;
import com.nguyenhien.auth_service.api.response.MessageResponse;
import com.nguyenhien.auth_service.application.interfaces.IAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    // @GetMapping("/health")
    // public String health() {
    //     return "auth-service ok";
    // }

    // Register
    @PostMapping("/register")
    @Operation(summary = "Sign up an acount", description = "Register API", responses = {
            @ApiResponse(responseCode = "201", description = "Register successfully"),
            @ApiResponse(responseCode = "401", description = "Register failure")
    })
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        var userMasterDTO = authService.register(dto);
        return ResponseEntity.ok(userMasterDTO);
    }

    // Login
    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "User authenticate and return JWT", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    }

    )
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequestDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        var result = authService.login(loginRequestDTO);
        return ResponseEntity.ok(result);
    }

    // Refresh Token
    @PostMapping("/refresh-token")
    @Operation(
        summary="Refresh Token",
        description="Refresh token to get new accesstoken and new refresh token",
        responses={
            @ApiResponse(
                responseCode="200",
                description="Successfully fresh token",
                content=@Content(schema=@Schema(implementation=JwtResponse.class))
            ),
            @ApiResponse(
                responseCode="403",
                description="Invalid refresh token"
            )
        }
    )
    public ResponseEntity<JwtResponse> refreshToken(
        @RequestBody RefreshTokenRequest refreshTokenRequestDTO, 
        HttpServletRequest httpServletRequest) {
        var result = authService.refreshToken(refreshTokenRequestDTO);
        return ResponseEntity.ok(result);
    }

    // Revoke Token
    @PostMapping("revoke-token")
    @Operation(
        summary="Revoke token",
        description="Revoke refresh token when user logout, or have issue",
        responses = {
            @ApiResponse(
                responseCode="200",
                description="Successfully revoke refresh token",
                content=@Content(schema=@Schema(implementation=MessageResponse.class))
            ),
            @ApiResponse(
                responseCode="400",
                description="Invalid or token notfound"
            )
        }
    )
    public ResponseEntity<MessageResponse> revokeToken(
                @RequestBody RevokeTokenRequest revokeTokenRequestDTO,
                HttpServletRequest httpServletRequest) {
        var result = authService.revokeToken(revokeTokenRequestDTO.getToken(), 
                                        revokeTokenRequestDTO.getReason());
        return ResponseEntity.ok(result);
    }

    // Logout
    @PostMapping("/logout")
    @Operation(
        summary = "Logout user",
        description = "Invalidate refresh token for user",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully log out",
                content = @Content(schema = @Schema(implementation = MessageResponse.class))
            )
        }
    )
    public ResponseEntity<MessageResponse> logout (
                @RequestBody LogoutRequest logoutRequestDTO, 
                HttpServletRequest request) {
        var result = authService.logout(logoutRequestDTO);
        return ResponseEntity.ok(result);
    }
}
