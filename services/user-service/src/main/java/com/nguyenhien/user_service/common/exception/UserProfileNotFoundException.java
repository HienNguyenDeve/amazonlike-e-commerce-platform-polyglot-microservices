package com.nguyenhien.user_service.common.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserProfileNotFoundException extends RuntimeException {
  public UserProfileNotFoundException(UUID userProfileId) {
    super("User profile not found with id: " + userProfileId);
  }
}
