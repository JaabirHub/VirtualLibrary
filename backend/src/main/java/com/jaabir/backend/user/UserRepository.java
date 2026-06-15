package com.jaabir.backend.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for User.
 */
public interface  UserRepository extends JpaRepository<User, UUID>{
  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
  
}
