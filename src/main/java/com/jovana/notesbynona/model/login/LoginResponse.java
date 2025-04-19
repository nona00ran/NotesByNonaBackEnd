package com.jovana.notesbynona.model.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private String message;
}
