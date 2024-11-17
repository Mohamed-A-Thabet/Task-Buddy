package com.mohamed.management.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private User user;
    private String jwt;
    private String message;
}
