package com.backend.ljy.auth.api.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String nickname;
    private String password;

}
