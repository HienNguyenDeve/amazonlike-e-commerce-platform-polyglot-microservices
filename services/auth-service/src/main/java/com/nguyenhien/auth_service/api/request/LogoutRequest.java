package com.nguyenhien.auth_service.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutRequest {
  private String refreshToken;
  private String accessToken;
}
