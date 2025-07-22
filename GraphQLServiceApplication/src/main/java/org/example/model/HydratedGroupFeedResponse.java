package org.example.model;

import org.example.dataloader.HydrantGroupFeedDataLoaderKey;

import java.util.List;
import java.util.Optional;

/**
 * Response object that can contain both regular threads and pinned threads
 * from a single Hydrant service call. This allows the DataLoader to batch
 * requests for both types of data and return appropriately filtered responses.
 */
public class HydratedGroupFeedResponse {
  private final Optional<GroupFeedThreadConnection> threadsConnection;
  private final Optional<List<Thread>> pinnedThreads;
  private final HydrantGroupFeedDataLoaderKey originalKey;

  public HydratedGroupFeedResponse(
          Optional<GroupFeedThreadConnection> threadsConnection,
          Optional<List<Thread>> pinnedThreads,
          HydrantGroupFeedDataLoaderKey originalKey) {
    this.threadsConnection = threadsConnection;
    this.pinnedThreads = pinnedThreads;
    this.originalKey = originalKey;
  }

  /**
   * Get the threads connection if it was requested and fetched
   */
  public Optional<GroupFeedThreadConnection> getThreadsConnection() {
    return threadsConnection;
  }

  /**
   * Get the pinned threads if they were requested and fetched
   */
  public Optional<List<Thread>> getPinnedThreads() {
    return pinnedThreads;
  }

  /**
   * Get the original key that was used to request this data
   */
  public HydrantGroupFeedDataLoaderKey getOriginalKey() {
    return originalKey;
  }

  /**
   * Check if this response satisfies the requirements of a given key.
   * This is used by the DataLoader to determine if a cached response
   * can be used to fulfill a request.
   */
  public boolean satisfies(HydrantGroupFeedDataLoaderKey key) {
    boolean hasThreadsIfNeeded = !key.shouldIncludeRegularThreads() || threadsConnection.isPresent();
    boolean hasPinnedIfNeeded = !key.shouldIncludePinnedThreads() || pinnedThreads.isPresent();
    return hasThreadsIfNeeded && hasPinnedIfNeeded;
  }

  @Override
  public String toString() {
    return String.format("HydratedGroupFeedResponse{threadsPresent=%s, pinnedPresent=%s, originalKey=%s}",
            threadsConnection.isPresent(),
            pinnedThreads.isPresent(),
            originalKey
    );
  }
}