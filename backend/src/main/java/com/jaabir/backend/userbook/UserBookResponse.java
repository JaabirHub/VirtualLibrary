package com.jaabir.backend.userbook;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jaabir.backend.book.BookResponse;

public class UserBookResponse {

  private final UUID id;
  private final BookResponse book;
  private final ReadingStatus status;
  private final Integer rating;
  private final String notes;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public UserBookResponse(UserBook userBook) {
    this.id = userBook.getId();
    this.book = new BookResponse(userBook.getBook());
    this.status = userBook.getStatus();
    this.rating = userBook.getRating();
    this.notes = userBook.getNotes();
    this.createdAt = userBook.getCreatedAt();
    this.updatedAt = userBook.getUpdatedAt();
  }

  public UUID getId() { return id; }
  public BookResponse getBook() { return book; }
  public ReadingStatus getStatus() { return status; }
  public Integer getRating() { return rating; }
  public String getNotes() { return notes; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
}