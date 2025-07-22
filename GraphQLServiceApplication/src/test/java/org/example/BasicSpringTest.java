package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BasicSpringTest {

  @Test
  void contextLoads() {
    // If this fails, we have a fundamental Spring Boot issue
    // If this passes, the issue is GraphQL-specific
  }
}