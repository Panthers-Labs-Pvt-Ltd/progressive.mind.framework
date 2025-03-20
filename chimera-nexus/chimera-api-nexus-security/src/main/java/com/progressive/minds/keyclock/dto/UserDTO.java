package com.progressive.minds.keyclock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private boolean enabled = true;

    @NotNull
    private List<String> roles = List.of();

    @NotNull
    private List<String> groups = List.of();

}
