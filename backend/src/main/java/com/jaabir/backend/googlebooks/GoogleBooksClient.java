package com.jaabir.backend.googlebooks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
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

  @Cacheable(
      cacheNames = "googleBooksSearch",
      key = "T(java.lang.String).format('%s:%d:%d', #query != null ? #query.trim().toLowerCase() : '', #page, #size)"
  )
  public GoogleBooksResponse search(String query, int page, int size) {
    String url = UriComponentsBuilder
        .fromUriString(baseUrl + "/volumes")
        .queryParam("q", query)
        .queryParam("startIndex", page * size)
        .queryParam("maxResults", size)
        .queryParam("key", apiKey)
        .build()
        .toUriString();

    return restTemplate.getForObject(url, GoogleBooksResponse.class);
}

  @Cacheable(
      cacheNames = "googleBookById",
      key = "#googleVolumeId"
  )
  public VolumeItem findByGoogleVolumeId(String googleVolumeId) {
    String url = UriComponentsBuilder
        .fromUriString(baseUrl + "/volumes/" + googleVolumeId)
        .queryParam("key", apiKey)
        .build()
        .toUriString();

    return restTemplate.getForObject(url, VolumeItem.class);
  }
}
