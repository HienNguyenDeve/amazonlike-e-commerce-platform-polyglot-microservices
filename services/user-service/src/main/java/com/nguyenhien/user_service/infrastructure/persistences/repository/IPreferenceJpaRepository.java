package com.nguyenhien.user_service.infrastructure.persistences.repository;

import com.nguyenhien.user_service.infrastructure.persistences.entity.PreferenceEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPreferenceJpaRepository extends JpaRepository<PreferenceEntity, UUID> {

  Optional<PreferenceEntity> findByUserProfile_Id(UUID userProfileId);
}
