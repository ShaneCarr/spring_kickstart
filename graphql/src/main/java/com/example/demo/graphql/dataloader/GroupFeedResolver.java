// src/main/java/com/example/demo/graphql/GroupFeedResolver.java
package com.example.demo.graphql.dataloader;

import com.example.demo.graphql.dataloader.HydrantGroupFeedDataLoaderKey;
import com.example.demo.graphql.model.options.HydrantOptions;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller // <--- ESSENTIAL ANNOTATION
public class GroupFeedResolver {

    @QueryMapping
    public CompletableFuture<GroupFeed> groupFeed(
            @Argument String groupId,
            // The name 'groupFeedLoader' MUST match the name used in DataLoaderConfig.registry.register()
            DataLoader<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> groupFeedLoader
    ) {
        System.out.println(">> DEBUG: GroupFeedResolver.groupFeed called for groupId: " + groupId); // Diagnostic print

        HydrantGroupFeedDataLoaderKey key = HydrantGroupFeedDataLoaderKey.forBoth(
                "networkId", groupId, HydrantOptions.defaultOptions()
        );

        // Use the DataLoader to fetch the data
        return groupFeedLoader.load(key).thenApply(response -> {
            System.out.println(">> DEBUG: DataLoader returned response: " + response); // Diagnostic print
            return new GroupFeed(
                    response.threadsConnection()
                            .map(GroupFeedThreadConnection::getEdges)
                            .orElse(List.of())
                            .stream()
                            .map(GroupFeedThreadConnection.GroupFeedThreadEdge::node)
                            .toList(),
                    response.pinnedThreads().orElse(List.of())
            );
        });
    }
}