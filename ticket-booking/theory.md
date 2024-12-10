- Pessimistic locking.
- Suppose there are n threads trying to execute a critical section, a thread enters a critical section, while acquiring a lock, executed the critical section, and then releases the lock. Then it goes on similarly for the rest of the threads.

- Optimistic locking.
- Suppose there are n threads trying to execute a critical section, if two threads are trying to do the same thing, one thread succeeds while the other fails. 
  - how does it work ? Compare and swap method. (x, oldVal, newVal)
  - only update (x) if the old value = oldVal, -> x = newVal.
  - we only update the resource 
  
  - now if suppose thread 1 updates the resource, then any other thread trying to update the resource and having a stale oldVal will fail, in this case we can choose to either retry or fail.  
  - Also, remembering that the compare and swap method is atomic in nature. CPU will not context switch when executing compare and swap method. So, no chance the condition can be true for two or more threads.

- what is the point of using a queue here ? won't it be same as a pessimistic lock ? 
  - users if they don't use the queue, directly sent the request to the database. it's more costly, conflicts occuring frequently will be dealt by the database leading to retries and wasted resources.
  - queue can execute one query at a time, (we can have multiple queues for different section or categories), can help to avoid multiple retries
  - async notification in case of large queue size.

- Using caching to further enhance this. 
    - caching the request which are in progress, (under the grace time for payment completion) such queries will be put for retry or failure queries, without even in contention for database.
    - like using redis, suppose we set a key like <seat-id, status(booking), ttl=120> this key will exist in the cache for 2 mins.

- database options
  - sql database should be good for this case, we want consistency.
    - check this : (postgres) Extensions like pg_bouncer and pgpool-II can help with connection pooling and load balancing.
    - check this : cockroach db.
  - nosql (cassandra) optimized for higher availability rather than consistency.

- for queues: rabbit/kafka are better options than in house queues(language specific)
  - for a small size, where all operations are in memory this serves as a great option.
  - in case of crash, we will loose all the data in JVM or memory.
  - both have support for failed cases.
  - a simple queue (like in java) would be good when we have a single server

  - Rabbit vs Kafka for booking
    - Rabbit is good for real-time, transactional systems like booking. has dead-letter queues for failure cases.
  - Kafka
    - good for event driven systems for analytics, auditing. has dead


- Time take for each process to complete , in the following constraints (Not using the cache here) plain pessimistic lock vs queue approach
  - We'll explore how the system behaves when handling 100,000 requests for 600 seats distributed across 3 compartments: A, B, and C, each with 200 seats.

- Pessimistic Locking without Queues:

    - Each booking request directly accesses the database.
    - 30% of requests encounter conflicts due to locking contention.
    - Assume a database query takes 5ms under normal conditions.
    - A conflict (retry due to locking) adds an additional 10ms per retry.
    - Average retry count for conflicts = 2.

      - Total requests: 100,000
      - Conflicts: 30%
      - Time : .7 * 100,000 * 5ms + .3 * 100,000 * (5ms+20ms - for retry) = 350,000 + 750,000 ms = ~1000s.


- Using Queues (One Queue per Compartment):

    - Requests are distributed to 3 queues (A, B, and C) based on the compartment.
    - Each queue processes requests serially, avoiding locking conflicts.
    - Assume processing a request from a queue takes 5ms.
    - Queue workers scale horizontally, allowing concurrent processing of requests across compartments.

        - Queue Setup: We have 20 workers processing 33,333 requests in queue A, b, c.
        - Average conflict rate: Let’s assume 30% of requests experience conflicts (i.e., the seat is already booked by another request).
        - Time per request without conflict: 5ms.
        - Retry time: 10ms for each retry.
        - Time: 0.7 * 33,333 * 5 + .3 * 33,333 * (5 + 20) = ~367s
        - on having 20 workers, it takes around 400/20 = 20s.
