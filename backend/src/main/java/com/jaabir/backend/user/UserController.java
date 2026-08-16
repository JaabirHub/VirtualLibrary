package com.jaabir.backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaabir.backend.security.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("api/auth")
public class UserController {
  
  private final UserService userService;
  private final JwtUtil jwtUtil;

  public UserController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthDTO> register(@Valid @RequestBody RegisterDTO dto) {
    User user = userService.register(dto);
    String token = jwtUtil.generateToken(user.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(new AuthDTO(token, new UserDTO(user)));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthDTO> login(@Valid @RequestBody LoginDTO dto) {
    User user = userService.login(dto);
    String token = jwtUtil.generateToken(user.getEmail());
    return ResponseEntity.ok(new AuthDTO(token, new UserDTO(user)));
  }
  
  @GetMapping("/me")
  public ResponseEntity<UserDTO> me() {
    User user = (User) SecurityContextHolder.getContext()
      .getAuthentication()
      .getPrincipal();
    return ResponseEntity.ok(new UserDTO(user));
  }
}
