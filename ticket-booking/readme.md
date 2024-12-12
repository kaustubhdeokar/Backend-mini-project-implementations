The ticket booking project explores various strategies to handle high concurrency and ensure data consistency. Here is a brief write-up of the approaches and techniques used:

1. Locking Types
Initially, we explored pessimistic locking to handle concurrent booking requests. Pessimistic locking directly accesses the database, locking the resource until the transaction is complete. This approach can lead to high contention and increased retry rates, especially under heavy load.

2. Introducing Queues
To mitigate the drawbacks of pessimistic locking, we introduced queues. Each compartment (A, B, and C) has its own queue, allowing requests to be processed serially. This reduces contention and retries, as each queue processes one request at a time. The queue-based approach significantly improves performance and resource utilization.

3. Caching
To further enhance performance, we implemented caching using Redis. Caching helps manage in-progress requests and reduces the load on the database. For example, we cache seat booking statuses with a TTL (Time To Live) to handle payment processing gracefully. This approach minimizes database contention and improves the overall efficiency of the booking system.

### For detailed explanations, refer to theory.md in the ticket booking folder.