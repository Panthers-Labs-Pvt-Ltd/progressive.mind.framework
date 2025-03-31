package com.progressive.minds.keyclock.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class GroupDTO {
    private String name;
  private List<String> roles = new ArrayList<>();
}