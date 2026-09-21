# GitStatus – AI-Powered GitHub Repository Intelligence Platform

GitStatus is a production-ready backend platform built with **Spring Boot** that automatically discovers, analyzes, ranks, and explains GitHub repositories using Artificial Intelligence.

The platform combines the **GitHub REST API**, **Ollama LLM**, **MongoDB**, and **Redis** to provide intelligent repository recommendations across multiple technology domains including AI, Backend, Frontend, DevOps, Security, Mobile, Databases, and more.

---

## Features

### Repository Discovery

- Automatically fetch repositories from multiple technology categories
- Synchronize repository metadata using the GitHub REST API
- Store repository statistics including:
  - Stars
  - Forks
  - Watchers
  - Issues
  - Last Updated Date

---

### AI-Powered Repository Analysis

- Generates human-readable repository explanations using Ollama
- Processes repository README files
- Uses prompt engineering for high-quality repository summaries
- Stores AI-generated explanations for future requests

---

### Intelligent Repository Ranking

GitStatus calculates a custom **Attention Score** using repository engagement metrics.

```
Raw Score =
(Stars × 0.50)
+ (Forks × 1.50)
+ (Watchers × 0.30)
+ (Issues × 0.10)
```

The final score is normalized and adjusted using repository activity and update recency.

---

### Redis Caching

To reduce unnecessary GitHub API calls:

- README files are cached
- AI explanations are cached
- Repository metadata is cached
- Configurable cache expiration

---

### Scheduled Background Jobs

- Automatic repository synchronization
- Scheduled README updates
- Background AI analysis
- Daily ranking refresh

---

### REST API

Provides REST endpoints for:

- Repository discovery
- Category filtering
- README retrieval
- AI explanations
- Cache management

---

## Technology Stack

| Category | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Build Tool | Maven |
| Database | MongoDB |
| Cache | Redis |
| AI | Ollama (Gemma 3) |
| GitHub Integration | GitHub REST API |
| Scheduling | Spring Scheduler |
| HTTP Clients | WebClient, RestClient |
| Monitoring | Spring Boot Actuator |
| Version Control | Git & GitHub |

---

# System Architecture

```
                  GitHub REST API
                         │
                         ▼
                Repository Fetch Service
                         │
                         ▼
                   MongoDB Database
                         │
          ┌──────────────┴──────────────┐
          ▼                             ▼
   README Cache                  Ranking Service
       │                               │
       ▼                               ▼
     Redis                     Attention Score
       │                               │
       └──────────────┬────────────────┘
                      ▼
               Ollama (Gemma 3)
                      │
                      ▼
          AI Repository Explanation
                      │
                      ▼
               REST API Endpoints
```

---

# Project Structure

```
src/main/java
│
├── config
├── controller
├── dto
├── entity
├── model
├── repository
├── scheduler
├── service
├── ollamaservices
└── GitStatusApplication.java
```

The project follows the standard Spring Boot layered architecture.

- Controller Layer
- Service Layer
- Repository Layer
- Configuration Layer
- AI Integration Layer

---

# REST API

## Repository APIs

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/fetch/categories` | Get supported categories |
| GET | `/api/fetch/all` | Fetch repositories from all categories |
| GET | `/api/fetch/{category}` | Fetch repositories by category |

---

## AI APIs

| Method | Endpoint |
|---------|----------|
| GET | `/api/ai/explain/{githubId}` |

Generates an AI explanation for a repository.

---

## README APIs

| Method | Endpoint |
|---------|----------|
| GET | `/api/readme/{githubId}` |
| GET | `/api/readme/exists/{githubId}` |
| POST | `/api/readme/cache-all` |
| POST | `/api/readme/cache-batch` |
| DELETE | `/api/readme/{githubId}` |

---

# Getting Started

## Prerequisites

- Java 17+
- Maven
- MongoDB
- Redis
- Ollama

Install the required Ollama model:

```bash
ollama pull gemma3:4b
```

Start Ollama

```bash
ollama serve
```

---

## Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/GitStatus.git

cd GitStatus
```

---

## Configure Environment

Create

```
src/main/resources/application.yml
```

using the provided

```
application.yml.example
```

and configure:

- MongoDB URI
- GitHub Personal Access Token
- Redis
- Ollama

---

## Build

```bash
mvn clean install
```

---

## Run

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/gitStatus.jar
```

---

# Attention Score

Repositories are ranked using:

```
Score =
(Stars × 0.50)
+ (Forks × 1.50)
+ (Watchers × 0.30)
+ (Issues × 0.10)

↓

Recency Adjustment

↓

Normalized Score (0–100)
```

This allows highly active repositories to rank above inactive projects with large historical star counts.

---

# Performance Optimizations

- Redis caching
- Batch README processing
- Scheduled synchronization
- Layered architecture
- Dependency Injection
- Repository Pattern
- MongoDB indexing
- RESTful API design

---

# Future Improvements

- Newsletter delivery
- User authentication
- OAuth login
- GraphQL API
- Kafka event processing
- Docker deployment
- Kubernetes deployment
- CI/CD pipeline
- Swagger/OpenAPI documentation

---

# Author

**Sohail Ahmad**

Software Engineer | Backend Developer | AI Engineer

GitHub:
https://github.com/burusho786



---

