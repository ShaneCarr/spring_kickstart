package org.example;

import com.graph.graphqlexample.graphql.GraphqlApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GraphqlApplication.class)
//@TestPropertySource(properties = "spring.main.web-application-type=reactive")
public class GroupFeedIntegrationTest {
    @LocalServerPort
    int port;

    @Test
    void testPinnedThreads() {
        WebTestClient client = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + "/graphql")
                .build();

        GraphQlTester graphQlTester = HttpGraphQlTester.create(client);

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
