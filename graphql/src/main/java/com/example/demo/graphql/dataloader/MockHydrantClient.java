// src/main/java/com/example/demo/service/MockHydrantClient.java
package com.example.demo.graphql.dataloader;

import com.example.demo.graphql.dataloader.HydrantGroupFeedDataLoaderKey;
import com.example.demo.graphql.model.options.HydrantOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service // <--- ESSENTIAL ANNOTATION
public class MockHydrantClient {

    // Simulate some in-memory data for groups
    private final Map<String, List<Thread>> mockGroupData = new ConcurrentHashMap<>();

    public MockHydrantClient() {
        // Initialize some dummy data for "abc" group
        mockGroupData.put("abc", List.of(
                new Thread("thread-abc-1", "Regular Thread One", false),
                new Thread("thread-abc-2", "Pinned Thread Two", true),
                new Thread("thread-abc-3", "Regular Thread Three", false),
                new Thread("thread-abc-4", "Pinned Thread Four", true)
        ));
        mockGroupData.put("xyz", List.of(
                new Thread("thread-xyz-1", "XYZ Thread One", false),
                new Thread("thread-xyz-2", "XYZ Pinned Two", true)
        ));
    }


    public List<HydratedGroupFeedResponse> fetchGroupFeeds(List<HydrantGroupFeedDataLoaderKey> keys) {
        System.out.println(">> MockHydrantClient: Fetching group feeds for keys: " + keys);

        List<HydratedGroupFeedResponse> responses = new ArrayList<>();

        for (HydrantGroupFeedDataLoaderKey key : keys) {
            List<Thread> allThreadsForGroup = mockGroupData.getOrDefault(key.getGroupId(), List.of());

            List<Thread> regularThreads = new ArrayList<>();
            List<Thread> pinnedThreads = new ArrayList<>();

            if (key.shouldIncludeRegularThreads()) {
                regularThreads = allThreadsForGroup.stream()
                        .filter(t -> !t.pinned())
                        .collect(Collectors.toList());
                // Apply options if needed here for regular threads
            }

            if (key.shouldIncludePinnedThreads()) {
                pinnedThreads = allThreadsForGroup.stream()
                        .filter(Thread::pinned)
                        .collect(Collectors.toList());
                // Apply options if needed here for pinned threads
            }

            responses.add(new HydratedGroupFeedResponse(
                    Optional.of(GroupFeedThreadConnection.fromThreads(regularThreads)),
                    Optional.of(pinnedThreads)
            ));
        }
        return responses;
    }
}