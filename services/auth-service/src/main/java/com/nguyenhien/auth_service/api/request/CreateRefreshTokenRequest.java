package com.nguyenhien.auth_service.api.request;

import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRefreshTokenRequest {
    private UUID userId;
    private String username;
    private Set<String> roles;
}
