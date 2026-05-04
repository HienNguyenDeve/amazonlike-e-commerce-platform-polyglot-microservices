package com.nguyenhien.auth_service.unit.service;

import com.nguyenhien.auth_service.application.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    /**
     * Base64 secret key.
     * Important: HS256 needs a key with enough length.
     */
    private static final String SECRET_KEY =
            "bXktc3VwZXItc2VjcmV0LWtleS1mb3Itand0LXRlc3RpbmctMTIzNDU2Nzg5MA==";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();

        ReflectionTestUtils.setField(tokenService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(tokenService, "expireTime", 3600);
    }

    @Test
    void generateToken_shouldReturnJwtToken_whenAuthenticationIsValid() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "john",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Act
        String token = tokenService.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void getAuthentication_shouldReturnAuthentication_whenTokenIsValid() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "john",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = tokenService.generateToken(authentication);

        // Act
        Authentication result = tokenService.getAuthentication(token);

        // Assert
        assertNotNull(result);
        assertEquals("john", result.getName());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void getAuthentication_shouldReturnNull_whenTokenIsNull() {
        // Act
        Authentication result = tokenService.getAuthentication(null);

        // Assert
        assertNull(result);
    }

    @Test
    void getAuthentication_shouldReturnNull_whenTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid.jwt.token";

        // Act
        Authentication result = tokenService.getAuthentication(invalidToken);

        // Assert
        assertNull(result);
    }

    @Test
    void getAuthentication_shouldReturnAuthenticationWithMultipleRoles_whenTokenHasMultipleRoles() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                )
        );

        String token = tokenService.generateToken(authentication);

        // Act
        Authentication result = tokenService.getAuthentication(token);

        // Assert
        assertNotNull(result);
        assertEquals("admin", result.getName());

        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));

        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }
}