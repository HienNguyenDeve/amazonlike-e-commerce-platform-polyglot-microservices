package com.nguyenhien.user_service.application.interfaces;

import java.util.UUID;

import com.nguyenhien.user_service.api.requests.UpdateUserProfileRequest;
import com.nguyenhien.user_service.api.responses.UserProfileResponse;

public interface IUserProfileService {
    UserProfileResponse getUserProfile(UUID userProfileId);

    UserProfileResponse updateUserProfile(
            UUID userProfileId,
            UpdateUserProfileRequest request);

    UserProfileResponse createProfile(
            UUID authUserId,
            String email);
}
