package com.nguyenhien.user_service.builder;

import com.nguyenhien.user_service.domain.enums.Gender;
import com.nguyenhien.user_service.domain.enums.UserStatus;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public final class UserProfileBuilder {
  private final UserProfileEntity entity;

  private UserProfileBuilder() {

    entity =
        UserProfileEntity.builder()
            .id(UUID.randomUUID())
            .authUserId(UUID.randomUUID())
            .email("user@test.com")
            .fullName("Nguyen Van A")
            .phone("0901234567")
            .gender(Gender.MALE)
            .birthday(LocalDate.of(2000, 1, 1))
            .avatarUrl("https://avatar.test/default.png")
            .status(UserStatus.ACTIVE)
            .profileCompleted(false)
            .loyaltyPoint(0L)
            .addresses(new ArrayList<>())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
  }

  public static UserProfileBuilder aUser() {
    return new UserProfileBuilder();
  }

  public UserProfileBuilder id(UUID id) {
    entity.setId(id);
    return this;
  }

  public UserProfileBuilder authUserId(UUID authUserId) {
    entity.setAuthUserId(authUserId);
    return this;
  }

  public UserProfileBuilder email(String email) {
    entity.setEmail(email);
    return this;
  }

  public UserProfileBuilder fullName(String fullName) {
    entity.setFullName(fullName);
    return this;
  }

  public UserProfileBuilder phone(String phone) {
    entity.setPhone(phone);
    return this;
  }

  public UserProfileBuilder gender(Gender gender) {
    entity.setGender(gender);
    return this;
  }

  public UserProfileBuilder birthday(LocalDate birthday) {
    entity.setBirthday(birthday);
    return this;
  }

  public UserProfileBuilder avatar(String avatarUrl) {
    entity.setAvatarUrl(avatarUrl);
    return this;
  }

  public UserProfileBuilder loyalty(long point) {
    entity.setLoyaltyPoint(point);
    return this;
  }

  public UserProfileBuilder active() {
    entity.setStatus(UserStatus.ACTIVE);
    return this;
  }

  public UserProfileBuilder banned() {
    entity.setStatus(UserStatus.BANNED);
    return this;
  }

  public UserProfileBuilder completed() {

    entity.setProfileCompleted(true);

    if (entity.getFullName() == null) entity.setFullName("Completed User");

    if (entity.getPhone() == null) entity.setPhone("0911111111");

    return this;
  }

  public UserProfileBuilder incomplete() {
    entity.setProfileCompleted(false);
    return this;
  }

  public UserProfileEntity build() {
    return entity;
  }
}
