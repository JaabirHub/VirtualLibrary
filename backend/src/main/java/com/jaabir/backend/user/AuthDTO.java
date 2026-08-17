package com.jaabir.backend.user;

public class AuthDTO {
  private String token;
  private UserDTO user;

  public AuthDTO(String token, UserDTO user) {
    this.token = token;
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }
}
