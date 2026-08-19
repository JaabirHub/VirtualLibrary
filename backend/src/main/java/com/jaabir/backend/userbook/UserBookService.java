package com.jaabir.backend.userbook;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jaabir.backend.book.Book;
import com.jaabir.backend.book.BookService;
import com.jaabir.backend.user.User;

@Service
public class UserBookService {

  private final UserBookRepository userBookRepository;
  private final BookService bookService;

  public UserBookService(UserBookRepository userBookRepository,
          BookService bookService) {
    this.userBookRepository = userBookRepository;
    this.bookService = bookService;
  }

  public List<UserBookResponse> getLibrary(User user) {
    return userBookRepository.findByUser(user)
        .stream()
        .map(UserBookResponse::new)
        .toList();
  }

  public UserBookResponse addBook(User user, UserBookRequest request) {
    if (userBookRepository.existsByUserAndBookGoogleVolumeId(user, request.getGoogleVolumeId())) {
      throw new IllegalArgumentException("Book already in library");
    }

    Book book = bookService.findOrCacheBook(request.getGoogleVolumeId());

    UserBook userBook = new UserBook();
    userBook.setUser(user);
    userBook.setBook(book);
    userBook.setStatus(request.getStatus() != null ? request.getStatus() : ReadingStatus.WANT_TO_READ);
    userBook.setRating(request.getRating());
    userBook.setNotes(request.getNotes());

    return new UserBookResponse(userBookRepository.save(userBook));
  }

  public UserBookResponse updateBook(User user, String googleVolumeId, UserBookRequest request) {
    UserBook userBook = userBookRepository
        .findByUserAndBookGoogleVolumeId(user, googleVolumeId)
          .orElseThrow(() -> new IllegalArgumentException("Book not found in library"));

    if (request.getStatus() != null) {
      userBook.setStatus(request.getStatus());
    }
    if (request.getRating() != null) {
      userBook.setRating(request.getRating());
    }
    if (request.getNotes() != null) {
       userBook.setNotes(request.getNotes());
    }

    return new UserBookResponse(userBookRepository.save(userBook));
  }

  public void removeBook(User user, String googleVolumeId) {
    UserBook userBook = userBookRepository
        .findByUserAndBookGoogleVolumeId(user, googleVolumeId)
          .orElseThrow(() -> new IllegalArgumentException("Book not found in library"));

    userBookRepository.delete(userBook);
  }
}