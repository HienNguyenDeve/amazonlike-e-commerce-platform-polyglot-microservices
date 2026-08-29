package com.nguyenhien.user_service.infrastructure.persistences.repository;

import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJpaUserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {
  boolean existsByAuthUserId(UUID authUserId);

  Optional<UserProfileEntity> findByAuthUserId(UUID authUserId);
}
