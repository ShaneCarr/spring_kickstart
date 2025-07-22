package org.example.config;

import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.example.dataloader.HydrantGroupFeedDataLoader;
import org.example.dataloader.HydrantGroupFeedDataLoaderKey;
import org.example.model.HydratedGroupFeedResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataLoaderRegistrar;

@Configuration
public class DataLoaderConfig {

  // Use a sensible name for the DataLoader registration
  public static final String GROUP_FEED_LOADER = "groupFeedLoader";

  @Bean
  public DataLoaderRegistrar dataLoaderRegistrar(HydrantGroupFeedDataLoader batchLoader) {
    return (registry, context) -> {
      DataLoaderOptions options = DataLoaderOptions.newOptions()
              .setBatchingEnabled(true)
              .setCachingEnabled(true)
              .setMaxBatchSize(100)
              .build();

      DataLoader<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> dataLoader =
              DataLoaderFactory.newDataLoader(batchLoader, options);

      // Register with a sensible name, not the type name
      registry.register(GROUP_FEED_LOADER, dataLoader);
    };
  }
}