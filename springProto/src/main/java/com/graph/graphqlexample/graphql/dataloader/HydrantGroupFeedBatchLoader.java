package com.graph.graphqlexample.graphql.dataloader;//package org.example.dataloader;
//
//import org.dataloader.BatchLoader;
//import org.dataloader.BatchLoaderEnvironment;
//import org.dataloader.BatchLoaderWithContext;
//import org.example.dataloader.HydrantGroupFeedDataLoaderKey;
//import org.example.model.HydratedGroupFeedResponse;
//import org.example.service.MockHydrantClient;
//import org.springframework.stereotype.Component;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.CompletionStage;
//
//@Component
//public class HydrantGroupFeedDataLoader
//        implements BatchLoaderWithContext<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> {
//
//  private final MockHydrantClient mockHydrantClient;
//
//  public HydrantGroupFeedDataLoader(MockHydrantClient mockHydrantClient) {
//    this.mockHydrantClient = mockHydrantClient;
//  }
//
//  @Override
//  public CompletionStage<List<HydratedGroupFeedResponse>> load(
//          List<HydrantGroupFeedDataLoaderKey> keys,
//          BatchLoaderEnvironment environment) {
//
//    return CompletableFuture.supplyAsync(() -> mockHydrantClient.fetchGroupFeeds(keys));
//  }
//}
//}