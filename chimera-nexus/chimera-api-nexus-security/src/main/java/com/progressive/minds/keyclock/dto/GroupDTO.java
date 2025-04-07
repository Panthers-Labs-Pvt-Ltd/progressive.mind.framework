package com.progressive.minds.keyclock.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupDTO {
    private String name;
    private List<String> roles = new ArrayList<>();
}