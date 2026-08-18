package com.jaabir.backend.googlebooks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleBooksClient {
  
  private final RestTemplate restTemplate;
  private final String apiKey;
  private final String baseUrl;

  public GoogleBooksClient(RestTemplate restTemplate,
      @Value("${google.books.api.key}") String apiKey,
      @Value("${google.books.api.base-url}") String baseUrl) {
    
    this.restTemplate = restTemplate;
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
  }

  public GoogleBooksResponse search(String query, int page, int size) {
    String url = String.format(
            "%s/volumes?q=%s&startIndex=%d&maxResults=%d&key=%s",
            baseUrl, query, page * size, size, apiKey
        );

    return restTemplate.getForObject(url, GoogleBooksResponse.class);
  }

  public VolumeItem findByGoogleVolumeId(String googleVolumeId) {
    String url = String.format(
            "%s/volumes/%s?key=%s",
            baseUrl, googleVolumeId, apiKey);

    return restTemplate.getForObject(url, VolumeItem.class);
  }

}
