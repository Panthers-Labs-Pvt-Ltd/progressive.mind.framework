package com.progressive.minds.keyclock.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RealmSetupDTO {
  private boolean enabled = true;
  private String realm;
  private ClientConfigDTO client;
  private List<RoleDTO> roles = new ArrayList<>();
  private List<GroupDTO> groups = new ArrayList<>();
  private List<UserDTO> users = new ArrayList<>();
}
