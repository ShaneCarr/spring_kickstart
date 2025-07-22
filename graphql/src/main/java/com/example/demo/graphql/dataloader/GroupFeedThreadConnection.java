package com.example.demo.graphql.dataloader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroupFeedThreadConnection {
    private final List<GroupFeedThreadEdge> edges;

    public GroupFeedThreadConnection(List<GroupFeedThreadEdge> edges) {
        this.edges = edges;
    }

    public List<GroupFeedThreadEdge> getEdges() {
        return edges;
    }

    public static GroupFeedThreadConnection fromThreads(List<com.example.demo.graphql.dataloader.Thread> threads) {
        List<GroupFeedThreadEdge> edges = threads.stream()
                .map(thread -> new GroupFeedThreadEdge(thread, "cursor-" + thread.id()))
                .collect(Collectors.toList());
        return new GroupFeedThreadConnection(edges);
    }

    public record GroupFeedThreadEdge(Thread node, String cursor) {}
}