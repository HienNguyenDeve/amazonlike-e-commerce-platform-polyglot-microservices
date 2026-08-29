package com.nguyenhien.auth_service.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevokeTokenRequest {
  @NotBlank(message = "Token is required")
  private String token;

  private String reason;
}
