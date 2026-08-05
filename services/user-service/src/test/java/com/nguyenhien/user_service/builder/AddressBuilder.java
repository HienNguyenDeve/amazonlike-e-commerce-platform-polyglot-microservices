package com.nguyenhien.user_service.builder;

import java.time.Instant;
import java.util.UUID;

import com.nguyenhien.user_service.domain.enums.AddressType;
import com.nguyenhien.user_service.infrastructure.persistences.entity.AddressEntity;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;

public final class AddressBuilder {
    private final AddressEntity entity;

    private AddressBuilder() {

        entity = AddressEntity.builder()
                .id(UUID.randomUUID())
                .receiverName("Nguyen Van A")
                .phone("0901234567")
                .province("Ha Noi")
                .district("Hoang Mai")
                .ward("Dinh Cong")
                .detailAddress("123 Dinh Cong")
                .postalCode("100000")
                .addressType(AddressType.HOME)
                .isDefault(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

    }

    public static AddressBuilder anAddress() {
        return new AddressBuilder();
    }

    public AddressBuilder user(UserProfileEntity user) {
        entity.setUserProfile(user);
        return this;
    }

    public AddressBuilder receiver(String name) {
        entity.setReceiverName(name);
        return this;
    }

    public AddressBuilder phone(String phone) {
        entity.setPhone(phone);
        return this;
    }

    public AddressBuilder province(String province) {
        entity.setProvince(province);
        return this;
    }

    public AddressBuilder district(String district) {
        entity.setDistrict(district);
        return this;
    }

    public AddressBuilder ward(String ward) {
        entity.setWard(ward);
        return this;
    }

    public AddressBuilder detail(String detail) {
        entity.setDetailAddress(detail);
        return this;
    }

    public AddressBuilder home() {
        entity.setAddressType(AddressType.HOME);
        return this;
    }

    public AddressBuilder office() {
        entity.setAddressType(AddressType.OFFICE);
        return this;
    }

    public AddressBuilder defaultAddress() {
        entity.setDefault(true);
        return this;
    }

    public AddressBuilder notDefault() {
        entity.setDefault(false);
        return this;
    }

    public AddressEntity build() {
        return entity;
    }
}
