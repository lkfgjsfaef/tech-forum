# TechForum

A full-featured community forum platform built with Spring Boot, designed for developer communities.

## Tech Stack

- **Backend**: Spring Boot 3.2.5, MyBatis-Plus, MySQL, Redis
- **Real-time**: WebSocket (STOMP), Server-Sent Events (SSE)
- **AI**: Spring AI with multi-model routing (ZhiPu / DeepSeek / OpenAI)
- **Frontend**: Thymeleaf, Bootstrap, Vanilla JS
- **Infra**: Liquibase, Knife4j (OpenAPI 3), Caffeine Cache
- **Java**: JDK 17

## Features

- Article publishing & management with Markdown support
- Nested comments (threaded replies)
- Real-time chat via WebSocket
- AI coding assistant with streaming responses
- User following & notifications
- Paid content & reading type control
- Full-text search
- SEO-friendly URL slugs
- Rate limiting & permission-based access control
- Distributed tracing with TraceId

## Quick Start

### Prerequisites

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7.0+

### Setup

```bash
# 1. Clone
git clone <your-repo-url>
cd tech-forum

# 2. Configure environment
cp .env.example .env
# Edit .env with your database credentials and API keys

# 3. Initialize database
mysql -u root -p < pai_coding.sql

# 4. Build & run
mvn clean compile
mvn spring-boot:run
```

Visit `http://localhost:8080` to access the forum.

### API Documentation

Once running, visit `http://localhost:8080/doc.html` for the Knife4j API docs.

## Project Structure

```
├── forum-api/         # DTOs, VOs, enums — pure interface definitions
├── forum-core/        # Shared infrastructure: MDC, cache, WebSocket, AOP
├── forum-service/     # Business logic: articles, users, comments, chat
├── forum-web/         # Controllers, filters, interceptors, Thymeleaf views
├── forum-ui/          # HTML templates
├── Agent/             # Standalone AI chat service
└── pai_coding.sql     # Database schema
```

## Configuration

All sensitive config values are externalized via environment variables. See `.env.example` for the full list:

| Variable | Description |
|----------|-------------|
| `MYSQL_PASSWORD` | MySQL password |
| `REDIS_PASSWORD` | Redis password |
| `JWT_SECRET` | JWT signing key (min 32 chars) |
| `AI_ZHIPU_API_KEY` | ZhiPu AI API key |

## License

Apache License 2.0
