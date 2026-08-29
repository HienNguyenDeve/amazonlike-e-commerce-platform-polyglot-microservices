package com.nguyenhien.user_service.builder;

import com.nguyenhien.user_service.infrastructure.persistences.entity.PreferenceEntity;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import java.time.Instant;
import java.util.UUID;

public final class PreferenceBuilder {
  private final PreferenceEntity entity;

  private PreferenceBuilder() {

    entity =
        PreferenceEntity.builder()
            .id(UUID.randomUUID())
            .language("vi")
            .currency("VND")
            .emailNotification(true)
            .smsNotification(false)
            .pushNotification(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
  }

  public static PreferenceBuilder preference() {
    return new PreferenceBuilder();
  }

  public PreferenceBuilder user(UserProfileEntity user) {
    entity.setUserProfile(user);
    return this;
  }

  public PreferenceBuilder language(String language) {
    entity.setLanguage(language);
    return this;
  }

  public PreferenceBuilder currency(String currency) {
    entity.setCurrency(currency);
    return this;
  }

  public PreferenceBuilder emailNotification(boolean value) {
    entity.setEmailNotification(value);
    return this;
  }

  public PreferenceBuilder smsNotification(boolean value) {
    entity.setSmsNotification(value);
    return this;
  }

  public PreferenceBuilder pushNotification(boolean value) {
    entity.setPushNotification(value);
    return this;
  }

  public PreferenceEntity build() {
    return entity;
  }
}
