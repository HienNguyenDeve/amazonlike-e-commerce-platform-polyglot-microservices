package com.nguyenhien.user_service.application.services;

import com.nguyenhien.user_service.api.requests.PreferenceSearchRequest;
import com.nguyenhien.user_service.api.requests.UpdatePreferenceRequest;
import com.nguyenhien.user_service.api.responses.PreferenceResponse;
import com.nguyenhien.user_service.application.interfaces.IPreferenceService;
import com.nguyenhien.user_service.application.mappers.IPreferenceMapper;
import com.nguyenhien.user_service.common.exception.PreferenceNotFoundException;
import com.nguyenhien.user_service.common.exception.UserProfileNotFoundException;
import com.nguyenhien.user_service.domain.models.Preference;
import com.nguyenhien.user_service.domain.models.UserProfile;
import com.nguyenhien.user_service.domain.repositories.IPreferenceRepository;
import com.nguyenhien.user_service.domain.repositories.IUserProfileRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PreferenceService implements IPreferenceService {

  private final IUserProfileRepository userProfileRepository;

  private final IPreferenceRepository preferenceRepository;

  private final IPreferenceMapper preferenceMapper;

  @Override
  @Transactional(readOnly = true)
  public PreferenceResponse getMyPreferences(UUID userId) {

    UserProfile profile =
        userProfileRepository
            .findByAuthUserId(userId)
            .orElseThrow(() -> new UserProfileNotFoundException(userId));

    Preference preference =
        preferenceRepository
            .findByUserProfileId(profile.getId())
            .orElseThrow(PreferenceNotFoundException::new);

    return preferenceMapper.toResponse(preference);
  }

  @Override
  @Transactional
  public PreferenceResponse updatePreferences(UUID userId, UpdatePreferenceRequest request) {

    UserProfile profile =
        userProfileRepository
            .findByAuthUserId(userId)
            .orElseThrow(() -> new UserProfileNotFoundException(userId));

    Preference preference =
        preferenceRepository
            .findByUserProfileId(profile.getId())
            .orElseThrow(PreferenceNotFoundException::new);

    preference.setLanguage(request.getLanguage());

    preference.setCurrency(request.getCurrency());

    preference.setEmailNotification(request.isEmailNotification());

    preference.setSmsNotification(request.isSmsNotification());

    preference.setPushNotification(request.isPushNotification());

    Preference saved = preferenceRepository.save(preference);

    return preferenceMapper.toResponse(saved);
  }

  @Override
  public List<PreferenceResponse> findAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

  @Override
  public List<PreferenceResponse> findByName(String keyword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findByName'");
  }

  @Override
  public Page<PreferenceResponse> findPaginated(PreferenceSearchRequest searchDTO) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findPaginated'");
  }

  @Override
  public PreferenceResponse findById(UUID id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public PreferenceResponse create(UpdatePreferenceRequest jobDTO) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

  @Override
  public boolean delete(UUID id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
}
