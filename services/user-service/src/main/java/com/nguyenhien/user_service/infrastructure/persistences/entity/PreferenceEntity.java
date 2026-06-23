package com.nguyenhien.user_service.infrastructure.persistences.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_profile_id",
            nullable = false,
            unique = true
    )
    private UserProfileEntity userProfile;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String currency;

    @Column(name = "email_notification", nullable = false)
    private boolean emailNotification;

    @Column(name = "sms_notification", nullable = false)
    private boolean smsNotification;

    @Column(name = "push_notification", nullable = false)
    private boolean pushNotification;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
