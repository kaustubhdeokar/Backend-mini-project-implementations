import pika
import random
from locust import User, task, events
import time

class RabbitMQUser(User):
    @events.test_start.add_listener
    def on_test_start(environment, **kwargs):
        # Establish a connection to RabbitMQ
        connection_params = pika.ConnectionParameters('localhost')
        RabbitMQUser.connection = pika.BlockingConnection(connection_params)
        RabbitMQUser.channel = RabbitMQUser.connection.channel()

        # Declare the exchange (if it doesn't exist)
        RabbitMQUser.channel.exchange_declare(
            exchange='ticket_booking_exchange',
            exchange_type='direct',
            durable=True
        )

        # Declare queues for each compartment
        compartments = ['A', 'B', 'C']
        for compartment in compartments:
            queue_name = f"queue_{compartment}"
            RabbitMQUser.channel.queue_declare(queue=queue_name, durable=True)
            RabbitMQUser.channel.queue_bind(
                exchange='ticket_booking_exchange',
                queue=queue_name,
                routing_key=f"compartment.{compartment}"
            )

    @events.test_stop.add_listener
    def on_test_stop(environment, **kwargs):
        # Close the RabbitMQ connection
        RabbitMQUser.connection.close()

    @task
    def book_ticket(self):
        time.sleep(1)
        # Generate random request data
        compartment = random.choice(['A', 'B', 'C'])
        seat_number = random.randint(1, 20)
        userid = random.randint(1, 122200)
        username = f"user{userid}"

        # Create message
        message = {
            "username": username,
            "compartment": compartment,
            "seatNumber": seat_number,
            "userid":userid
        }

        # Publish the message to the appropriate queue via routing key
        RabbitMQUser.channel.basic_publish(
            exchange='ticket_booking_exchange',
            routing_key=f"compartment.{compartment}",  # Use routing key based on compartment
            body=str(message),
            properties=pika.BasicProperties(content_type='application/json', delivery_mode=2)
        )

        print(f"Message sent to compartment {compartment}: {message}")
