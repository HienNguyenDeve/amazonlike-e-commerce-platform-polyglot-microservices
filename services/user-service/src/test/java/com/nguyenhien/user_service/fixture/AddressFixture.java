package com.nguyenhien.user_service.fixture;

import com.nguyenhien.user_service.builder.AddressBuilder;
import com.nguyenhien.user_service.infrastructure.persistences.entity.AddressEntity;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;

public final class AddressFixture {
  private AddressFixture() {}

  public static AddressEntity defaultAddress(UserProfileEntity user) {

    return AddressBuilder.anAddress().user(user).defaultAddress().build();
  }

  public static AddressEntity homeAddress(UserProfileEntity user) {

    return AddressBuilder.anAddress().user(user).home().build();
  }

  public static AddressEntity officeAddress(UserProfileEntity user) {

    return AddressBuilder.anAddress().user(user).office().notDefault().build();
  }
}
