package org.example.config;

import org.example.dataloader.HydrantGroupFeedDataLoaderKey;
import org.example.model.HydratedGroupFeedResponse;
import org.example.dataloader.HydrantGroupFeedDataLoader;
import org.example.service.MockHydrantClient;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Configuration
public class DataLoaderConfig implements DataLoaderRegistrar {

  @Override
  public void registerDataLoaders(DataLoaderRegistry registry) {
    MappedBatchLoader<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> batchLoader =
            keys -> {
              // Your batching logic here, return CompletableFuture<Map>
              return CompletableFuture.supplyAsync(() -> {
                Map<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> result = ...;
                return result;
              });
            };

    registry.register("groupFeedLoader", DataLoader.newMappedDataLoader(batchLoader));
  }
}

//@Configuration
//public class DataLoaderConfig {
//
//  // Use a sensible name for the DataLoader registration
//  public static final String GROUP_FEED_LOADER = "groupFeedLoader";
//
//  @Bean
//  public DataLoaderRegistrar dataLoaderRegistrar(HydrantGroupFeedDataLoader batchLoader) {
//    return (registry, context) -> {
//      DataLoaderOptions options = DataLoaderOptions.newOptions()
//              .setBatchingEnabled(true)
//              .setCachingEnabled(true)
//              .setMaxBatchSize(100)
//              .build();
//
//      DataLoader<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> dataLoader =
//              DataLoaderFactory.newDataLoader(batchLoader, options);
//
//      // Register with a sensible name, not the type name
//      registry.register(GROUP_FEED_LOADER, dataLoader);
//    };
//  }
//}