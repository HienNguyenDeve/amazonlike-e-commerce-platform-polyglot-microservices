package com.nguyenhien.user_service.domain.models;

import com.nguyenhien.user_service.domain.enums.AddressType;
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
public class Address {
  private UUID id;

  private UserProfileEntity userProfile;

  private String receiverName;

  private String phone;

  private String province;

  private String district;

  private String ward;

  private String detailAddress;

  private String postalCode;

  private AddressType addressType;

  private boolean isDefault;

  private Instant createdAt;

  private Instant updatedAt;
}
