package com.nguyenhien.user_service.api.requests;

import com.nguyenhien.user_service.domain.enums.Gender;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateUserProfileRequest(
    @Size(max = 255) String fullName,
    String phone,
    Gender gender,
    LocalDate birthday,
    String avatarUrl) {}
