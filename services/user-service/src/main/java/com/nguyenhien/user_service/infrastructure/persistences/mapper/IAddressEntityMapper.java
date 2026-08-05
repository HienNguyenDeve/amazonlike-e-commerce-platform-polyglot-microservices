package com.nguyenhien.user_service.infrastructure.persistences.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.nguyenhien.user_service.domain.models.Address;
import com.nguyenhien.user_service.infrastructure.persistences.entity.AddressEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAddressEntityMapper {
    AddressEntity toEntity(Address model);

    Address toModel(AddressEntity entity);
}