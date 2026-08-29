package com.nguyenhien.user_service.api.controllers;

import com.nguyenhien.user_service.api.requests.UpdatePreferenceRequest;
import com.nguyenhien.user_service.api.responses.PreferenceResponse;
import com.nguyenhien.user_service.application.services.PreferenceService;
import com.nguyenhien.user_service.common.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me/preferences")
@RequiredArgsConstructor
@Tag(name = "User Preferences", description = "Manage current user preferences")
public class PreferenceController {

  private final PreferenceService preferenceService;

  @Operation(
      summary = "Get current user preferences",
      description = "Retrieve preference settings of the authenticated user")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Preferences retrieved successfully"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping
  public ResponseEntity<PreferenceResponse> getMyPreferences() {

    UUID userId = SecurityUtils.getCurrentUserId();

    PreferenceResponse response = preferenceService.getMyPreferences(userId);

    return ResponseEntity.ok().body(response);
  }

  @Operation(
      summary = "Update current user preferences",
      description = "Update preference settings of the authenticated user")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Preferences updated successfully"),
    @ApiResponse(responseCode = "400", description = "Validation failed"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PutMapping
  public ResponseEntity<PreferenceResponse> updatePreferences(
      @Valid @RequestBody UpdatePreferenceRequest request) {

    UUID userId = SecurityUtils.getCurrentUserId();

    PreferenceResponse response = preferenceService.updatePreferences(userId, request);

    return ResponseEntity.ok().body(response);
  }
}
