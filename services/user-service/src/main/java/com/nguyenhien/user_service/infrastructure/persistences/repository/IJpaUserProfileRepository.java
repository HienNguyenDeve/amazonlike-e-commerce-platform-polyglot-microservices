package com.nguyenhien.user_service.infrastructure.persistences.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;

public interface IJpaUserProfileRepository extends JpaRepository<UserProfileEntity, UUID>{
    boolean existsByAuthUserId(UUID authUserId);
}
