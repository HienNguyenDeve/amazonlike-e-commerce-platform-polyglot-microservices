package com.nguyenhien.user_service.api.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Update user preferences request")
public class UpdatePreferenceRequest {

  @Schema(description = "Language code", example = "vi")
  @NotBlank
  private String language;

  @Schema(description = "Currency code", example = "VND")
  @NotBlank
  private String currency;

  @Schema(description = "Enable email notifications", example = "true")
  private boolean emailNotification;

  @Schema(description = "Enable SMS notifications", example = "false")
  private boolean smsNotification;

  @Schema(description = "Enable push notifications", example = "true")
  private boolean pushNotification;
}
