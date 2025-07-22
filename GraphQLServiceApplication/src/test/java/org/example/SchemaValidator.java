package org.example;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import graphql.schema.validation.SchemaValidationError;

import java.io.File;
import java.io.FileReader;
import java.util.List;

public class SchemaValidator {

  public static void validateSchema(String schemaPath) {
    try {
      System.out.println("=== SCHEMA VALIDATION REPORT ===");

      // Step 1: Parse the schema file
      SchemaParser parser = new SchemaParser();
      TypeDefinitionRegistry registry = parser.parse(new FileReader(schemaPath));
      System.out.println("✓ Schema file parsed successfully");

      // Step 2: Try to build executable schema with minimal wiring
      RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring().build();
      SchemaGenerator generator = new SchemaGenerator();
      GraphQLSchema schema = generator.makeExecutableSchema(registry, wiring);

      System.out.println("✓ Schema structure is valid");
      System.out.println("✓ All types are properly defined");

      // Step 3: Check for missing resolvers
      checkForMissingResolvers(registry);

    } catch (Exception e) {
      System.err.println("❌ SCHEMA ERROR: " + e.getMessage());
      if (e.getCause() != null) {
        System.err.println("❌ ROOT CAUSE: " + e.getCause().getMessage());
      }
    }
  }

  private static void checkForMissingResolvers(TypeDefinitionRegistry registry) {
    System.out.println("\n=== RESOLVER REQUIREMENTS ===");

    registry.getType("Query").ifPresent(queryType -> {
      if (queryType instanceof graphql.language.ObjectTypeDefinition) {
        graphql.language.ObjectTypeDefinition query = (graphql.language.ObjectTypeDefinition) queryType;
        query.getFieldDefinitions().forEach(field -> {
          System.out.println("⚠️  Missing resolver for Query." + field.getName());
        });
      }
    });
  }
}