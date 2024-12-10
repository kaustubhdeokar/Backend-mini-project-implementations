package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seats")
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue
    private Long seatId;
    private char compartment;
    private int seatNo;
    private boolean isBooked;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Seat(char compartment, int seatNo) {
        this.compartment = compartment;
        this.seatNo = seatNo;
    }

    @Version
    private int version; //used for optimistic locking.

    public Seat() {
    }

    public void setUser(User user) {
        this.user = user;
    }
}
