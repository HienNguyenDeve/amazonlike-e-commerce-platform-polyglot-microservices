package com.nguyenhien.user_service.domain.models;

import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preference {
  private UUID id;

  private UserProfileEntity userProfile;

  private String language;

  private String currency;

  private boolean emailNotification;

  private boolean smsNotification;

  private boolean pushNotification;

  private Instant createdAt;

  private Instant updatedAt;
}
