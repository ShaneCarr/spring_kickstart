package com.graph.graphqlexample.graphql.model;

import java.util.List;

public class GroupFeed {
  private List<Thread> threads;
  private List<Thread> pinnedThreads;

  public GroupFeed(List<Thread> threads, List<Thread> pinnedThreads) {
    this.threads = threads;
    this.pinnedThreads = pinnedThreads;
  }

  public List<Thread> getThreads() {
    return threads;
  }

  public List<Thread> getPinnedThreads() {
    return pinnedThreads;
  }
}