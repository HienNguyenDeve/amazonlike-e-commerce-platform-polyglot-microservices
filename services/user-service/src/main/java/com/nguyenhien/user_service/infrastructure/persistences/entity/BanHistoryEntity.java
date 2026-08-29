package com.nguyenhien.user_service.infrastructure.persistences.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Table(name = "user_ban_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanHistoryEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_profile_id", nullable = false)
  private UserProfileEntity userProfile;

  @Column(columnDefinition = "TEXT")
  private String reason;

  @Column(name = "banned_by")
  private UUID bannedBy;

  @Column(name = "banned_at", nullable = false)
  private Instant bannedAt;
}
