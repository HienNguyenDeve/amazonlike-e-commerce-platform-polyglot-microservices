# Tilte
Amazon-like (E-commerce microservices plaform)

## Overview
This project is a production-grade E-commerce Backend Platform built using a Microservices Architecture.

The system simulates a real-world e-commerce environment, focusing on:
- Scalability
- Resilience
- Event-driven architecture
- Clean system design
- Production-ready engineering practices

## Architechture
🔹 High-Level Architecture
Client 
→ API Gateway 
→ Microservices Cluster 
→ Messaging (Kafka) 
→ Databases (PostgreSQL / Redis) 
→ External Integrations

🔹 Architecture Style
Microservices Architecture
Event-driven (Kafka)
RESTful APIs (synchronous communication)
Eventually Consistent System (Saga pattern)

## Tech stack
Backend
- Java 21
- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Security
- Spring Data JPA

Data & Messaging
- PostgreSQL
- Redis
- Kafka

Infrastructure
- Docker / Docker Compose
- (Planned) Kubernetes
- (Planned) CI/CD Pipeline

Observability (Planned)
- Prometheus + Grafana
- OpenTelemetry (Tracing)
- Centralized Logging (Loki / ELK)

## Project structure

ecommerce-platform/ 
├── docs/ # SRS, diagrams, API docs 
├── infra/ # Docker, Kafka, DB, monitoring 
├── platform/ │ └── api-gateway/ # Gateway service 
├── services/ 
│ ├── auth-service/ 
│ ├── product-service/ 
│ ├── cart-service/ 
│ ├── order-service/ 
│ ├── inventory-service/ 
│ ├── payment-service/ 
│ └── notification-service/ 
├── contracts/ # API & event schemas 
├── libs/ # Shared libraries 
├── scripts/ # Automation scripts 
└── tests/ # Integration / E2E tests

## Services
- api-gateway: Entry point, routing, cross-cutting concerns
- auth-service: Authentication, authorization, JWT, OAuth2
- product-service: Product catalog & category management
- cart-service: Shopping cart (Redis-based)
- order-service: Order lifecycle management
- inventory-service: Stock management & reservation
- payment-service: Payment processing & transaction
- notification-service: Email/SMS notification


## How to run

🔧 Prerequisites
Docker & Docker Compose
Java 21
Maven
(Optional) IntelliJ IDEA

▶️ Start Infrastructure
cd infra/docker
docker compose up -d

This will start:

PostgreSQL
Redis
Kafka
Kafka UI

▶️ Run Services (Example)
cd services/auth-service
./mvnw spring-boot:run

Repeat for:

product-service
api-gateway

🌐 Access
Component:	URL
API Gateway:	http://localhost:8080
Swagger:	http://localhost:8080/swagger-ui
Kafka UI:	http://localhost:8081

🔁 Development Workflow
- Start infrastructure via Docker
- Run required services locally
- Test APIs via Gateway
- Implement feature per service
- Add integration/event flow
- Validate with Postman / E2E tests

🔄 Event-Driven Flow (Example)
Order Created
→ Inventory Reserved
→ Payment Initiated
→ Payment Succeeded / Failed
→ Order Confirmed / Cancelled
→ Notification Sent

🧪 Testing Strategy
- Unit Tests (service level)
- Integration Tests (service + DB)
- Contract Tests (API / event schema)
- End-to-End Tests (full flow)

🔐 Security
- JWT Authentication
- Role-Based Access Control (RBAC)
- Secure API Gateway routing
- Input validation & sanitization

📊 Non-Functional Goals
High availability (target ≥ 99.9%)
Horizontal scalability
Fault tolerance (retry, timeout, circuit breaker)
Observability (logs, metrics, traces)

🗺️ Roadmap
- Phase 0 — Foundation
Project setup
Docker infrastructure
Gateway + base services
- Phase 1 — Identity
Authentication & authorization
- Phase 2 — Catalog
Product & category management
- Phase 3 — Commerce Core
Cart & order flow
- Phase 4 — Payment & Inventory
Checkout & transaction
- Phase 5 — Event & Saga
Kafka integration
Event-driven workflow
- Phase 6 — Extensions
Notification, shipping, promotion
- Phase 7 — Hardening
Observability
CI/CD
Performance tuning

🎯 Purpose

This project is built to:

Practice system design & microservices architecture
Simulate real production backend systems
Demonstrate backend engineering skills for interviews
Build a high-quality technical portfolio

⚠️ Disclaimer

This project is a learning + engineering simulation and is not intended for direct commercial use without further hardening and security validation.

👤 Author

[Nguyễn Văn Hiển]

Backend Engineer
Focus: Microservices, System Design, Distributed Systems

## Future improvements