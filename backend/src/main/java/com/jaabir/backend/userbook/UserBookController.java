package com.jaabir.backend.userbook;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaabir.backend.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/me/library")
public class UserBookController {

  private final UserBookService userBookService;

  public UserBookController(UserBookService userBookService) {
    this.userBookService = userBookService;
  }

  @GetMapping
  public ResponseEntity<List<UserBookResponse>> getLibrary() {
    User user = getCurrentUser();
    return ResponseEntity.ok(userBookService.getLibrary(user));
  }

  @PostMapping
  public ResponseEntity<UserBookResponse> addBook(@Valid @RequestBody UserBookRequest request) {
    User user = getCurrentUser();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userBookService.addBook(user, request));
  }

  @PatchMapping("/{googleVolumeId}")
  public ResponseEntity<UserBookResponse> updateBook(
        @PathVariable String googleVolumeId,
        @Valid @RequestBody UserBookRequest request) {
    User user = getCurrentUser();
    return ResponseEntity.ok(userBookService.updateBook(user, googleVolumeId, request));
  }

  @DeleteMapping("/{googleVolumeId}")
  public ResponseEntity<Void> removeBook(@PathVariable String googleVolumeId) {
    User user = getCurrentUser();
    userBookService.removeBook(user, googleVolumeId);
    return ResponseEntity.noContent().build();
  }

  private User getCurrentUser() {
    return (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
  }
}