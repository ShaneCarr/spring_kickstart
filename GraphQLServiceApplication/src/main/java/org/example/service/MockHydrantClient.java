package org.example.service;

import org.example.dataloader.HydrantGroupFeedDataLoaderKey;
import org.example.model.GroupFeedThreadConnection;
import org.example.model.HydratedGroupFeedResponse;
import org.example.model.Thread;
import org.example.model.options.HydrantOptions;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MockHydrantClient {

  public List<HydratedGroupFeedResponse> fetchGroupFeeds(List<HydrantGroupFeedDataLoaderKey> keys) {
    // Step 1: Group by deduplicated merged keys
    Map<HydrantGroupFeedDataLoaderKey, List<HydrantGroupFeedDataLoaderKey>> mergedGroups = new HashMap<>();

    for (HydrantGroupFeedDataLoaderKey key : keys) {
      Optional<HydrantGroupFeedDataLoaderKey> existing = mergedGroups.keySet().stream()
              .filter(k -> k.canBatchWith(key))
              .findFirst();

      if (existing.isPresent()) {
        var merged = existing.get().mergeWith(key);
        List<HydrantGroupFeedDataLoaderKey> originalKeys = mergedGroups.remove(existing.get());
        originalKeys.add(key);
        mergedGroups.put(merged, originalKeys);
      } else {
        mergedGroups.put(key, new ArrayList<>(List.of(key)));
      }
    }

    List<HydratedGroupFeedResponse> responses = new ArrayList<>();

    for (var entry : mergedGroups.entrySet()) {
      HydrantGroupFeedDataLoaderKey mergedKey = entry.getKey();
      List<HydrantGroupFeedDataLoaderKey> originalKeys = entry.getValue();

      // Simulate URI building
      String uri = buildUriFromKey(mergedKey);
      String body = buildBodyFromKey(mergedKey);

      System.out.println("MOCK HYDRANT CALL");
      System.out.println("POST " + uri);
      System.out.println("BODY: " + body);
      System.out.println();

      for (HydrantGroupFeedDataLoaderKey key : originalKeys) {
        HydrantOptions opts = key.getOptions();

        List<Thread> threads = key.shouldIncludeRegularThreads()
                ? generateMockThreads(key.getGroupId(), false, opts)
                : null;

        List<Thread> pinned = key.shouldIncludePinnedThreads()
                ? generateMockThreads(key.getGroupId(), true, opts)
                : null;

        Optional<GroupFeedThreadConnection> threadsConnection = Optional.ofNullable(threads)
                .map(GroupFeedThreadConnection::fromThreads);

        Optional<List<Thread>> pinnedThreads = Optional.ofNullable(pinned);

        responses.add(new HydratedGroupFeedResponse(threadsConnection, pinnedThreads, key));
      }
    }

    return responses;
  }

  private List<Thread> generateMockThreads(String groupId, boolean pinned, HydrantOptions options) {
    List<Thread> all = new ArrayList<>();
    String prefix = pinned ? "p" : "t";

    for (int i = 1; i <= 10; i++) {
      String id = groupId + "-" + prefix + i;
      all.add(new Thread(id, (pinned ? "Pinned" : "Post") + " " + i, pinned));
    }

    String gt = options.getGreaterThan().orElse(null);
    String lt = options.getLessThan().orElse(null);

    List<Thread> filtered = all.stream()
            .filter(t -> gt == null || t.getId().compareTo(gt) > 0)
            .filter(t -> lt == null || t.getId().compareTo(lt) < 0)
            .collect(Collectors.toList());

    int limit = options.getThreadsCount().orElse(10);
    return filtered.stream().limit(limit).collect(Collectors.toList());
  }

  private String buildUriFromKey(HydrantGroupFeedDataLoaderKey key) {
    StringBuilder builder = new StringBuilder();
    builder.append("/api/v3/nested/networks/").append(encode(key.getNetworkId()));
    builder.append("/feeds/group/").append(encode(key.getGroupId()));

    HydrantOptions opts = key.getOptions();
    opts.getGreaterThan().ifPresent(gt -> builder.append("?greater_than=").append(encode(gt)));
    opts.getLessThan().ifPresent(lt -> builder.append("&less_than=").append(encode(lt)));
    opts.getThreadsCount().ifPresent(tc -> builder.append("&threads_count=").append(tc));

    return builder.toString();
  }

  private String buildBodyFromKey(HydrantGroupFeedDataLoaderKey key) {
    // Fake body serialization of FeedAttributes
    return "{\"attributes\":\"mock-serialized-attrs\"}";
  }

  private String encode(String input) {
    return URLEncoder.encode(input, StandardCharsets.UTF_8);
  }
}