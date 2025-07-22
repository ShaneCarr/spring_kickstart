package org.example.resolver;

import org.dataloader.DataLoader;
import org.example.dataloader.HydrantGroupFeedDataLoaderKey;
import org.example.model.GroupFeed;
import org.example.model.GroupFeedThreadConnection;
import org.example.model.HydratedGroupFeedResponse;
import org.example.model.options.HydrantOptions;
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