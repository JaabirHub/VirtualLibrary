package com.jaabir.backend.userbook;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaabir.backend.user.User;

public interface UserBookRepository extends JpaRepository<UserBook, UUID>{
  List<UserBook> findByUser(User user);
  Optional<UserBook> findByUserAndBookGoogleVolumeId(User user, String googleVolumeId);
  boolean existsByUserAndBookGoogleVolumeId(User user, String googleVolumeId);
}
