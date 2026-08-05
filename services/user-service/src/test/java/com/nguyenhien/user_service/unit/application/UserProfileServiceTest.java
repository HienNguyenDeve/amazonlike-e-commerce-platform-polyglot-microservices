package com.nguyenhien.user_service.unit.application;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nguyenhien.user_service.api.requests.UpdateUserProfileRequest;
import com.nguyenhien.user_service.api.responses.UserProfileResponse;
import com.nguyenhien.user_service.application.events.message.UserProfileCreatedEvent;
import com.nguyenhien.user_service.application.events.pulisher.UserProfileCreatedProducer;
import com.nguyenhien.user_service.application.mappers.IUserProfileMapper;
import com.nguyenhien.user_service.application.services.UserProfileService;
import com.nguyenhien.user_service.common.exception.UserProfileNotFoundException;
import com.nguyenhien.user_service.domain.enums.Gender;
import com.nguyenhien.user_service.domain.models.UserProfile;
import com.nguyenhien.user_service.domain.repositories.IUserProfileRepository;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
        // Mock
        @Mock
        private IUserProfileRepository repository;

        @Mock
        private IUserProfileMapper mapper;

        @Mock
        private UserProfileCreatedProducer producer;

        // Inject mocks
        @InjectMocks
        private UserProfileService service;

        private UUID userId;
        private UUID authUserId;

        private UserProfile profile;

        private UserProfileResponse response;

        // Before each
        @BeforeEach
        void setUp() {

                userId = UUID.randomUUID();
                authUserId = UUID.randomUUID();

                profile = UserProfile.create(
                                authUserId,
                                "john@gmail.com");

                profile.setId(userId);

                response = new UserProfileResponse();
        }

        // Test
        // Create profile
        @Test
        void createProfile_shouldCreateProfileSuccessfull_whenNotExistProfile() {
                // Arrange
                when(repository.existsByAuthUserId(authUserId))
                                .thenReturn(false);

                when(repository.save(any(UserProfile.class)))
                                .thenReturn(profile);

                when(mapper.toResponse(profile))
                                .thenReturn(response);

                // Act
                UserProfileResponse result = service.createProfile(
                                authUserId,
                                "john@gmail.com");

                // Assert
                assertThat(result).isEqualTo(response);

                verify(repository).existsByAuthUserId(authUserId);

                verify(repository).save(any(UserProfile.class));

                verify(mapper).toResponse(profile);

                verify(producer)
                                .publishUserProfileCreated(any(UserProfileCreatedEvent.class));
        }

        // Throw exception
        @Test
        void createProfile_shouldThrowException_WhenProfileAlreadyExists() {
                // Arrange
                when(repository.existsByAuthUserId(authUserId))
                                .thenReturn(true);

                // Act
                assertThatThrownBy(() -> service.createProfile(authUserId, "john@gmail.com"))
                                .isInstanceOf(IllegalStateException.class)
                                .hasMessage("Profile already exists");

                // Assert
                verify(repository, never())
                                .save(any());

                verifyNoInteractions(mapper);

                verifyNoInteractions(producer);
        }

        // Get profile
        @Test
        void getUserProfile_showReturnProfile_WhenProfileAlreadyExists() {
                // Arrange
                when(repository.findById(userId))
                                .thenReturn(Optional.of(profile));

                when(mapper.toResponse(profile))
                                .thenReturn(response);

                // Act
                UserProfileResponse result = service.getUserProfile(userId);

                // Assert
                assertThat(result)
                                .isEqualTo(response);

                verify(repository)
                                .findById(userId);

                verify(mapper)
                                .toResponse(profile);

        }

        // Throw exception
        @Test
        void getUserProfile_shouldThrow_WhenProfileNotFound() {

                when(repository.findById(userId))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> service.getUserProfile(userId))
                                .isInstanceOf(UserProfileNotFoundException.class);

                verify(repository)
                                .findById(userId);

                verifyNoInteractions(mapper);

                verifyNoInteractions(producer);
        }

        // Update profile
        @Test
        void updateUserProfile_shouldUpdateProfileSuccessfully() {

                UpdateUserProfileRequest request = new UpdateUserProfileRequest(
                                "John Updated",
                                "0123456789",
                                Gender.MALE,
                                null,
                                "avatar.jpg");

                when(repository.findById(userId))
                                .thenReturn(Optional.of(profile));

                when(repository.save(profile))
                                .thenReturn(profile);

                when(mapper.toResponse(profile))
                                .thenReturn(response);

                UserProfileResponse result = service.updateUserProfile(
                                userId,
                                request);

                assertThat(result)
                                .isEqualTo(response);

                verify(repository)
                                .findById(userId);

                verify(repository)
                                .save(profile);

                verify(mapper)
                                .toResponse(profile);
        }

        @Test
        void updateUserProfile_shouldThrow_WhenUpdatingMissingProfile() {

                UpdateUserProfileRequest request = mock(UpdateUserProfileRequest.class);

                when(repository.findById(userId))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> service.updateUserProfile(userId, request))
                                .isInstanceOf(UserProfileNotFoundException.class);

                verify(repository)
                                .findById(userId);

                verify(repository, never())
                                .save(any());

                verifyNoInteractions(mapper);
        }
}
