package com.nguyenhien.user_service.assertions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import com.nguyenhien.user_service.infrastructure.persistences.repository.IJpaUserProfileRepository;

public final class DatabaseAssert {
    private DatabaseAssert() {
    }

    public static void userExists(IJpaUserProfileRepository repository, UUID id) {
        assertThat(repository.findById(id)).isPresent();
    }

    public static void userNotExists(IJpaUserProfileRepository repository, UUID id) {
        assertThat(repository.findById(id)).isEmpty();
    }
}
