package com.mohamed.management.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UserRequest {
    @NonNull
    private String username;
    private String email;
    @NonNull
    private String password;

    public String toString(){
        return "username: " + username + "\nemail: " + email + "\npassword: " + password;
    }
}
