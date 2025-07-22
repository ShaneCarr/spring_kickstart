package com.graph.graphqlexample.graphql.dataloader;

import com.graph.graphqlexample.graphql.dataloader.HydrantGroupFeedDataLoaderKey;
import com.graph.graphqlexample.graphql.model.HydratedGroupFeedResponse;
import com.graph.graphqlexample.graphql.service.MockHydrantClient;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.BatchLoaderWithContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class HydrantGroupFeedDataLoader
        implements BatchLoaderWithContext<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> {

  private final MockHydrantClient mockHydrantClient;

  public HydrantGroupFeedDataLoader(MockHydrantClient mockHydrantClient) {
    this.mockHydrantClient = mockHydrantClient;
  }

  @Override
  public CompletionStage<List<HydratedGroupFeedResponse>> load(
          List<HydrantGroupFeedDataLoaderKey> keys,
          BatchLoaderEnvironment environment) {

    // You can extract context here later if needed
    // Object context = environment.getContext();

    return CompletableFuture.supplyAsync(() ->
            mockHydrantClient.fetchGroupFeeds(keys)
    );
  }
}