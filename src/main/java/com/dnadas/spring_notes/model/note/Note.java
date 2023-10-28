package com.dnadas.spring_notes.model.note;

import java.time.LocalDateTime;
import java.util.Objects;

public class Note {
  private final Long id;
  private String title;
  private String content;
  private final LocalDateTime createdAt;

  public Note(Long id, String title, String content, LocalDateTime createdAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Note note = (Note) o;
    return Objects.equals(id, note.id) && Objects.equals(title, note.title) &&
      Objects.equals(content, note.content) && Objects.equals(
      createdAt, note.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, createdAt);
  }

  @Override
  public String toString() {
    return "Note{" +
      "id=" + id +
      ", title='" + title + '\'' +
      ", content='" + content + '\'' +
      ", createdAt=" + createdAt +
      '}';
  }
}
