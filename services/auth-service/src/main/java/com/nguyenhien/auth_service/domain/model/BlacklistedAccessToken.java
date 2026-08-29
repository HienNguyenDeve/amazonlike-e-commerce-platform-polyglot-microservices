package com.nguyenhien.auth_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("backlisted_access_token")
public class BlacklistedAccessToken {
  @Id private String token;

  @TimeToLive private Long expiration;
}
