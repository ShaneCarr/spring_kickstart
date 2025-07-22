// src/main/java/com/example/demo/graphql/dataloader/DataLoaderConfig.java
package com.example.demo.graphql.dataloader;

import com.example.demo.graphql.model.HydratedGroupFeedResponse;
import com.graph.graphqlexample.graphql.dataloader.HydrantGroupFeedDataLoader;
import com.graph.graphqlexample.graphql.dataloader.HydrantGroupFeedDataLoaderKey;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataLoaderRegistrar;

@Configuration // <--- ESSENTIAL ANNOTATION
public class DataLoaderConfig {

    public DataLoaderConfig() {
        System.out.println(">> DEBUG: DataLoaderConfig constructor called!"); // Diagnostic print
    }

    @Bean // <--- This method provides the DataLoaderRegistrar bean
    public DataLoaderRegistrar groupFeedLoaderRegistrar(HydrantGroupFeedDataLoader batchLoader) {
        System.out.println(">> DEBUG: DataLoaderConfig @Bean method 'groupFeedLoaderRegistrar' called!"); // Diagnostic print

        // The DataLoaderRegistrar is a functional interface.
        // Its 'register' method will be called by Spring GraphQL for each request.
        return (DataLoaderRegistry registry, graphql.GraphQLContext context) -> {
            System.out.println(">> DEBUG: DataLoaderRegistrar lambda invoked to register 'groupFeedLoader' per-request.");

            DataLoader<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> loader =
                    DataLoaderFactory.newDataLoader(batchLoader); // Create DataLoader using your batchLoader
            registry.register("groupFeedLoader", loader); // <--- REGISTER THE DATALOADER WITH THE NAME
            System.out.println(">> DEBUG: 'groupFeedLoader' DataLoader registered successfully in registry.");
        };
    }
}