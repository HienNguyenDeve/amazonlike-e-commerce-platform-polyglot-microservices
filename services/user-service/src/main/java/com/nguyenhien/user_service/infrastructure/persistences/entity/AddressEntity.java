package com.nguyenhien.user_service.infrastructure.persistences.entity;

import com.nguyenhien.user_service.domain.enums.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_profile_id")
  private UserProfileEntity userProfile;

  @Column(name = "receiver_name")
  private String receiverName;

  private String phone;

  private String province;

  private String district;

  private String ward;

  @Column(name = "detail_address")
  private String detailAddress;

  @Column(name = "postal_code")
  private String postalCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "address_type")
  private AddressType addressType;

  @Column(name = "is_default")
  private boolean isDefault;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;
}
