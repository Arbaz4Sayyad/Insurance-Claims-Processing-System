# Interview Preparation: Insurance Eligibility & Claims Processing System (IECS)
## Senior Backend Engineer Context (2+ YOE)

---

### **Section 1: Microservices Architecture & System Design**

#### **Q1. Walk me through the architecture of your Insurance Eligibility System. Why choose microservices over a monolith?**
**Answer:** The system is built using a **Spring Cloud-based Microservices architecture**. It consists of specialized services: **User Service** (Identity), **Application Registration (AR)**, **Data Collection (DC)**, **Eligibility Determination (ED)**, and **Benefit Issuance (BI)**. 
We chose microservices because insurance rules and claims processing are inherently complex and change at different rates. For instance, the **ED Service** (rule engine) might need frequent updates due to policy changes, while the **User Service** remains stable. This decoupling allows us to scale services independently—scaling only the **DC Service** during open enrollment periods—and ensures that a failure in the **Notification Service** doesn't bring down the entire claims pipeline.

#### **Q2. How do your microservices communicate with each other? How do you handle service discovery?**
**Answer:** We use a combination of **Synchronous (REST via Feign Clients)** and **Internal Event-driven** communication. For critical, real-time flows (like checking a user's role during login), we use **OpenFeign** for inter-service REST calls. 
To manage these dynamic instances, we use **Netflix Eureka** for Service Discovery. Every microservice registers itself with the Eureka server on startup. When Service A needs to call Service B, it looks up the service name in Eureka rather than using hardcoded IPs. This enables seamless horizontal scaling and load balancing via **Spring Cloud LoadBalancer**.

#### **Q3. What is the role of the API Gateway in your system?**
**Answer:** We use **Spring Cloud Gateway** as the single entry point for all client requests. It handles three critical concerns:
1.  **Routing:** It maps external URLs to internal microservice instances (e.g., `/api/users/**` -> User Service).
2.  **Security:** It acts as a PEP (Policy Enforcement Point), validating the **JWT** before forwarding requests to downstream services.
3.  **Cross-cutting Concerns:** It implements **Rate Limiting** to prevent DDoS attacks and **Global Logging** to trace every incoming request with a unique Correlation ID.

#### **Q4. How do you manage configuration across multiple microservices without restarting them?**
**Answer:** We use **Spring Cloud Config Server**. All configuration properties (DB credentials, feature flags, API keys) are stored in a centralized Git repository. Microservices fetch their config on startup. To update configurations at runtime without a restart, we use the **`@RefreshScope`** annotation combined with the `/actuator/refresh` endpoint. This allows us to toggle features or update timeout values dynamically.

---

### **Section 2: Security & Authentication**

#### **Q5. How is authentication and authorization implemented in IECS?**
**Answer:** We use **JWT (JSON Web Tokens)** for stateless authentication and **OAuth2** for external integrations. When a user logs in via the User Service, they receive a signed JWT containing their identity and roles. 
For **Authorization**, we've implemented **RBAC (Role-Based Access Control)**. Roles like `CITIZEN`, `CASEWORKER`, and `ADMIN` are embedded in the JWT's claims. Downstream services use Spring Security's `@PreAuthorize` or `@Secured` annotations to ensure that only a `CASEWORKER` can approve a claim, for example.

#### **Q6. Why did you choose JWT over Session-based authentication?**
**Answer:** In a distributed microservices environment, **Session-based authentication** requires a shared session store (like Redis) or "Sticky Sessions" at the load balancer, which complicates scaling. **JWT** is stateless; the token itself contains all the necessary information. Any microservice can validate the token's signature using a public/shared key without querying a central database, making the system highly scalable and resilient to single points of failure.

#### **Q7. How do you protect sensitive data like SSNs or financial info in your database?**
**Answer:** Data protection is handled at three levels:
1.  **Encryption at Rest:** Using PostgreSQL's transparent data encryption or disk-level encryption.
2.  **Encryption at Move:** All inter-service communication and client-to-gateway traffic happens over **TLS/HTTPS**.
3.  **Application Level:** Sensitive fields are encrypted before being saved to the DB using **JPA Attribute Converters** with AES-256. We also ensure that logs are masked to prevent accidental leakage of PII (Personally Identifiable Information).

---

### **Section 3: Database Design & Data Management**

#### **Q8. You have a "Database per Service" pattern. How do you handle data consistency across services?**
**Answer:** Since each service (AR, DC, ED) has its own PostgreSQL database, we cannot use traditional ACID transactions across them. We follow the principle of **Eventual Consistency**. 
For example, when an application is registered in the **AR Service**, it publishes an event. The **DC Service** listens to this and initializes a data record. If a downstream step fails, we use **Compensating Transactions** (part of the **Saga Pattern**) to roll back the previous steps (e.g., marking the application as `REJECTED` or `ERROR`).

#### **Q9. Why PostgreSQL instead of a NoSQL database like MongoDB for insurance claims?**
**Answer:** Insurance data is highly structured and requires strict **ACID compliance**. Financial benefit issuance cannot afford "lost updates" or "partial writes." PostgreSQL gives us powerful relational features (JOINs across claims and citizens) and supports complex data types (JSONB for flexible metadata). The trade-off is a slightly more rigid schema, but for a system where data integrity is paramount, SQL is the right choice.

#### **Q10. How do you optimize slow queries in the Claims Dashboard?**
**Answer:** First, I identify slow queries using **PostgreSQL EXPLAIN ANALYZE**. 
1.  **Indexing:** I apply **B-Tree indexes** on frequently filtered columns like `case_status` or `citizen_id`.
2.  **Covering Indexes:** For dashboards that only need a few columns, I create indexes that `INCLUDE` those columns to avoid a table heap lookup.
3.  **Read Replicas:** If the read load is high, I would split the traffic—pointing heavy dashboard queries to a Read Replica while keeping the primary for writes.

---

### **Section 4: Performance, Caching & Scaling**

#### **Q11. Where do you use Redis in this project, and why?**
**Answer:** We use **Redis** for two main purposes:
1.  **Caching Master Data:** Frequently accessed but rarely changed data (like Plan types, State codes, or Eligibility rules) is cached in Redis to reduce the load on the PostgreSQL databases and decrease API latency.
2.  **Rate Limiting:** We store request counts per API key in Redis (via Spring Cloud Gateway) to enforce limits and protect against bursts of traffic.

#### **Q12. How do you handle a scenario where 10,000 users try to submit applications simultaneously?**
**Answer:** Our system scales horizontally. 
1.  **Statelessness:** Since all services are stateless (auth in JWT, no local sessions), we can spin up multiple instances of the **AR** and **DC** services behind the Gateway.
2.  **Load Balancing:** The API Gateway distributes requests across these instances.
3.  **Backpressure:** If the DB or AI processing (if any) is slow, we use a **Message Queue (Kafka)** to buffer these requests. The services can then process applications at their own pace without crashing.

#### **Q13. How do you handle large file uploads (e.g., medical documents) in a microservices setup?**
**Answer:** Loading large files into the JVM memory is a recipe for `OutOfMemoryError`. 
1.  **Streaming:** I use **Streaming I/O** to pipe the file directly from the HTTP request to the storage layer (like AWS S3 or a local volume). 
2.  **Direct Uploads:** For better performance, the backend can provide a **Pre-signed URL**, allowing the frontend to upload the file directly to S3. The backend only stores the file's metadata and S3 path.

---

### **Section 5: Async Processing & Reliability**

#### **Q14. What happens if the Eligibility Determination (ED) service is down? How do you prevent a cascading failure?**
**Answer:** We implement the **Circuit Breaker Pattern** using **Resilience4j**. If the **ED Service** is down or slow, the Circuit Breaker "opens" and redirects calls to a **Fallback Method**. This fallback might return a "Processing in progress" status to the user or fetch a cached result, ensuring that the **AR Service** doesn't hang or crash while waiting for a timed-out response.

#### **Q15. How do you handle retries for inter-service communication?**
**Answer:** For transient network issues, we use **Feign Retries** or **Resilience4j Retry**. We always use **Exponential Backoff** with jitter to avoid "Thundering Herd" problems where all retrying clients hit the server at the exact same moment, causing it to crash again.

#### **Q16. Why did you mention Kafka as a future improvement? How would it change your current design?**
**Answer:** Currently, we use internal events or REST calls. Moving to **Kafka** would enable a truly **Event-Driven Architecture**. 
Instead of the AR Service calling the DC Service directly, it would simply publish an `ApplicationCreated` event. Multiple services (DC, Analytics, Notification) can subscribe to this independently. This increases **Temporal Decoupling**—even if the Notification service is down for 2 hours, it can consume the messages from Kafka once it's back up, ensuring no notification is ever lost.

---

### **Section 6: Error Handling, Logging & Monitoring**

#### **Q17. How do you debug a request that failed across three different microservices?**
**Answer:** I use **Distributed Tracing**. I would integrate **Spring Cloud Sleuth** (now Micrometer Tracing) which adds a `Trace ID` to every request. This ID is propagated across all inter-service calls via HTTP headers. By searching for that Trace ID in a centralized log aggregator (like **ELK Stack** or **Grafana Loki**), I can see the exact flow of the request and identify which service threw the exception.

#### **Q18. How do you handle exceptions globally in your Spring Boot services?**
**Answer:** I use a **`@ControllerAdvice`** class with **`@ExceptionHandler`** methods. This ensures that the API always returns a consistent, structured error response (e.g., a JSON with `errorCode`, `message`, and `timestamp`) rather than a messy stack trace. This improves the developer experience for the frontend team and prevents leaking internal system details to the user.

---

### **Section 7: Deployment & Infrastructure**

#### **Q19. Why did you containerize your services with Docker?**
**Answer:** Docker ensures "It works on my machine" translates to production. It packages the JDK, dependencies, and configuration into a single image. This eliminates environment-related bugs and makes it incredibly easy to deploy the entire stack (PostgreSQL, Redis, 5+ Microservices) using **Docker Compose** or **Kubernetes**, ensuring consistency across Dev, QA, and Prod.

#### **Q20. How do you handle database migrations in a microservices environment?**
**Answer:** I use **Flyway**. Each microservice has its own folder of versioned SQL migration scripts. When a service starts, Flyway checks the `flyway_schema_history` table and applies any new scripts. This ensures the database schema is always in sync with the code version and makes rollbacks predictable.

---

### **Section 8: Practical & Scenario-Based**

#### **Q21. A caseworker reports that a claim's status is "Pending" but the benefit was already issued. How do you investigate this "Split Brain" scenario?**
**Answer:** This is likely a **Distributed Transaction** failure. I would:
1.  Trace the `case_id` across the **BI (Benefit Issuance)** and **AR/ED** logs using the **Trace ID**.
2.  Check if the message to update the status was lost or if the service crashed *after* issuing the benefit but *before* updating its own DB.
3.  **Fix:** I would implement an **Idempotent Consumer** in the BI service and use the **Outbox Pattern** to ensure that DB updates and event publishing happen atomically within the same service.

#### **Q22. How do you ensure your code is "Interview Ready" in terms of quality?**
**Answer:** I follow **SOLID principles** and **Clean Architecture**. My business logic is isolated in **Service layers**, while data access is handled by **Repositories (Spring Data JPA)**. I write **Unit Tests** using JUnit/Mockito to cover edge cases in eligibility rules and **Integration Tests** using **Testcontainers** to verify that my microservices correctly interact with a real PostgreSQL instance.

#### **Q23. How do you manage secrets like DB passwords and API keys?**
**Answer:** I never commit secrets to Git. I use **Environment Variables** which are injected into the Docker container. In a production environment, I would use a dedicated **Secret Manager** (like AWS Secrets Manager or HashiCorp Vault) and integrate it with **Spring Cloud Vault** to fetch secrets securely at runtime.

#### **Q24. What is the biggest technical challenge you faced in this project?**
**Answer:** The biggest challenge was managing **inter-service dependencies** during startup. Since services like AR depend on the Config Server and Eureka, they would often fail if they started too quickly. I solved this by implementing **Health Checks** and using the `depends_on` condition with `healthcheck` in Docker Compose, and adding **retry logic** in the application's `bootstrap.yml` to wait for the Config Server.

#### **Q25. How do you handle API Versioning if you need to make a breaking change?**
**Answer:** I prefer **URI Versioning** (e.g., `/api/v1/claims` vs `/api/v2/claims`). This allows us to keep the old version running for existing clients while rolling out the new structure. At the Gateway level, we can route traffic based on the version prefix, giving us a graceful migration path.

---

### **Section 9: Bonus/Advanced Topics**

#### **Q26. What is "Service Mesh" and would you use it here?**
**Answer:** A Service Mesh (like **Istio**) moves concerns like service discovery, retries, and security from the application code to a "Sidecar" proxy. For our current scale, **Spring Cloud** is sufficient and easier to manage. However, if we grow to 50+ services with complex routing and security requirements (like Mutual TLS between all services), a Service Mesh would be a better choice to keep the application code clean.

#### **Q27. How do you handle "Idempotency" in your REST APIs?**
**Answer:** For `POST` requests (like submitting a claim), I use an **Idempotency Key** (usually a UUID generated by the frontend). The backend stores this key in Redis for a short duration. If the same request is received again with the same key, the backend returns the cached response instead of processing the claim twice, preventing duplicate benefit issuance.

#### **Q28. Explain the "Outbox Pattern" and why it's useful in your system.**
**Answer:** The Outbox Pattern solves the problem of "Atomicity" between a DB update and sending a message. Instead of sending a message to a queue directly, the service writes the message to a special `OUTBOX` table in its own database as part of the same transaction. A separate **Relay Service** (or Debezium) then reads from this table and pushes to the message broker. This ensures that if the DB update fails, the message is never sent, and vice-versa.

#### **Q29. How do you handle "Bulkheads" in your system?**
**Answer:** Using **Resilience4j Bulkhead**, I can limit the number of concurrent calls to a specific service (e.g., the slow Notification service). This prevents one slow service from consuming all available threads in the caller service, protecting the rest of the application's functionality.

#### **Q30. How do you monitor the "Health" of your distributed system?**
**Answer:** We use **Spring Boot Actuator** which provides endpoints like `/health` and `/metrics`. We scrape these metrics using **Prometheus** and visualize them in **Grafana**. This allows us to set up alerts for high CPU usage, slow response times, or high error rates, enabling us to react before users even notice an issue.
