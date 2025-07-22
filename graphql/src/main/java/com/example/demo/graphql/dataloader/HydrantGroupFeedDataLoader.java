// src/main/java/com/example/demo/graphql/dataloader/HydrantGroupFeedDataLoader.java
package com.example.demo.graphql.dataloader;

import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.BatchLoaderWithContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component // <--- ESSENTIAL ANNOTATION
public class HydrantGroupFeedDataLoader
        implements BatchLoaderWithContext<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> {

    private final MockHydrantClient mockHydrantClient;

    public HydrantGroupFeedDataLoader(MockHydrantClient mockHydrantClient) {
        this.mockHydrantClient = mockHydrantClient;
        System.out.println(">> DEBUG: HydrantGroupFeedDataLoader constructor called!"); // Diagnostic print
    }

    @Override
    public CompletionStage<List<HydratedGroupFeedResponse>> load(
            List<HydrantGroupFeedDataLoaderKey> keys,
            BatchLoaderEnvironment environment) {

        System.out.println(">> DEBUG: HydrantGroupFeedDataLoader load method called with keys: " + keys); // Diagnostic print

        // In a real scenario, you'd typically pass all keys to the client to fetch in one go
        // and then map the results back to the original keys.
        // For simplicity with MockHydrantClient that already takes a list:
        return CompletableFuture.supplyAsync(() ->
                mockHydrantClient.fetchGroupFeeds(keys)
        );
    }
}