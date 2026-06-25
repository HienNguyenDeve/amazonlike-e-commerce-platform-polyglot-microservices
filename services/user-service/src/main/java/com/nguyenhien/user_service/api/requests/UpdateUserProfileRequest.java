package com.nguyenhien.user_service.api.requests;

import java.time.LocalDate;

import com.nguyenhien.user_service.domain.enums.Gender;

import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(

        @Size(max = 255)
        String fullName,

        String phone,

        Gender gender,

        LocalDate birthday,

        String avatarUrl
) {
}
