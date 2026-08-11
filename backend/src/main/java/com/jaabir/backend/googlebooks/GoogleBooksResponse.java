package com.jaabir.backend.googlebooks;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksResponse {
  private List<VolumeItem> items;

  public List<VolumeItem> getItems() {
    return items;
  }

  public void setItems(List<VolumeItem> items) {
    this.items = items;
  }
}
