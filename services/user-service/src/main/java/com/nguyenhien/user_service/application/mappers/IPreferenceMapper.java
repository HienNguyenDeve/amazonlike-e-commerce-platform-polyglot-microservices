package com.nguyenhien.user_service.application.mappers;

import org.mapstruct.Mapper;

import com.nguyenhien.user_service.api.responses.PreferenceResponse;
import com.nguyenhien.user_service.domain.models.Preference;

@Mapper(componentModel = "spring")
public interface IPreferenceMapper {

    PreferenceResponse toResponse(Preference preference);

}
