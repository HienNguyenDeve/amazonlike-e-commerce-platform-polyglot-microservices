package com.nguyenhien.user_service.infrastructure.persistences.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.nguyenhien.user_service.domain.models.UserProfile;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface IUserProfileEntityMapper {
  UserProfileEntity toEntity(UserProfile model);

  UserProfile toModel(UserProfileEntity entity);
}
