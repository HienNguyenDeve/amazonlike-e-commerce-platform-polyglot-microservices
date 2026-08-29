package com.nguyenhien.user_service.application.interfaces;

import com.nguyenhien.user_service.api.requests.UpdateUserProfileRequest;
import com.nguyenhien.user_service.api.responses.UserProfileResponse;
import java.util.UUID;

public interface IUserProfileService {
  UserProfileResponse getUserProfile(UUID userProfileId);

  UserProfileResponse updateUserProfile(UUID userProfileId, UpdateUserProfileRequest request);

  UserProfileResponse createProfile(UUID authUserId, String email);
}
