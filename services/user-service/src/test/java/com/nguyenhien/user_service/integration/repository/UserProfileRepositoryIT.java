package com.nguyenhien.user_service.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nguyenhien.user_service.domain.enums.UserStatus;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import com.nguyenhien.user_service.infrastructure.persistences.repository.IJpaUserProfileRepository;
import com.nguyenhien.user_service.integration.BaseRepositoryIT;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserProfileRepositoryIT extends BaseRepositoryIT {
  @Autowired private IJpaUserProfileRepository repository;

  @Test
  void shouldSaveUserProfile() {

    UserProfileEntity entity =
        UserProfileEntity.builder()
            .authUserId(UUID.randomUUID())
            .email("test@gmail.com")
            .status(UserStatus.ACTIVE)
            .loyaltyPoint(0L)
            .build();

    UserProfileEntity saved = repository.save(entity);

    assertThat(saved.getId()).isNotNull();
  }

  @Test
  void shouldFindByAuthUserId() {

    UUID authId = UUID.randomUUID();
    repository.save(
        UserProfileEntity.builder()
            .authUserId(authId)
            .email("abc@gmail.com")
            .status(UserStatus.ACTIVE)
            .loyaltyPoint(0L)
            .build());

    Optional<UserProfileEntity> result = repository.findByAuthUserId(authId);

    assertThat(result).isPresent();
  }
}
