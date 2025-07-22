// src/main/java/com/example/demo/graphql/dataloader/HydrantGroupFeedDataLoaderKey.java
package com.example.demo.graphql.dataloader;

import com.example.demo.graphql.model.options.HydrantOptions;

import java.util.Objects;
import java.util.Optional;

public class HydrantGroupFeedDataLoaderKey {
    private final String networkId;
    private final String groupId;
    private final HydrantOptions options;
    private final boolean includeRegularThreads;
    private final boolean includePinnedThreads;

    private HydrantGroupFeedDataLoaderKey(String networkId, String groupId, HydrantOptions options, boolean includeRegularThreads, boolean includePinnedThreads) {
        this.networkId = networkId;
        this.groupId = groupId;
        this.options = options;
        this.includeRegularThreads = includeRegularThreads;
        this.includePinnedThreads = includePinnedThreads;
    }

    public static HydrantGroupFeedDataLoaderKey forBoth(String networkId, String groupId, HydrantOptions options) {
        return new HydrantGroupFeedDataLoaderKey(networkId, groupId, options, true, true);
    }

    public static HydrantGroupFeedDataLoaderKey forRegularThreads(String networkId, String groupId, HydrantOptions options) {
        return new HydrantGroupFeedDataLoaderKey(networkId, groupId, options, true, false);
    }

    public static HydrantGroupFeedDataLoaderKey forPinnedThreads(String networkId, String groupId, HydrantOptions options) {
        return new HydrantGroupFeedDataLoaderKey(networkId, groupId, options, false, true);
    }

    public String getNetworkId() { return networkId; }
    public String getGroupId() { return groupId; }
    public HydrantOptions getOptions() { return options; }
    public boolean shouldIncludeRegularThreads() { return includeRegularThreads; }
    public boolean shouldIncludePinnedThreads() { return includePinnedThreads; }

    public boolean canBatchWith(HydrantGroupFeedDataLoaderKey other) {
        // Simplified batching logic: can batch if networkId and groupId are the same
        // and options are compatible for merging (e.g., no conflicting filters)
        return this.networkId.equals(other.networkId) &&
                this.groupId.equals(other.groupId) &&
                this.options.equals(other.options); // More complex merge logic might be needed
    }

    public HydrantGroupFeedDataLoaderKey mergeWith(HydrantGroupFeedDataLoaderKey other) {
        // Merges this key with another for batching purposes.
        // If they can batch, combine their inclusion flags.
        return new HydrantGroupFeedDataLoaderKey(
                this.networkId,
                this.groupId,
                this.options,
                this.includeRegularThreads || other.includeRegularThreads,
                this.includePinnedThreads || other.includePinnedThreads
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HydrantGroupFeedDataLoaderKey that = (HydrantGroupFeedDataLoaderKey) o;
        return includeRegularThreads == that.includeRegularThreads &&
                includePinnedThreads == that.includePinnedThreads &&
                networkId.equals(that.networkId) &&
                groupId.equals(that.groupId) &&
                options.equals(that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId, groupId, options, includeRegularThreads, includePinnedThreads);
    }

    @Override
    public String toString() {
        return "HydrantGroupFeedDataLoaderKey{" +
                "networkId='" + networkId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", options=" + options +
                ", includeRegularThreads=" + includeRegularThreads +
                ", includePinnedThreads=" + includePinnedThreads +
                '}';
    }
}