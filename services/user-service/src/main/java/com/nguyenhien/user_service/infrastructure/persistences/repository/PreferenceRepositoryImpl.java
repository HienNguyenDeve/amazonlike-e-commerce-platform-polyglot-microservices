package com.nguyenhien.user_service.infrastructure.persistences.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.nguyenhien.user_service.domain.models.Preference;
import com.nguyenhien.user_service.domain.repositories.IPreferenceRepository;
import com.nguyenhien.user_service.infrastructure.persistences.entity.PreferenceEntity;
import com.nguyenhien.user_service.infrastructure.persistences.mapper.IPreferenceEntityMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PreferenceRepositoryImpl implements IPreferenceRepository {

    private final IPreferenceJpaRepository jpaRepository;

    private final IPreferenceEntityMapper entityMapper;

    @Override
    public Optional<Preference> findByUserProfileId(UUID userProfileId) {

        return jpaRepository
                .findByUserProfile_Id(userProfileId)
                .map(entityMapper::toModel);
    }

    @Override
    public Preference save(Preference preference) {

        PreferenceEntity entity = entityMapper.toEntity(preference);

        PreferenceEntity saved = jpaRepository.save(entity);

        return entityMapper.toModel(saved);
    }
}