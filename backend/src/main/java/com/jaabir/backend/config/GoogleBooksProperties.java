package com.jaabir.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.books.api")
public class GoogleBooksProperties {

  private String key;
  private String baseUrl;

  public String getKey() { return key; }
  public void setKey(String key) { this.key = key; }

  public String getBaseUrl() { return baseUrl; }
  public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
}