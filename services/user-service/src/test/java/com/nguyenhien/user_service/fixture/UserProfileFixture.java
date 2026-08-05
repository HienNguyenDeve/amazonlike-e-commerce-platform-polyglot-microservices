package com.nguyenhien.user_service.fixture;

import java.util.UUID;

import com.nguyenhien.user_service.builder.UserProfileBuilder;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;

public final class UserProfileFixture {
    private UserProfileFixture() {
    }

    public static UserProfileEntity activeUser() {

        return UserProfileBuilder.aUser()
                .active()
                .build();

    }

    public static UserProfileEntity completedUser() {

        return UserProfileBuilder.aUser()
                .completed()
                .build();

    }

    public static UserProfileEntity bannedUser() {

        return UserProfileBuilder.aUser()
                .banned()
                .build();

    }

    public static UserProfileEntity vipUser() {

        return UserProfileBuilder.aUser()
                .completed()
                .loyalty(100000)
                .build();

    }

    public static UserProfileEntity withEmail(String email) {

        return UserProfileBuilder.aUser()
                .email(email)
                .build();

    }

    public static UserProfileEntity withId(UUID id) {

        return UserProfileBuilder.aUser()
                .id(id)
                .build();

    }
}
