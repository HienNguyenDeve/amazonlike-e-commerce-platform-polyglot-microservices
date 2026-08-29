package com.nguyenhien.user_service.application.mappers;

import com.nguyenhien.user_service.api.responses.PreferenceResponse;
import com.nguyenhien.user_service.domain.models.Preference;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPreferenceMapper {

  PreferenceResponse toResponse(Preference preference);
}
