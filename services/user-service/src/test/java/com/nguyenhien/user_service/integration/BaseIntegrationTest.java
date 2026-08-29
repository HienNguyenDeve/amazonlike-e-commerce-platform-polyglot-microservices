package com.nguyenhien.user_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenhien.user_service.config.TestContainerConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest extends TestContainerConfig {
  @Autowired protected ObjectMapper objectMapper;

  @Autowired protected EntityManager entityManager;

  @Autowired protected JdbcTemplate jdbcTemplate;

  @BeforeEach
  void flushPersistenceContext() {
    entityManager.clear();
  }

  protected String toJson(Object object) throws Exception {
    return objectMapper.writeValueAsString(object);
  }

  protected <T> T fromJson(String json, Class<T> clazz) throws Exception {
    return objectMapper.readValue(json, clazz);
  }

  protected void flush() {
    entityManager.flush();
    entityManager.clear();
  }
}
