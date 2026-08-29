package com.nguyenhien.user_service.api.controllers;

import com.nguyenhien.user_service.api.requests.UpdateUserProfileRequest;
import com.nguyenhien.user_service.api.responses.UserProfileResponse;
import com.nguyenhien.user_service.application.interfaces.IUserProfileService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final IUserProfileService userProfileService;

  @GetMapping("/{id}")
  public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable("id") UUID id) {

    UserProfileResponse response = userProfileService.getUserProfile(id);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserProfileResponse> updateUserProfile(
      @PathVariable("id") UUID id, @Valid @RequestBody UpdateUserProfileRequest request) {

    UserProfileResponse response = userProfileService.updateUserProfile(id, request);

    return ResponseEntity.ok(response);
  }
}
