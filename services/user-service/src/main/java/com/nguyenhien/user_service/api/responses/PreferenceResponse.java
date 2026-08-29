package com.nguyenhien.user_service.api.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User preferences response")
public class PreferenceResponse {

  private UUID id;

  private String language;

  private String currency;

  private boolean emailNotification;

  private boolean smsNotification;

  private boolean pushNotification;
}
