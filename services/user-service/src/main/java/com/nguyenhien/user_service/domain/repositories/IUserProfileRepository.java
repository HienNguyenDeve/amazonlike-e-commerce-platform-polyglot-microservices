package com.nguyenhien.user_service.domain.repositories;

import com.nguyenhien.user_service.domain.models.UserProfile;
import java.util.Optional;
import java.util.UUID;

public interface IUserProfileRepository {
  boolean existsByAuthUserId(UUID authUserId);

  UserProfile save(UserProfile profile);

  Optional<UserProfile> findById(UUID id);

  Optional<UserProfile> findByAuthUserId(UUID id);
}
