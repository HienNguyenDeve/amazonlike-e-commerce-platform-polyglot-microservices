package com.nguyenhien.user_service.infrastructure.persistences.repository;

import com.nguyenhien.user_service.domain.models.UserProfile;
import com.nguyenhien.user_service.domain.repositories.IUserProfileRepository;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import com.nguyenhien.user_service.infrastructure.persistences.mapper.IUserProfileEntityMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements IUserProfileRepository {
  private final IJpaUserProfileRepository jpaUserProfileRepository;
  private final IUserProfileEntityMapper mapper;

  @Override
  public boolean existsByAuthUserId(UUID authUserId) {
    return jpaUserProfileRepository.existsByAuthUserId(authUserId);
  }

  @Override
  public UserProfile save(UserProfile model) {
    UserProfileEntity entity = mapper.toEntity(model);

    UserProfileEntity saved = jpaUserProfileRepository.save(entity);

    return mapper.toModel(saved);
  }

  @Override
  public Optional<UserProfile> findById(UUID id) {
    return jpaUserProfileRepository.findById(id).map(mapper::toModel);
  }

  @Override
  public Optional<UserProfile> findByAuthUserId(UUID id) {
    return jpaUserProfileRepository.findByAuthUserId(id).map(mapper::toModel);
  }
}
