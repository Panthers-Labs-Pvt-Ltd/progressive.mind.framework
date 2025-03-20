package com.progressive.minds.keyclock.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    @NotBlank
    private String name;

    private String description;
}
