package org.example;

import org.example.SchemaValidator;
import org.junit.jupiter.api.Test;

public class SchemaValidationTest {

  @Test
  void validateSchemaStructure() {
    SchemaValidator.validateSchema("src/main/resources/graphql/schema.graphqls");
  }
}