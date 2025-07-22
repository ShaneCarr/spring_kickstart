package org.example;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.GraphQlSource;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SchemaBootTest {

  @Autowired
  GraphQlSource graphQlSource;

  @Test
  void validateSchemaLoads() {
    // This will fail loudly with schema wiring errors like:
    // "Missing resolver for field 'groupFeed'"
    graphQlSource.schema();
  }

  @Test
  void schemaPrints() {
    System.out.println("---- SCHEMA SDL ----");
    System.out.println(graphQlSource.schema());
    System.out.println("---- END SCHEMA ----");
    assertNotNull(graphQlSource.schema());
  }

  @Test
  void directParse() throws FileNotFoundException {
    SchemaParser parser = new SchemaParser();
    TypeDefinitionRegistry registry = parser.parse(
            new FileReader("src/main/resources/graphql/schema.graphqls")
    );
    SchemaGenerator generator = new SchemaGenerator();
    RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring().build();
    GraphQLSchema schema = generator.makeExecutableSchema(registry, wiring);
    System.out.println(schema);
  }

  @Test
  void validateSchemaWiringManually() throws Exception {
    SchemaParser parser = new SchemaParser();
    SchemaGenerator generator = new SchemaGenerator();

    TypeDefinitionRegistry registry = parser.parse(new FileReader("src/main/resources/graphql/schema.graphqls"));

    RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
            .type("Query", typeWiring -> typeWiring
                    .dataFetcher("groupFeed", env -> {
                      throw new RuntimeException("This should never be called");
                    })
            )
            .build();

    GraphQLSchema schema = generator.makeExecutableSchema(registry, wiring);
    System.out.println(schema);
  }
}