package com.example.demo.repo;

import com.example.demo.model.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findByCompartmentAndSeatNo(char compartment, int seatNo);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Seat s WHERE s.compartment = :compartment AND s.seatNo = :seatNumber")
    Optional<Seat> findByCompartmentAndSeatNumberWithLock(
            @Param("compartment") char compartment,
            @Param("seatNumber") int seatNumber
    );
}
