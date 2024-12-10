package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BookingRequest {

    @NotBlank
    private String username;

    @NotBlank
    private char compartment;

    @Min(1)
    @Max(200)
    private int seatNumber;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char getCompartment() {
        return compartment;
    }

    public void setCompartment(char compartment) {
        this.compartment = compartment;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}

