package com.nguyenhien.user_service.infrastructure.persistences.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.nguyenhien.user_service.domain.models.Preference;
import com.nguyenhien.user_service.infrastructure.persistences.entity.PreferenceEntity;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = IUserProfileEntityMapper.class,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface IPreferenceEntityMapper {
  PreferenceEntity toEntity(Preference model);

  Preference toModel(PreferenceEntity entity);
}
