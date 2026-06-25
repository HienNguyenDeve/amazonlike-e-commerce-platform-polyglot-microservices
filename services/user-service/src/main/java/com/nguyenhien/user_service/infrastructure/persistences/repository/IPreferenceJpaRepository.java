package com.nguyenhien.user_service.infrastructure.persistences.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenhien.user_service.infrastructure.persistences.entity.PreferenceEntity;

@Repository
public interface IPreferenceJpaRepository extends JpaRepository<PreferenceEntity, UUID> {

    Optional<PreferenceEntity> findByUserProfile_Id(UUID userProfileId);

}
