package com.nguyenhien.user_service.api.responses;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.nguyenhien.user_service.domain.enums.Gender;
import com.nguyenhien.user_service.domain.enums.UserStatus;

public record UserProfileResponse(

        UUID id,

        UUID authUserId,

        String email,

        String fullName,

        String phone,

        Gender gender,

        LocalDate birthday,

        String avatarUrl,

        UserStatus status,

        boolean profileCompleted,

        Long loyaltyPoint,

        Instant createdAt,

        Instant updatedAt
) {
}
