package com.graph.graphqlexample.graphql.dataloader;



import com.graph.graphqlexample.graphql.model.options.HydrantOptions;

import java.util.Objects;

/**
 * Enhanced data loader key that supports batching threads and pinned threads together.
 * This key determines what data to fetch from Hydrant and enables intelligent batching
 * when multiple resolvers need data from the same group.
 */
public class HydrantGroupFeedDataLoaderKey {
  private final String networkId;
  private final String groupId;
  private final HydrantOptions options;
  private final boolean includePinnedThreads;
  private final boolean includeRegularThreads;

  private HydrantGroupFeedDataLoaderKey(Builder builder) {
    this.networkId = Objects.requireNonNull(builder.networkId, "networkId cannot be null");
    this.groupId = Objects.requireNonNull(builder.groupId, "groupId cannot be null");
    this.options = Objects.requireNonNull(builder.options, "options cannot be null");
    this.includePinnedThreads = builder.includePinnedThreads;
    this.includeRegularThreads = builder.includeRegularThreads;
  }

  // Factory methods for different use cases

  /**
   * Create a key for fetching only regular threads (for threads resolver)
   */
  public static HydrantGroupFeedDataLoaderKey forThreadsOnly(String networkId, String groupId, HydrantOptions options) {
    return new Builder(networkId, groupId, options)
            .includeRegularThreads(true)
            .includePinnedThreads(false)
            .build();
  }

  /**
   * Create a key for fetching only pinned threads (for pinnedThreads resolver)
   */
  public static HydrantGroupFeedDataLoaderKey forPinnedThreadsOnly(String networkId, String groupId, HydrantOptions options) {
    return new Builder(networkId, groupId, options)
            .includeRegularThreads(false)
            .includePinnedThreads(true)
            .build();
  }

  /**
   * Create a key for fetching both types (for batched requests)
   */
  public static HydrantGroupFeedDataLoaderKey forBoth(String networkId, String groupId, HydrantOptions options) {
    return new Builder(networkId, groupId, options)
            .includeRegularThreads(true)
            .includePinnedThreads(true)
            .build();
  }

  /**
   * Check if this key can be batched with another key.
   * Keys can be batched if they target the same group and have compatible options.
   */
  public boolean canBatchWith(HydrantGroupFeedDataLoaderKey other) {
    return Objects.equals(this.networkId, other.networkId)
            && Objects.equals(this.groupId, other.groupId)
            && this.options.isCompatibleForBatching(other.options);
  }

  /**
   * Merge two keys for batching - combines their requirements.
   * The result will include all data needed by both keys.
   */
  public HydrantGroupFeedDataLoaderKey mergeWith(HydrantGroupFeedDataLoaderKey other) {
    if (!canBatchWith(other)) {
      throw new IllegalArgumentException("Cannot batch incompatible keys: " + this + " and " + other);
    }

    return new Builder(this.networkId, this.groupId, this.options.merge(other.options))
            .includeRegularThreads(this.includeRegularThreads || other.includeRegularThreads)
            .includePinnedThreads(this.includePinnedThreads || other.includePinnedThreads)
            .build();
  }

  // Getters

  public String getNetworkId() {
    return networkId;
  }

  public String getGroupId() {
    return groupId;
  }

  public HydrantOptions getOptions() {
    return options;
  }

  public boolean shouldIncludePinnedThreads() {
    return includePinnedThreads;
  }

  public boolean shouldIncludeRegularThreads() {
    return includeRegularThreads;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HydrantGroupFeedDataLoaderKey that = (HydrantGroupFeedDataLoaderKey) o;
    return includePinnedThreads == that.includePinnedThreads &&
            includeRegularThreads == that.includeRegularThreads &&
            Objects.equals(networkId, that.networkId) &&
            Objects.equals(groupId, that.groupId) &&
            Objects.equals(options, that.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkId, groupId, options, includePinnedThreads, includeRegularThreads);
  }

  @Override
  public String toString() {
    return String.format("HydrantGroupFeedDataLoaderKey{networkId='%s', groupId='%s', regularThreads=%s, pinnedThreads=%s}",
            networkId, groupId, includeRegularThreads, includePinnedThreads);
  }

  /**
   * Builder for creating HydrantGroupFeedDataLoaderKey instances
   */
  public static class Builder {
    private final String networkId;
    private final String groupId;
    private final HydrantOptions options;
    private boolean includePinnedThreads = false;
    private boolean includeRegularThreads = false;

    public Builder(String networkId, String groupId, HydrantOptions options) {
      this.networkId = networkId;
      this.groupId = groupId;
      this.options = options;
    }

    public Builder includePinnedThreads(boolean include) {
      this.includePinnedThreads = include;
      return this;
    }

    public Builder includeRegularThreads(boolean include) {
      this.includeRegularThreads = include;
      return this;
    }

    public HydrantGroupFeedDataLoaderKey build() {
      if (!includePinnedThreads && !includeRegularThreads) {
        throw new IllegalArgumentException("Must include at least one type of threads");
      }
      return new HydrantGroupFeedDataLoaderKey(this);
    }
  }
}