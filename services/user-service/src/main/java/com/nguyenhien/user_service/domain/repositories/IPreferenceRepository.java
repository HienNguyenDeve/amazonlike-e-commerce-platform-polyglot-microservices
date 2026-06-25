package com.nguyenhien.user_service.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import com.nguyenhien.user_service.domain.models.Preference;

public interface IPreferenceRepository {

    Optional<Preference> findByUserProfileId(UUID userProfileId);

    Preference save(Preference preference);

}
