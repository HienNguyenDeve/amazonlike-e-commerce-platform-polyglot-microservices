package com.nguyenhien.user_service.common.util;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

  public static UUID getCurrentUserId() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return (UUID) authentication.getPrincipal();
  }
}
