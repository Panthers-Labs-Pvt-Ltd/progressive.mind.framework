package com.progressive.minds.keyclock.dto;

import lombok.Data;

@Data
public class AdminConfigDTO {
  private String serverUrl;
  private String username;
  private String password;
  private String realm = "master";
}
