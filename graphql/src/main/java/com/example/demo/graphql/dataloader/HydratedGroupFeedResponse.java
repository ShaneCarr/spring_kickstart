package com.example.demo.graphql.dataloader;

import com.example.demo.graphql.dataloader.HydrantGroupFeedDataLoaderKey; // This dependency will be resolved when DataLoaderKey is created

import java.util.List;
import java.util.Optional;

public record HydratedGroupFeedResponse(
        Optional<GroupFeedThreadConnection> threadsConnection,
        Optional<List<Thread>> pinnedThreads
) {
    // Your custom constructor if needed, otherwise default record constructor is fine
}