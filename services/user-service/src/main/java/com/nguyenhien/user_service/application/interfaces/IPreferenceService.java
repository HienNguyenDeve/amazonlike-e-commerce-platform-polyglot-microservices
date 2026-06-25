package com.nguyenhien.user_service.application.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.nguyenhien.user_service.api.requests.PreferenceSearchRequest;
import com.nguyenhien.user_service.api.requests.UpdatePreferenceRequest;
import com.nguyenhien.user_service.api.responses.PreferenceResponse;

public interface IPreferenceService {

    PreferenceResponse getMyPreferences(UUID userId);

    PreferenceResponse updatePreferences(UUID userId,UpdatePreferenceRequest request);

    List<PreferenceResponse> findAll();

    List<PreferenceResponse> findByName(String keyword);

    Page<PreferenceResponse> findPaginated(PreferenceSearchRequest searchDTO);

    PreferenceResponse findById(UUID id);

    PreferenceResponse create(UpdatePreferenceRequest jobDTO);

    boolean delete(UUID id);

}
