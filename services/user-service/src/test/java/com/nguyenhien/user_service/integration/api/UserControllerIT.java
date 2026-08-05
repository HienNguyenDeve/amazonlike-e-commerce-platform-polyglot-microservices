package com.nguyenhien.user_service.integration.api;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nguyenhien.user_service.config.TestSecurityConfig;
import com.nguyenhien.user_service.domain.enums.UserStatus;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import com.nguyenhien.user_service.infrastructure.persistences.repository.IJpaUserProfileRepository;
import com.nguyenhien.user_service.integration.BaseControllerIT;

@SpringBootTest
@Import(TestSecurityConfig.class)
public class UserControllerIT extends BaseControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IJpaUserProfileRepository repository;

    @Test
    void shouldReturnUserProfile() throws Exception {

        UserProfileEntity entity = repository.save(
                UserProfileEntity.builder()
                        .authUserId(UUID.randomUUID())
                        .email("abc@gmail.com")
                        .status(UserStatus.ACTIVE)
                        .loyaltyPoint(0L)
                        .build()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/" + entity.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value("abc@gmail.com"));
    }
}
