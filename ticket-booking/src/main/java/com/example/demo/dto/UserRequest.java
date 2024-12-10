package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
