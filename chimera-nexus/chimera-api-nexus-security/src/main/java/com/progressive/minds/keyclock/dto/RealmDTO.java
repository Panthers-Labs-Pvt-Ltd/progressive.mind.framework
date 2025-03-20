package com.progressive.minds.keyclock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RealmDTO {
    @NotBlank
    private String serverUrl;
    @NotNull
    private String realm;
    @NotNull
    private String clientId;
    @NotNull
    private String username;

    @NotNull
    private String password;
}
