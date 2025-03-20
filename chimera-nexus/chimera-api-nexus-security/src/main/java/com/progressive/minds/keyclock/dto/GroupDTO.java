package com.progressive.minds.keyclock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {

    @NotBlank
    private String name;

    @NotNull
    private List<String> roles = List.of();

}
