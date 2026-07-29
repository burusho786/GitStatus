GitStatus - AI-Powered GitHub Repository Analysis Platform
A Spring Boot backend service that leverages AI to analyze, rank, and provide intelligent insights on GitHub repositories across multiple technology categories.

📋 Table of Contents
Overview
Key Features
Technology Stack
Architecture
Prerequisites
Quick Start
API Endpoints
Configuration
Project Structure
Core Components
Caching Strategy
Development Guide
Future Enhancements

 Overview
GitStatus is an intelligent GitHub repository analysis platform designed to help developers discover, understand, and rank popular repositories. It combines GitHub's REST API with advanced AI capabilities (Ollama) to generate meaningful insights and explanations for repositories across diverse technology categories including AI, Backend, Frontend, Security, DevOps, and more.

The application uses a multi-layered architecture with:

MongoDB for persistent data storage
Redis for high-performance caching
Ollama AI for repository understanding and analysis
Spring Boot 3.5.3 with reactive capabilities
✨ Key Features
🔍 Repository Discovery & Cataloging
Multi-category support: Fetch repositories from AI, Backend, Frontend, Security, DevOps, and more categories
Bulk fetching: Retrieve and catalog repositories across entire categories
Real-time metadata: Store stars, forks, watchers, issues, and update timestamps
🤖 AI-Powered Analysis
Intelligent Explanations: Use Ollama AI to generate human-readable explanations of repository purpose and functionality
README Processing: Automatically fetch, cache, and process repository READMEs
Smart Prompt Engineering: Context-aware prompts for accurate AI analysis
📊 Repository Ranking
Advanced Scoring Algorithm: Multi-factor scoring combining:
Stars (50% weight)
Forks (150% weight)
Watchers (30% weight)
Issues (10% weight)
Activity-Based Ranking: Accounts for repository update frequency and recency
Normalized Scores: Percentile-based attention metrics
⚡ Performance Optimization
Redis Caching: 12-hour cache TTL for README files and AI analysis results
Batch Processing: Efficient caching of multiple READMEs in configurable batches
Smart Cache Management: Check, delete, and manage cache entries individually
Spring Cache Integration: Transparent caching with @EnableCaching
📅 Scheduled Operations
Automated Refresh: Configurable cron jobs for repository data synchronization
Background Processing: Non-blocking scheduled tasks with @EnableScheduling
📧 Newsletter & Subscriptions (Infrastructure Ready)
Subscriber Management: Database entities for newsletter subscribers
Category Subscriptions: Users can subscribe to specific repository categories
API Ready: Infrastructure prepared for future newsletter delivery features
🛠 Technology Stack
Component	Technology	Version
Framework	Spring Boot	3.5.3
Runtime	Java	17+
Build Tool	Maven	3.9+
Web Layer	Spring Web MVC + WebFlux	Latest
Database	MongoDB	Latest
Cache	Redis	6.0+
AI Model	Ollama (Gemma3:4b)	Latest
HTTP Client	Spring WebClient & RestClient	6.2.8
Data Processing	Spring Data MongoDB	3.5.1
Validation	Spring Validation	Latest
Code Generation	Project Lombok	Latest
Monitoring	Spring Boot Actuator	Latest
🏗 Architecture
┌─────────────────────────────────────────────────────────┐
│                   API Layer (Controllers)               │
│  RepoAiController │ FetchController │ ReadmeController  │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│              Service Layer                               │
│  RepoAiService  │ ReadmeCacheService  │ RankingService  │
│  GithubFetchService  │  CacheService  │ RepoService      │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│         Data Access & AI Integration Layer              │
│  OllamaService  │ PromptService  │ Repository Layer    │
└────────────────────────┬────────────────────────────────┘
                         │
          ┌──────────────┼──────────────┐
          │              │              │
     ┌────▼─────┐  ┌────▼─────┐  ┌───▼──────┐
     │ MongoDB   │  │  Redis   │  │  Ollama  │
     │ (Primary  │  │  (Cache  │  │   (AI    │
     │  Data)    │  │ & Session)  │ Analysis) │
     └───────────┘  └──────────┘  └──────────┘
📦 Prerequisites
System Requirements
Java 17+ (JDK installed and configured)
Maven 3.9+ (for building)
MongoDB (local or cloud instance)
Redis (local or cloud instance)
Ollama (running locally on default port 11434)
External Services
GitHub API Token (Personal Access Token with repo scope)
MongoDB Atlas (or local MongoDB instance)
Redis Server (local or managed service)
Ollama Server (with Gemma3:4b model installed)
Environment Setup
Install Ollama (if not already installed)

# Download from https://ollama.ai
# Pull the required model
ollama pull gemma3:4b
ollama serve  # Start the Ollama server
Start Redis

# Using Docker (recommended)
docker run -d -p 6379:6379 redis:latest

# Or using local installation
redis-server
Ensure MongoDB is Accessible

Update connection string in application.yml
🚀 Quick Start
1. Clone & Navigate
cd C:\Users\LAPTOP\Downloads\gitStatus\gitStatus
2. Configure Application
Edit application.yml with your credentials:

github:
  token: your_github_pat_token_here

spring:
  data:
    mongodb:
      uri: your_mongodb_connection_string
    redis:
      host: localhost
      port: 6379

ollama:
  base-url: http://localhost:11434
  model: gemma3:4b
3. Build & Run
# Build with Maven
mvn clean install

# Run the application
mvn spring-boot:run

# Or run the JAR directly
java -jar target/gitStatus-0.0.1-SNAPSHOT.jar
4. Verify
# Check if server is running
curl http://localhost:8080/api/fetch/categories
📡 API Endpoints
Repository Discovery
Fetch All Categories
GET /api/fetch/categories
Response: Set of supported repository categories

["ai", "backend", "frontend", "security", "devops", "database", "mobile"]
Fetch All Repositories
GET /api/fetch/all
Response: Success message with fetch count

Fetch Category Repositories
GET /api/fetch/{category}
Parameters:

category (path): Repository category name Response: Fetched count confirmation
AI Analysis
Generate Repository Explanation
GET /api/ai/explain/{githubId}
Parameters:

githubId (path): GitHub repository ID Response:
{
  "id": "...",
  "githubId": 12345,
  "repositoryExplainer": "This repository is...",
  "generatedAt": "2026-07-29T10:30:00"
}
README Management
Get Single README
GET /api/readme/{githubId}
Parameters:

githubId (path): GitHub repository ID Response: README file content
Check if README Cached
GET /api/readme/exists/{githubId}
Response: true or false

Cache All READMEs
POST /api/readme/cache-all
Response: Caching status message

Cache Batch of READMEs
POST /api/readme/cache-batch?batchSize=50
Parameters:

batchSize (query, default: 50): Number of READMEs to cache Response: Success message with count
Delete README Cache
DELETE /api/readme/{githubId}
Response: Deletion confirmation

⚙️ Configuration
Application Properties (application.yml)
spring:
  application:
    name: gitStatus
  
  data:
    mongodb:
      uri: mongodb+srv://user:pass@cluster.mongodb.net/gitStatus
    redis:
      host: localhost
      port: 6379
  
  cache:
    type: redis  # Use Redis as cache backend

github:
  token: your_github_pat_token

scheduler:
  cron: "0 0 2 * * *"  # Daily at 2 AM UTC

server:
  port: 8080

ollama:
  base-url: http://localhost:11434
  model: gemma3:4b
Key Configuration Parameters
README Cache TTL: 12 hours (configurable in ReadmeCacheService)
README Size Limit: 10,000 characters (truncation in RepoAiService)
Default Batch Size: 50 repositories
Cron Schedule: Daily at 2 AM UTC
📁 Project Structure
src/main/java/com/gitStatus/gitStatus/
├── GitStatusApplication.java          # Spring Boot entry point
│
├── config/
│   ├── AppConfig.java                 # Application-wide beans
│   ├── OllamaConfig.java              # Ollama integration config
│   ├── RedisConfig.java               # Redis cache config
│   ├── RestClientConfig.java          # HTTP client beans
│   └── WebClientConfig.java           # WebClient configuration
│
├── controller/
│   ├── RepoAiController.java          # AI analysis endpoints
│   ├── FetchController.java           # Repository fetching
│   ├── ReadmeController.java          # README management
│   ├── AdminController.java           # Admin operations
│   ├── OllamaController.java          # Ollama testing
│   └── RedisTestController.java       # Cache testing
│
├── dto/
│   ├── OllamaRequest.java             # Ollama API request
│   ├── OllamaResponse.java            # Ollama API response
│   ├── RepoResponse.java              # Repository DTO
│   └── NewsletterRequest.java         # Newsletter signup
│
├── entity/
│   ├── RepositoryEntity.java          # Repository database entity
│   └── RepoAiAnalysis.java            # AI analysis entity
│
├── model/
│   ├── RepositoryDocument.java        # MongoDB document model
│   ├── NewsletterSubscriber.java      # Subscriber model
│   ├── Category.java                  # Category model
│   └── StarHistory.java               # Star history tracking
│
├── repository/
│   ├── RepoRepository.java            # Repository data access
│   ├── RepoMongoRepository.java       # MongoDB repository
│   ├── RepoAiAnalysisRepository.java  # AI analysis data access
│   ├── CategoryRepository.java        # Category data access
│   └── StarHistoryRepository.java     # Star history access
│
├── service/
│   ├── GithubFetchService.java        # GitHub API integration
│   ├── ReadmeCacheService.java        # README caching logic
│   ├── CacheService.java              # Generic caching utilities
│   ├── RankingService.java            # Repository ranking algorithms
│   └── RepoService.java               # Core repository logic
│
├── ollamaservices/
│   ├── OllamaService.java             # Ollama API client
│   ├── PromptService.java             # Prompt engineering
│   └── RepoAiService.java             # Repository analysis logic
│
└── scheduler/
    └── RepoScheduler.java             # Scheduled tasks
🔧 Core Components
OllamaService
Interfaces with local Ollama instance for AI model inference.

Method: askModel(prompt: String)
Returns: AI-generated response
Model: Gemma3:4b (configurable)
PromptService
Constructs intelligent prompts for repository analysis.

Method: buildRepositoryPrompt(readme: String)
Features: Context-aware, structured prompts for better AI responses
RepoAiService
Orchestrates AI analysis with caching and persistence.

Caching: Checks Redis before calling Ollama
Persistence: Stores results in MongoDB
Error Handling: Validates README availability
ReadmeCacheService
Manages README fetching and caching lifecycle.

Features:
Fetch from GitHub raw content
Cache in Redis with 12-hour TTL
Batch caching support
Cache deletion & existence checks
RankingService
Calculates repository importance scores.

Score Formula:
rawScore = (stars × 0.50) + (forks × 1.50) + (watchers × 0.30) + (issues × 0.10)
Attention Score: Normalized with recency weighting
Recency Factor: Penalties for older repositories
💾 Caching Strategy
Redis Cache Architecture
Cache Key	TTL	Purpose	Size
readme:{githubId}	12 hours	Cached README files	~100KB avg
repo:{githubId}	24 hours	Repository metadata	~1KB
analysis:{githubId}	24 hours	AI analysis results	~2KB avg
Cache Invalidation
Manual: DELETE endpoint for individual cache clearing
Batch: Automatic when processing new batches
TTL-based: Automatic expiration after configured duration
Cache Hit Benefits
README requests: ~10x faster with cache
AI analysis: Eliminates redundant Ollama calls
Repository metadata: Instant access to frequently queried data
👨‍💻 Development Guide
Building from Source
# Clean build
mvn clean install

# Skip tests for faster builds
mvn clean install -DskipTests

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

Running Tests
mvn test

Code Formatting
Uses Lombok for boilerplate reduction
Follows Spring conventions and best practices
Dependency injection via constructor injection (recommended)
Debugging
Enable debug logging:
logging:
  level:
    com.gitStatus: DEBUG
    org.springframework: INFO
🚧 Future Enhancements
Planned Features
Newsletter System: Automated repository digest delivery
Advanced Filtering: Filter by language, license, contributor count
Trend Analysis: Identify rising star repositories
Custom Scoring: User-defined ranking algorithms
API Authentication: OAuth2/JWT token support
WebSocket Support: Real-time update notifications
GraphQL API: Alternative query interface
Analytics Dashboard: Repository insights visualization
Multi-Model Support: Support for additional AI models
Batch Analysis Jobs: Async processing of large datasets
Performance Roadmap
Database Indexing: Optimize query performance
Connection Pooling: Configure HikariCP settings
Load Balancing: Horizontal scaling support
Microservices: Decompose monolithic architecture
Event-Driven: Kafka integration for async processing
Quality Improvements
Test Coverage: Increase unit and integration test coverage
Error Handling: Comprehensive exception handling
Validation: Input validation and sanitization
Monitoring: Enhanced observability with Spring Cloud Sleuth
Documentation: OpenAPI/Swagger integration
📊 API Usage Examples
Example 1: Discover & Analyze AI Repository
# 1. Get available categories
curl http://localhost:8080/api/fetch/categories

# 2. Fetch AI repositories
curl http://localhost:8080/api/fetch/ai

# 3. Get README for a repository (githubId: 12345)
curl http://localhost:8080/api/readme/12345

# 4. Generate AI explanation
curl http://localhost:8080/api/ai/explain/12345
Example 2: Cache Optimization
# 1. Cache a batch of READMEs
curl -X POST http://localhost:8080/api/readme/cache-batch?batchSize=100

# 2. Check if README is cached
curl http://localhost:8080/api/readme/exists/12345

# 3. Delete cache when needed
curl -X DELETE http://localhost:8080/api/readme/12345
🐛 Troubleshooting
Redis Connection Error
ERROR: Cannot get a resource, pool error Timeout waiting for idle object
Solution: Ensure Redis server is running on localhost:6379

MongoDB Connection Error
ERROR: Exception in initializeObjectsRecursively: connection refused
Solution: Verify MongoDB URI in application.yml

Ollama Model Not Found
ERROR: Model not found: gemma3:4b
Solution:

ollama pull gemma3:4b
ollama serve
GitHub Rate Limiting
ERROR: API rate limit exceeded
Solution: Ensure valid GitHub token is configured

📝 License & Attribution
This project integrates with:

Spring Boot Framework (Apache 2.0)
MongoDB (SSPL/Community)
Redis (BSD)
Ollama (MIT)
🤝 Contributing
Contributions are welcome! Please follow the existing code structure and conventions:

Create feature branches from main
Write meaningful commit messages
Add tests for new features
Update documentation as needed
📧 Contact & Support
For issues, questions, or suggestions, please open an issue in the repository or contact the development team.

🎓 Learning Resources
Spring Boot Documentation
MongoDB Developer Center
Redis Documentation
Ollama Documentation
GitHub API Documentation
Last Updated: July 29, 2026
Version: 0.0.1-SNAPSHOT
Java Target: 17+
Spring Boot Version: 3.5.3
