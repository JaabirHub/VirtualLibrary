package com.jaabir.backend.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }


  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new IllegalArgumentException("User not found."));
  };

  @Transactional
  public User register(RegisterDTO dto) {
    if(userRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("Email is already in use.");
    }

    User user = new User();
    user.setEmail(dto.getEmail());
    user.setUsername(dto.getUsername());
    user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
    user.setRole(Role.USER);
    return userRepository.save(user);
  }

  @Transactional
  public User login(LoginDTO dto) {
    User user = findByEmail(dto.getEmail());

    if(!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid Credentials");
    }

    return (user);
  }
}
