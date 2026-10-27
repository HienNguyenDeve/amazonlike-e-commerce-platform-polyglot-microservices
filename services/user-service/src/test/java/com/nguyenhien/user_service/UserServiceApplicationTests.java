package com.nguyenhien.user_service;

import com.nguyenhien.user_service.config.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
class UserServiceApplicationTests extends TestContainerConfig {

  @Test
  void contextLoads() {}
}
