package com.jaabir.backend.book;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jaabir.backend.googlebooks.GoogleBooksClient;
import com.jaabir.backend.googlebooks.GoogleBooksResponse;
import com.jaabir.backend.googlebooks.VolumeItem;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
public class BookService {
  
  private final BookRepository bookRepository;
  private final GoogleBooksClient googleBooksClient;
  private final JsonMapper jsonMapper;

  public BookService(BookRepository bookRepository,
      GoogleBooksClient googleBooksClient,
      JsonMapper jsonMapper) {
    
    this.bookRepository = bookRepository;
    this.googleBooksClient = googleBooksClient;
    this.jsonMapper = jsonMapper;
  }

  public List<BookResponse> search(String query, int page, int size) {
    GoogleBooksResponse response = googleBooksClient.search(query, page, size);

    if (response == null || response.getItems() == null) {
      return List.of();
    }

    return response.getItems().stream()
          .map(BookResponse::new)
          .toList();
  }

  public Book findOrCacheBook(String googleVolumeId) {
    return bookRepository.findByGoogleVolumeId(googleVolumeId)
        .orElseGet(() -> {
          VolumeItem item = googleBooksClient.findByGoogleVolumeId(googleVolumeId);

          if (item == null || item.getVolumeInfo() == null) {
            throw new IllegalArgumentException("Book not found: " + googleVolumeId);
          }

          return bookRepository.save(mapToBook(item));
        });
  }

  private Book mapToBook(VolumeItem item) {
    Book book = new Book();
    book.setGoogleVolumeId(item.getId());
    book.setTitle(item.getVolumeInfo().getTitle());
    book.setPublisher(item.getVolumeInfo().getPublisher());
    book.setPublishedDate(item.getVolumeInfo().getPublishedDate());
    book.setDescription(item.getVolumeInfo().getDescription());
    book.setPageCount(item.getVolumeInfo().getPageCount());
    book.setLanguage(item.getVolumeInfo().getLanguage());
    book.setInfoLink(item.getVolumeInfo().getInfoLink());
    book.setThumbnailUrl(item.getVolumeInfo().getImageLinks() != null
            ? item.getVolumeInfo().getImageLinks().getThumbnail()
            : null);

    try {
      if (item.getVolumeInfo().getAuthors() != null) {
        book.setAuthors(jsonMapper.writeValueAsString(
        item.getVolumeInfo().getAuthors()));
      }
      if (item.getVolumeInfo().getCategories() != null) {
        book.setCategories(jsonMapper.writeValueAsString(
        item.getVolumeInfo().getCategories()));
      }
    } catch (JacksonException e) {
        book.setAuthors("[]");
        book.setCategories("[]");
    }

    return book;
  }
}
