package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.main.web-application-type=none")
class GroupFeedIntegrationTest {

  @Autowired
  GraphQlTester graphQlTester;

  @Test
  void testPinnedThreads() {
    String query = """
            query {
              groupFeed(groupId: "abc") {
                threads { id title pinned }
                pinnedThreads { id title pinned }
              }
            }
            """;

    graphQlTester.document(query)
            .execute()
            .path("groupFeed.threads").entityList(Object.class).hasSize(2)
            .path("groupFeed.pinnedThreads").entityList(Object.class).hasSize(2);
  }
}