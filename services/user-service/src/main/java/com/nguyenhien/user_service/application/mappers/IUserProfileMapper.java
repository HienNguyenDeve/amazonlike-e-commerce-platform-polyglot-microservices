package com.nguyenhien.user_service.application.mappers;

import com.nguyenhien.user_service.api.responses.UserProfileResponse;
import com.nguyenhien.user_service.domain.models.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface IUserProfileMapper {
  UserProfileResponse toResponse(UserProfile entity);
}
