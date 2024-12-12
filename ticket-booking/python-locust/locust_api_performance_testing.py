import time
import pika
import random
from locust import User, task, events

class RabbitMQUser(User):
    messages_sent  = 0
    start_time = time.time()
        
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
        elapsed = time.time() - RabbitMQUser.start_time
        if RabbitMQUser.messages_sent / elapsed > 10:
            time.sleep(1)
            return
        
        compartment = random.choice(['A', 'B', 'C'])
        seat_number = random.randint(1, 200)
        username = f"user{random.randint(1, 10000)}"

        # Create message
        message = {
            "username": username,
            "compartment": compartment,
            "seatNumber": seat_number
        }

        # Publish the message to RabbitMQ
        RabbitMQUser.channel.basic_publish(
            exchange='ticket_booking_exchange',
            routing_key=f"compartment.{compartment}",
            body=str(message),
            properties=pika.BasicProperties(content_type='application/json', delivery_mode=2)
        )

        RabbitMQUser.messages_sent += 1
