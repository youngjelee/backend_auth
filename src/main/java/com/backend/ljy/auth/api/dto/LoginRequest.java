package com.backend.ljy.auth.api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

}
