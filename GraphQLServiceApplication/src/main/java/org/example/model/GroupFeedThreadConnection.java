package org.example.model;

import java.util.List;

/**
 * GraphQL Connection type for paginated threads in a group feed.
 * This follows the Relay connection specification for pagination.
 */
public class GroupFeedThreadConnection {
  private final List<GroupFeedThreadEdge> edges;
  private final PageInfo pageInfo;
  private final int totalCount;

  public GroupFeedThreadConnection(List<GroupFeedThreadEdge> edges, PageInfo pageInfo, int totalCount) {
    this.edges = edges;
    this.pageInfo = pageInfo;
    this.totalCount = totalCount;
  }

  public List<GroupFeedThreadEdge> getEdges() {
    return edges;
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public int getTotalCount() {
    return totalCount;
  }

  /**
   * Edge type for the connection
   */
  public static class GroupFeedThreadEdge {
    private final Thread node;
    private final String cursor;

    public GroupFeedThreadEdge(Thread node, String cursor) {
      this.node = node;
      this.cursor = cursor;
    }

    public Thread getNode() {
      return node;
    }

    public String getCursor() {
      return cursor;
    }
  }

  /**
   * Page info for pagination
   */
  public static class PageInfo {
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;
    private final String startCursor;
    private final String endCursor;

    public PageInfo(boolean hasNextPage, boolean hasPreviousPage, String startCursor, String endCursor) {
      this.hasNextPage = hasNextPage;
      this.hasPreviousPage = hasPreviousPage;
      this.startCursor = startCursor;
      this.endCursor = endCursor;
    }

    public boolean isHasNextPage() {
      return hasNextPage;
    }

    public boolean isHasPreviousPage() {
      return hasPreviousPage;
    }

    public String getStartCursor() {
      return startCursor;
    }

    public String getEndCursor() {
      return endCursor;
    }
  }

  //  factory method for simple cases
  public static GroupFeedThreadConnection fromThreads(List<Thread> threads) {
    List<GroupFeedThreadEdge> edges = threads.stream()
            .map(thread -> new GroupFeedThreadEdge(thread, thread.getId()))
            .toList();

    PageInfo pageInfo = new PageInfo(
            false, // hasNextPage - simplified for now todo
            false, // hasPreviousPage
            edges.isEmpty() ? null : edges.get(0).getCursor(),
            edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor()
    );

    return new GroupFeedThreadConnection(edges, pageInfo, threads.size());
  }
}