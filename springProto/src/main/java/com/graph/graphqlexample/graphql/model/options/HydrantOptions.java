package com.graph.graphqlexample.graphql.model.options;

import java.util.Objects;
import java.util.Optional;

/**
 * Low-level options for Hydrant service requests.
 * Contains thread counts, filtering, and hydration flags.
 */
public class HydrantOptions {
  private final Optional<Integer> threadsCount;
  private final boolean includePinnedThreads;
  private final Optional<String> greaterThan;
  private final Optional<String> lessThan;

  private HydrantOptions(Builder builder) {
    this.threadsCount = Optional.ofNullable(builder.threadsCount);
    this.includePinnedThreads = builder.includePinnedThreads;
    this.greaterThan = Optional.ofNullable(builder.greaterThan);
    this.lessThan = Optional.ofNullable(builder.lessThan);
  }

  public Optional<Integer> getThreadsCount() {
    return threadsCount;
  }

  public boolean shouldIncludePinnedThreads() {
    return includePinnedThreads;
  }

  public Optional<String> getGreaterThan() {
    return greaterThan;
  }

  public Optional<String> getLessThan() {
    return lessThan;
  }

  /**
   * Check if these options can be batched with other options.
   * Requests can be batched if they target the same page (same pagination cursors).
   * Pinned threads are always fetched on the first page and then ignored in
   * subsequent paginated requests.
   */
  public boolean isCompatibleForBatching(HydrantOptions other) {
    // For batching, we need compatible pagination parameters
    // For simplicity, allow batching if pagination cursors are the same or absent
    return Objects.equals(this.greaterThan, other.greaterThan) &&
            Objects.equals(this.lessThan, other.lessThan);
  }

  /**
   * Merge two options for batching.
   * Takes the maximum threads count and combines boolean flags.
   */
  public HydrantOptions merge(HydrantOptions other) {
    if (!isCompatibleForBatching(other)) {
      throw new IllegalArgumentException("Cannot merge incompatible HydrantOptions");
    }

    // Take the maximum threads count
    Integer mergedThreadsCount = null;
    if (this.threadsCount.isPresent() && other.threadsCount.isPresent()) {
      mergedThreadsCount = Math.max(this.threadsCount.get(), other.threadsCount.get());
    } else if (this.threadsCount.isPresent()) {
      mergedThreadsCount = this.threadsCount.get();
    } else if (other.threadsCount.isPresent()) {
      mergedThreadsCount = other.threadsCount.get();
    }

    return builder()
            .threadsCount(mergedThreadsCount)
            .includePinnedThreads(this.includePinnedThreads || other.includePinnedThreads)
            .greaterThan(this.greaterThan.orElse(null))
            .lessThan(this.lessThan.orElse(null))
            .build();
  }

  public static HydrantOptions defaultOptions() {
    return builder().build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
            .threadsCount(threadsCount.orElse(null))
            .includePinnedThreads(includePinnedThreads)
            .greaterThan(greaterThan.orElse(null))
            .lessThan(lessThan.orElse(null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HydrantOptions that = (HydrantOptions) o;
    return includePinnedThreads == that.includePinnedThreads &&
            Objects.equals(threadsCount, that.threadsCount) &&
            Objects.equals(greaterThan, that.greaterThan) &&
            Objects.equals(lessThan, that.lessThan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(threadsCount, includePinnedThreads, greaterThan, lessThan);
  }

  @Override
  public String toString() {
    return String.format("HydrantOptions{threadsCount=%s, includePinned=%s, greaterThan=%s, lessThan=%s}",
            threadsCount.orElse(null), includePinnedThreads,
            greaterThan.orElse(null), lessThan.orElse(null));
  }

  /**
   * Builder for HydrantOptions
   */
  public static class Builder {
    private Integer threadsCount;
    private boolean includePinnedThreads = false;
    private String greaterThan;
    private String lessThan;

    public Builder threadsCount(Integer threadsCount) {
      this.threadsCount = threadsCount;
      return this;
    }

    public Builder includePinnedThreads(boolean include) {
      this.includePinnedThreads = include;
      return this;
    }

    public Builder greaterThan(String cursor) {
      this.greaterThan = cursor;
      return this;
    }

    public Builder lessThan(String cursor) {
      this.lessThan = cursor;
      return this;
    }

    public HydrantOptions build() {
      return new HydrantOptions(this);
    }
  }
}