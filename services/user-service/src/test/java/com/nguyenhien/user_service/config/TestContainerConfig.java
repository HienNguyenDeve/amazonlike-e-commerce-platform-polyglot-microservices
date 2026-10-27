package com.nguyenhien.user_service.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
public abstract class TestContainerConfig {
  @Container @ServiceConnection
  protected static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16")
          .withDatabaseName("amazonlike_user")
          .withUsername("postgres")
          .withPassword("postgres");

  @Container @ServiceConnection
  protected static final KafkaContainer KAFKA = new KafkaContainer("apache/kafka:4.1.1");
}
