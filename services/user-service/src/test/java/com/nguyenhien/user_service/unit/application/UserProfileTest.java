package com.nguyenhien.user_service.unit.application;

import static org.assertj.core.api.Assertions.*;

import com.nguyenhien.user_service.domain.enums.Gender;
import com.nguyenhien.user_service.domain.enums.UserStatus;
import com.nguyenhien.user_service.domain.models.Address;
import com.nguyenhien.user_service.domain.models.Preference;
import com.nguyenhien.user_service.domain.models.UserProfile;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserProfileTest {
  private UUID authUserId;

  @BeforeEach
  void setUp() {
    authUserId = UUID.randomUUID();
  }

  @Test
  @DisplayName("Should create profile with default values")
  void shouldCreateProfileSuccessfully() {

    UserProfile profile = UserProfile.create(authUserId, "john@gmail.com");

    assertThat(profile.getAuthUserId()).isEqualTo(authUserId);

    assertThat(profile.getEmail()).isEqualTo("john@gmail.com");

    assertThat(profile.getStatus()).isEqualTo(UserStatus.ACTIVE);

    assertThat(profile.isProfileCompleted()).isFalse();

    assertThat(profile.getLoyaltyPoint()).isZero();

    assertThat(profile.getAddresses()).isEmpty();

    assertThat(profile.getPreference()).isNull();
  }

  @Test
  @DisplayName("Should update profile and mark completed")
  void shouldUpdateProfileSuccessfully() {

    UserProfile profile = UserProfile.create(authUserId, "john@gmail.com");

    LocalDate birthday = LocalDate.of(1998, 1, 1);

    profile.updateProfile("John", "0123456789", Gender.MALE, birthday, "avatar.jpg");

    assertThat(profile.getFullName()).isEqualTo("John");

    assertThat(profile.getPhone()).isEqualTo("0123456789");

    assertThat(profile.getGender()).isEqualTo(Gender.MALE);

    assertThat(profile.getBirthday()).isEqualTo(birthday);

    assertThat(profile.getAvatarUrl()).isEqualTo("avatar.jpg");

    assertThat(profile.isProfileCompleted()).isTrue();
  }

  @Test
  @DisplayName("Should not complete profile when phone is null")
  void shouldNotCompleteProfileWhenPhoneMissing() {

    UserProfile profile = UserProfile.create(authUserId, "john@gmail.com");

    profile.updateProfile("John", null, Gender.MALE, null, null);

    assertThat(profile.isProfileCompleted()).isFalse();
  }

  @Test
  @DisplayName("Should not complete profile when full name is null")
  void shouldNotCompleteProfileWhenFullNameMissing() {

    UserProfile profile = UserProfile.create(authUserId, "john@gmail.com");

    profile.updateProfile(null, "0123456789", Gender.MALE, null, null);

    assertThat(profile.isProfileCompleted()).isFalse();
  }

  @Test
  @DisplayName("Should add address")
  void shouldAddAddress() {

    UserProfile profile = UserProfile.create(authUserId, "john@gmail.com");

    Address address = new Address();

    profile.addAddress(address);

    assertThat(profile.getAddresses()).hasSize(1).contains(address);
  }

  @Test
  @DisplayName("Should update preference")
  void shouldUpdatePreference() {

    UserProfile profile = UserProfile.create(authUserId, "john@gmail.com");

    Preference preference = new Preference();

    profile.updatePreference(preference);

    assertThat(profile.getPreference()).isEqualTo(preference);
  }

  @Test
  @DisplayName("Should ban user")
  void shouldBanUser() {

    UserProfile profile = UserProfile.create(authUserId, "john@gmail.com");

    profile.ban();

    assertThat(profile.getStatus()).isEqualTo(UserStatus.BANNED);
  }
}
