package com.nguyenhien.user_service.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

@Testcontainers
public abstract class TestContainerConfig {
        @Container
        @ServiceConnection
        protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16")
                        .withDatabaseName("amazonlike_user")
                        .withUsername("postgres")
                        .withPassword("postgres")
                        .withReuse(true);

}
