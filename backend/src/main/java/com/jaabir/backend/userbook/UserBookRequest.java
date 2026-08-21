package com.jaabir.backend.userbook;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UserBookRequest {

  @NotBlank(message = "Google Volume ID is required")
  private String googleVolumeId;

  private ReadingStatus status;

  @Min(value = 1, message = "Rating must be at least 1")
  @Max(value = 5, message = "Rating must be at most 5")
  private Integer rating;

  private String notes;

  public String getGoogleVolumeId() { return googleVolumeId; }
  public void setGoogleVolumeId(String googleVolumeId) { this.googleVolumeId = googleVolumeId; }

  public ReadingStatus getStatus() { return status; }
  public void setStatus(ReadingStatus status) { this.status = status; }

  public Integer getRating() { return rating; }
  public void setRating(Integer rating) { this.rating = rating; }

  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
}