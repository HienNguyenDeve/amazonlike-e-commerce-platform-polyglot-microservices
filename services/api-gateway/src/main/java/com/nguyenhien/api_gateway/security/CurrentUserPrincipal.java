package com.nguyenhien.api_gateway.security;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserPrincipal implements Serializable {
  private UUID userId;

  private String username;

  private String email;

  private List<String> roles;
}
