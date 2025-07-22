package com.example.demo.graphql.dataloader;

import java.util.List;
import java.util.Optional;

public record GroupFeed(List<Thread> threads, List<Thread> pinnedThreads) { }