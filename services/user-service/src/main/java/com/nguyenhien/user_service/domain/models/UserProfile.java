package com.nguyenhien.user_service.domain.models;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nguyenhien.user_service.domain.enums.Gender;
import com.nguyenhien.user_service.domain.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
  private UUID id;

  private UUID authUserId;

  private String email;

  private String fullName;

  private String phone;

  private Gender gender;

  private LocalDate birthday;

  private String avatarUrl;

  private UserStatus status;

  private boolean profileCompleted;

  private Long loyaltyPoint;

  private List<Address> addresses = new ArrayList<>();

  private Preference preference;

  private Instant createdAt;

  private Instant updatedAt;

  public static UserProfile create(UUID authUserId, String email) {

    UserProfile profile = new UserProfile();

    profile.authUserId = authUserId;
    profile.email = email;
    profile.status = UserStatus.ACTIVE;
    profile.profileCompleted = false;
    profile.loyaltyPoint = 0L;

    return profile;
  }

  public void updateProfile(
      String fullName, String phone, Gender gender, LocalDate birthday, String avatarUrl) {

    this.fullName = fullName;
    this.phone = phone;
    this.gender = gender;
    this.birthday = birthday;
    this.avatarUrl = avatarUrl;

    this.profileCompleted = fullName != null && phone != null;
  }

  public void addAddress(Address address) {
    this.addresses.add(address);
  }

  public void updatePreference(Preference preference) {
    this.preference = (preference != null) ? preference : null;
  }

  public void ban() {
    this.status = UserStatus.BANNED;
  }
}
