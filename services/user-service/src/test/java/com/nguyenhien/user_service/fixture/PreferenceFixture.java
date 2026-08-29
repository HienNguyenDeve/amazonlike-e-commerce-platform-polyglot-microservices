package com.nguyenhien.user_service.fixture;

import com.nguyenhien.user_service.builder.PreferenceBuilder;
import com.nguyenhien.user_service.infrastructure.persistences.entity.PreferenceEntity;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;

public final class PreferenceFixture {
  private PreferenceFixture() {}

  public static PreferenceEntity defaultPreference(UserProfileEntity user) {

    return PreferenceBuilder.preference().user(user).build();
  }

  public static PreferenceEntity vietnamese(UserProfileEntity user) {

    return PreferenceBuilder.preference().user(user).language("vi").currency("VND").build();
  }

  public static PreferenceEntity english(UserProfileEntity user) {

    return PreferenceBuilder.preference().user(user).language("en").currency("USD").build();
  }
}
