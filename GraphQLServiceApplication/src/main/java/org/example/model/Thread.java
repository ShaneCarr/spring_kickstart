package org.example.model;

public class Thread {
  private String id;
  private String title;
  private boolean pinned;

  public Thread(String id, String title, boolean pinned) {
    this.id = id;
    this.title = title;
    this.pinned = pinned;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public boolean isPinned() {
    return pinned;
  }
}