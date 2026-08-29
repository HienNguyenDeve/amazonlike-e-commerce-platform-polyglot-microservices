package com.nguyenhien.auth_service.domain.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@RedisHash("refresh_tokens")
public class RefreshToken {
  @Id private String token; // Chuỗi Token ngẫu nhiên (Opaque Token)

  @Indexed private UUID userId; // Để tìm kiếm token theo User

  private String username;

  private String role;

  @TimeToLive private Long expiration; // Thời gian sống tính bằng giây (VD: 2592000 cho 30 ngày)
}
