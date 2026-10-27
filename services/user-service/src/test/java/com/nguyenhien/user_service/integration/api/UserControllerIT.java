package com.nguyenhien.user_service.integration.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nguyenhien.user_service.domain.enums.UserStatus;
import com.nguyenhien.user_service.infrastructure.persistences.entity.UserProfileEntity;
import com.nguyenhien.user_service.infrastructure.persistences.repository.IJpaUserProfileRepository;
import com.nguyenhien.user_service.integration.BaseControllerIT;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class UserControllerIT extends BaseControllerIT {
  @Autowired private MockMvc mockMvc;

  @Autowired private IJpaUserProfileRepository repository;

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/users/" + UUID.randomUUID()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "test-user", roles = "USER")
  void shouldReturnUserProfile() throws Exception {

    UserProfileEntity entity =
        repository.save(
            UserProfileEntity.builder()
                .authUserId(UUID.randomUUID())
                .email("abc@gmail.com")
                .status(UserStatus.ACTIVE)
                .loyaltyPoint(0L)
                .build());

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/users/" + entity.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("abc@gmail.com"));
  }
}
