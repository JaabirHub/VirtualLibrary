package com.jaabir.backend.googlebooks;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {
  private String title;
  private List<String> authors;
  private String publisher;
  private String publishedDate;
  private String description;
  private Integer pageCount;
  private List<String> categories;
  private String language;
  private String infoLink;
  private ImageLinks imageLinks;

  public String getTitle() {return title; }
  public void setTitle(String title) { this.title = title; }

  public List<String> getAuthors() { return authors; }
  public void setAuthors(List<String> authors) { this.authors = authors; }

  public String getPublisher() { return publisher; }
  public void setPublisher(String publisher) { this.publisher = publisher; }

  public String getPublishedDate() { return publishedDate; }
  public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public Integer getPageCount() { return pageCount; }
  public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }

  public List<String> getCategories() { return categories; }
  public void setCategories(List<String> categories) { this.categories = categories; }

  public String getLanguage() { return language; }
  public void setLanguage(String language) { this.language = language; }

  public String getInfoLink() { return infoLink; }
  public void setInfoLink(String infoLink) { this.infoLink = infoLink; }

  public ImageLinks getImageLinks() { return imageLinks; }
  public void setImageLinks(ImageLinks imageLinks) { this.imageLinks = imageLinks; }
}