package com.nguyenhien.auth_service.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("backlisted_access_token")
public class BlacklistedAccessToken {
    @Id
    private String token;

    @TimeToLive
    private Long expiration;
}
