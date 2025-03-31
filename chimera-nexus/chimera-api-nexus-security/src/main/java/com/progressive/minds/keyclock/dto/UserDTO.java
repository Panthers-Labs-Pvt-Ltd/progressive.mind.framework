package com.progressive.minds.keyclock.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
  private String email;
  private String firstName;
  private String lastName;
    private boolean enabled = true;
  private List<UserCredentialDTO> credentials = new ArrayList<>();
  private List<String> roles = new ArrayList<>();
  private List<String> groups = new ArrayList<>();
}