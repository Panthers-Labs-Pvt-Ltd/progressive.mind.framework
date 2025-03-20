package com.progressive.minds.keyclock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ClientDTO {

    @NotBlank
    private String clientId;

    private boolean enabled = true;
    private boolean publicClient = true;

    @NotNull
    private List<String> redirectUris = List.of();

    @NotNull
    private List<String> webOrigins = List.of();
}
