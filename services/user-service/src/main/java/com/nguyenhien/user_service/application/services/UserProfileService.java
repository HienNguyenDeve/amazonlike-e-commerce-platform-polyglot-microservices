package com.nguyenhien.user_service.application.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhien.user_service.api.requests.UpdateUserProfileRequest;
import com.nguyenhien.user_service.api.responses.UserProfileResponse;
import com.nguyenhien.user_service.application.events.message.UserProfileCreatedEvent;
import com.nguyenhien.user_service.application.events.pulisher.UserProfileCreatedProducer;
import com.nguyenhien.user_service.application.interfaces.IUserProfileService;
import com.nguyenhien.user_service.application.mappers.IUserProfileMapper;
import com.nguyenhien.user_service.common.exception.UserProfileNotFoundException;
import com.nguyenhien.user_service.domain.models.UserProfile;
import com.nguyenhien.user_service.domain.repositories.IUserProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService implements IUserProfileService {

        private final IUserProfileRepository userProfileRepository;
        private final IUserProfileMapper dtoMapper;
        private final UserProfileCreatedProducer userProfileCreatedProducer;

        @Override
        public UserProfileResponse getUserProfile(UUID userProfileId) {
                UserProfile profile = userProfileRepository.findById(userProfileId)
                                .orElseThrow(
                                                () -> new UserProfileNotFoundException(
                                                                userProfileId));

                UserProfileCreatedEvent event = new UserProfileCreatedEvent(
                                UUID.randomUUID(),
                                profile.getId(),
                                profile.getAuthUserId(),
                                profile.getEmail(),
                                Instant.now());

                userProfileCreatedProducer.publishUserProfileCreated(
                                event);

                return dtoMapper.toResponse(profile);

        }

        @Override
        public UserProfileResponse updateUserProfile(UUID userProfileId, UpdateUserProfileRequest request) {
                UserProfile profile = userProfileRepository.findById(userProfileId)
                                .orElseThrow(
                                                () -> new UserProfileNotFoundException(
                                                                userProfileId));

                profile.updateProfile(
                                request.fullName(),
                                request.phone(),
                                request.gender(),
                                request.birthday(),
                                request.avatarUrl());

                UserProfile updated = userProfileRepository.save(profile);

                return dtoMapper.toResponse(updated);
        }

        @Override
        public UserProfileResponse createProfile(UUID authUserId, String email) {
                if (userProfileRepository.existsByAuthUserId(authUserId)) {
                        throw new IllegalStateException("Profile already exists");
                }

                UserProfile profile = UserProfile.create(authUserId, email);

                UserProfile saved = userProfileRepository.save(profile);

                UserProfileCreatedEvent event = new UserProfileCreatedEvent(
                                UUID.randomUUID(),
                                saved.getId(),
                                saved.getAuthUserId(),
                                saved.getEmail(),
                                Instant.now());

                userProfileCreatedProducer.publishUserProfileCreated(event);

                return dtoMapper.toResponse(saved);
        }

}
