package com.example.demo.service;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.BookingRequest;
import com.example.demo.model.Seat;
import com.example.demo.model.User;
import com.example.demo.repo.SeatRepository;
import com.example.demo.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.OptimisticLockException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private static final int MAX_RETRIES = 2;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);


    public BookingService(SeatRepository seatRepository, UserRepository userRepository, UserService userService) {
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    //implementation 1 - all in same queue.
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processBooking(String message) {
        System.out.println("Received message:" + message);
    }

    @Transactional
    public void processMessage(String message) {
        try {
            System.out.println("Received message:" + message);
            String validJsonMessage = message.replace("'", "\"");
            BookingRequest ticketRequest = new ObjectMapper().readValue(validJsonMessage, BookingRequest.class);
            bookTicket(ticketRequest);
            System.out.println("Processing booking for :" + ticketRequest);
        } catch (Exception e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }

    //different for different compartment.
    @RabbitListener(queues = "queue_A")
    @Transactional
    public void listenToQueueA(String message) {
        System.out.println("Received message from queue_A: " + message);
        processMessage(message);
    }

    @RabbitListener(queues = "queue_B")
    @Transactional
    public void listenToQueueB(String message) {
        System.out.println("Received message from queue_B: " + message);
        processMessage(message);
    }

    @RabbitListener(queues = "queue_C")
    @Transactional
    public void listenToQueueC(String message) {
        System.out.println("Received message from queue_C: " + message);
        processMessage(message);
    }

    public String bookTicket(BookingRequest request) {

        for (int attempt = 1; attempt < MAX_RETRIES; attempt += 1) {
            try {
                // Validate user
                User user = getUser(request);

                String seatKey = request.getCompartment() + ":" + request.getSeatNumber();
                String value = String.valueOf(redisTemplate.opsForValue().get(seatKey));
                if ("inprogress".equals(value) || "booked".equals(value)) {
                    throw new IllegalArgumentException("Seat already :" + value);
                }
                redisTemplate.opsForValue().set(seatKey, "inprogress", Duration.ofMinutes(2));

                boolean paymentSuccessful = processPayment(request);

                if (paymentSuccessful) {
                    System.out.println("booking seat" + seatKey);
                    redisTemplate.opsForValue().set(seatKey, "booked");
                    saveBookingToDatabase(request, user);
                    return String.format("Seat %s%d in compartment %s has been booked successfully for %s.",
                            request.getCompartment(),
                            request.getSeatNumber(),
                            request.getCompartment(),
                            request.getUsername()
                    );
                } else {
                    redisTemplate.delete(seatKey);
                    System.out.println("Failed to book seat, payment failed");
                }


            } catch (OptimisticLockException e) {
                if (attempt <= MAX_RETRIES) {
                    System.out.println("Retry");
                } else {
                    throw new IllegalArgumentException("Seat booking failed due to concurrent update. Please try again later.");
                }
            }
        }
        throw new IllegalStateException("Booking failed after retries.");

    }

    private boolean processPayment(BookingRequest request) {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Random r = new Random();
        int i = r.nextInt(10);
        return i % 2 == 0;
    }

    private void saveBookingToDatabase(BookingRequest request, User user) {
        // Validate seat availability
        Optional<Seat> optionalSeat = seatRepository.findByCompartmentAndSeatNumberWithLock(
                request.getCompartment(), request.getSeatNumber());
        if (optionalSeat.isEmpty()) {
            throw new IllegalArgumentException("Invalid seat!");
        }

        Seat seat = optionalSeat.get();
        if (seat.isBooked()) {
            throw new IllegalArgumentException("Seat already booked!");
        }
        seat.setBooked(true);
        seat.setUser(user);
        seatRepository.save(seat);

    }

    private User getUser(BookingRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            //bad design - because i don't want to create 10000s of users.
            userService.createUser(request.getUsername());
        }
        User user = userRepository.findByUsername(request.getUsername()).get();
        return user;
    }
}
