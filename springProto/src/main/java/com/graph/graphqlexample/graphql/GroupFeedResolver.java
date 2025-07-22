package com.graph.graphqlexample.graphql;


import com.graph.graphqlexample.graphql.dataloader.HydrantGroupFeedDataLoaderKey;
import com.graph.graphqlexample.graphql.model.GroupFeed;
import com.graph.graphqlexample.graphql.model.GroupFeedThreadConnection;
import com.graph.graphqlexample.graphql.model.HydratedGroupFeedResponse;
import com.graph.graphqlexample.graphql.model.options.HydrantOptions;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class GroupFeedResolver {

    @QueryMapping
    public CompletableFuture<GroupFeed> groupFeed(
            @Argument String groupId,
            DataLoader<HydrantGroupFeedDataLoaderKey, HydratedGroupFeedResponse> groupFeedLoader
    ) {
        HydrantGroupFeedDataLoaderKey key = HydrantGroupFeedDataLoaderKey.forBoth(
                "networkId", groupId, HydrantOptions.defaultOptions()
        );

        return groupFeedLoader.load(key).thenApply(response ->
                new GroupFeed(
                        response.getThreadsConnection()
                                .map(GroupFeedThreadConnection::getEdges)
                                .orElse(List.of())
                                .stream()
                                .map(GroupFeedThreadConnection.GroupFeedThreadEdge::getNode)
                                .toList(),
                        response.getPinnedThreads().orElse(List.of())
                )
        );
    }
}