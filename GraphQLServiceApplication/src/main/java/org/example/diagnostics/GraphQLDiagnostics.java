package org.example.diagnostics;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GraphQLDiagnostics {

  @EventListener
  public void onApplicationFailed(ApplicationFailedEvent event) {
    Throwable cause = event.getException();

    System.err.println("\n🔍 DIAGNOSING SPRING BOOT FAILURE...");
    System.err.println("Root cause: " + cause.getClass().getSimpleName());
    System.err.println("Message: " + cause.getMessage());

    // Check for common GraphQL issues
    if (cause.getMessage() != null) {
      if (cause.getMessage().contains("parameter name information not found")) {
        System.err.println("\n🔥 CLEAR ERROR: GraphQL parameter mapping failed!");
        System.err.println("📋 SOLUTION: Add @Argument annotations or enable -parameters flag");
        System.err.println("💡 Example: @Argument(\"groupId\") String groupId");
      }

      if (cause.getMessage().contains("ClassNotFoundException") &&
              cause.getMessage().contains("dataloader")) {
        System.err.println("\n🔥 CLEAR ERROR: DataLoader version conflict!");
        System.err.println("📋 SOLUTION: Remove explicit dataloader dependency");
      }

      if (cause.getMessage().contains("schema") &&
              cause.getMessage().contains("not found")) {
        System.err.println("\n🔥 CLEAR ERROR: GraphQL schema file missing!");
        System.err.println("📋 SOLUTION: Create src/main/resources/graphql/schema.graphqls");
      }
    }

    System.err.println("\n📁 Check your project structure:");
    System.err.println("  src/main/java/org/example/GraphQLServiceApplication.java");
    System.err.println("  src/main/resources/graphql/schema.graphqls");
    System.err.println("  src/main/resources/application.yml");
  }
}