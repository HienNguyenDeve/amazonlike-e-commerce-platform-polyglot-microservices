package com.nguyenhien.user_service.domain.repositories;

import com.nguyenhien.user_service.domain.models.Preference;
import java.util.Optional;
import java.util.UUID;

public interface IPreferenceRepository {

  Optional<Preference> findByUserProfileId(UUID userProfileId);

  Preference save(Preference preference);
}
