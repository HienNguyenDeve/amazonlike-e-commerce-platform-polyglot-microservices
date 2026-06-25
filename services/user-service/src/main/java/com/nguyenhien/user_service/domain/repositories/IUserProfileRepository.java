package com.nguyenhien.user_service.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import com.nguyenhien.user_service.domain.models.UserProfile;

public interface IUserProfileRepository {
    boolean existsByAuthUserId(UUID authUserId);

    UserProfile save(UserProfile profile);

    Optional<UserProfile> findById(UUID id);

    Optional<UserProfile> findByAuthUserId(UUID id);

}
