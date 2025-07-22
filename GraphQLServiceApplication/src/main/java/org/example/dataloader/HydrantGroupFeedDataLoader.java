package org.example.dataloader;

import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.BatchLoaderWithContext;
import org.example.model.HydratedGroupFeedResponse;
import org.example.service.MockHydrantClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
@Component
public class HydrantGroupFeedDataLoader{
  private final MockHydrantClient mockHydrantClient;

  public HydrantGroupFeedDataLoader(MockHydrantClient mockHydrantClient) {
    this.mockHydrantClient = mockHydrantClient;
  }

  public Flux<HydratedGroupFeedResponse> load(List<HydrantGroupFeedDataLoaderKey> keys,
                                              BatchLoaderEnvironment env) {
    return Flux.fromIterable(mockHydrantClient.fetchGroupFeeds(keys));
  }
}
