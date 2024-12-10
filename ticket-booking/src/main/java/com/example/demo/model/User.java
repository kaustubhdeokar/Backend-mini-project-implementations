package com.example.demo.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Long userId;
    @NotBlank(message = "Username cannot be blank")
    private String username;

    public User(String username) {
        this.username = username;
    }

    public User() {
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
