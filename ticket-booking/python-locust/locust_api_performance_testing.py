from locust import HttpUser, task, between
import random

class BookingUser(HttpUser):
    # Wait time between requests for each user
    host = 'http://localhost:8000'
    wait_time = between(1, 1)

    @task
    def book_ticket(self):
        # Generate random compartment and seat number
        retries = 2
        for i in range(0, retries, 1):
            compartment = random.choice(['A', 'B', 'C'])
            seat_number = random.randint(1, 200)
            username = f"user{random.randint(1, 10000)}"  # Random username for uniqueness

            # Send POST request with randomized data
            response = self.client.post("/api/bookings", json={
                "username": username,
                "compartment": compartment,
                "seatNumber": seat_number
            })

            # Optional: Log the response or handle errors
            if response.status_code == 200:
                print(f"Successfully booked seat {seat_number} in compartment {compartment} for {username}")
                i=0
                break
            else:
                print('i',i)
                print('retries:', retries)
                print(f"Failed to book seat {seat_number} in compartment {compartment} for {username}: {response.text}")
            if(i==retries):
                break
