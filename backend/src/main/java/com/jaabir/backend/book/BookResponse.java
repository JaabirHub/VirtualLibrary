package com.jaabir.backend.book;

import java.util.List;
import java.util.UUID;

import com.jaabir.backend.googlebooks.VolumeItem;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public class BookResponse {
  private UUID id;
  private String googleVolumeId;
  private String title;
  private List<String> authors;
  private String publisher;
  private String publishedDate;
  private String description;
  private String thumbnailUrl;
  private Integer pageCount;
  private List<String> categories;
  private String language;
  private String infoLink;

  private final static JsonMapper jsonMapper = JsonMapper.builder().build();

  public BookResponse(Book book) {
    this.id = book.getId();
    this.googleVolumeId = book.getGoogleVolumeId();
    this.title = book.getTitle();
    this.publisher = book.getPublisher();
    this.publishedDate = book.getPublishedDate();
    this.description = book.getDescription();
    this.thumbnailUrl = book.getThumbnailUrl();
    this.pageCount = book.getPageCount();
    this.language = book.getLanguage();
    this.infoLink = book.getInfoLink();

    try {
      if (book.getAuthors() != null) {
          this.authors = jsonMapper.readValue(
                  book.getAuthors(),
                  jsonMapper.getTypeFactory()
                          .constructCollectionType(List.class, String.class)
          );
      }

      if (book.getCategories() != null) {
          this.categories = jsonMapper.readValue(
            book.getCategories(),
            jsonMapper.getTypeFactory()
            .constructCollectionType(List.class, String.class)
          );
      }
      } catch (JacksonException e) {
          this.authors = List.of();
          this.categories = List.of();
      }
  }

  public BookResponse(VolumeItem item) {
    this.googleVolumeId = item.getId();
    this.title = item.getVolumeInfo().getTitle();
    this.authors = item.getVolumeInfo().getAuthors();
    this.publisher = item.getVolumeInfo().getPublisher();
    this.publishedDate = item.getVolumeInfo().getPublishedDate();
    this.description = item.getVolumeInfo().getDescription();
    this.pageCount = item.getVolumeInfo().getPageCount();
    this.categories = item.getVolumeInfo().getCategories();
    this.language = item.getVolumeInfo().getLanguage();
    this.infoLink = item.getVolumeInfo().getInfoLink();
    this.thumbnailUrl = item.getVolumeInfo().getImageLinks() != null
        ? item.getVolumeInfo().getImageLinks().getThumbnail()
        : null;
  }

    public UUID getId() { return id; }
    public String getGoogleVolumeId() { return googleVolumeId; }
    public String getTitle() { return title; }
    public List<String> getAuthors() { return authors; }
    public String getPublisher() { return publisher; }
    public String getPublishedDate() { return publishedDate; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public Integer getPageCount() { return pageCount; }
    public List<String> getCategories() { return categories; }
    public String getLanguage() { return language; }
    public String getInfoLink() { return infoLink; }
}