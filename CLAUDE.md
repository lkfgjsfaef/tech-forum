# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build
mvn clean compile          # Compile all modules
mvn clean package -DskipTests  # Package without tests

# Run
mvn spring-boot:run        # Start on port 8080 (dev profile auto-activated)

# Test
mvn test                   # Run all tests
mvn test -pl forum-service -Dtest=ArticleReadServiceImplTest  # Single test class
mvn test -Dtest="ArticleReadServiceImplTest#queryDetail"       # Single test method

# Dependency check
mvn dependency:tree -pl forum-web
```

The project uses Java 17, Spring Boot 3.2.5, and Maven. There is no `mvnw` wrapper — use system `mvn`.

## Module Architecture

Five Maven modules ordered by dependency direction (top → bottom):

| Module | Purpose | Key Content |
|--------|---------|-------------|
| `forum-api` | Pure DTOs, VOs, enums, exceptions | `ResVo<T>` response envelope, `Status` error codes, `PageVo` pagination. No business logic, no dependencies on other modules. |
| `forum-core` | Shared technical infrastructure | MDC tracing (`MdcUtil`, `TraceIdFilter`), `@Permission` annotation, Caffeine cache config, WebSocket utils, async executor, sensitive-word filter |
| `forum-service` | All business logic | Article domain split into `ArticleReadServiceImpl` (8 query sub-modules) and `ArticleWriteServiceImpl`, plus user, comment, chat, notification, statistics, AI agent services. Uses MyBatis-Plus mappers under `mapper/` and custom DAOs under `repository/dao/`. |
| `forum-web` | HTTP layer: controllers, views, filters | `front/` for user-facing, `admin/` for admin. `hook/filter/` for TraceId + ReqRecord + OpenApi filters. `hook/interceptor/` for rate limiting + global view. `global/` for exception handlers and SEO injection. |
| `forum-ui` | Thymeleaf HTML templates | No Java code — only `src/main/resources/templates/` with article, user, tag pages. |

Base package: `com.example.forum`. The `forum-web` module is the deployable artifact (contains `ForumApplication.java` and `application.yml`).

## Key Architecture Patterns

### Read/Write Separation
Article queries use `ArticleReadServiceImpl` — handles detail, list, category/tag filter, search, ranking, recommendations, and column views. Mutations use `ArticleWriteServiceImpl` — handles publish, edit, unpublish, delete. Read services can independently tune caching and indexing.

### Event-Driven Async
Article publish fires `ArticlePublishEvent` via Spring Event. Listeners (`@EventListener` + `@Async`) handle cache eviction, fan notifications, and index updates — decoupled from the main write flow. Reuse `pai-async-*` thread pool from `ForumApplication.primaryTaskExecutor()`.

### Filter Chain & Tracing
Three `OncePerRequestFilter` (priority order): `TraceIdFilter` (generates X-Trace-Id in MDC + response header) → `ReqRecordFilter` (request logging) → `OpenApiFilter` (API key auth). For cross-thread tracing, use `MdcUtil.getCopyOfContextMap()`.

### Permission Model
`@Permission` annotation with `UserRole` enum (ALL / LOGIN / ADMIN). `GlobalViewInterceptor` enforces role checks. REST endpoints return 403 JSON; page requests redirect to login.

### Rate Limiting
`RateLimitInterceptor` uses Redis sliding window, keyed by IP + URI. Applied to `/**` excluding admin and static paths. Registered in `ForumApplication.addInterceptors()`.

### Exception Handling
`ForumExceptionHandler` (implements `HandlerExceptionResolver`, priority 0) catches REST exceptions and returns `ResVo<T>` with status codes. `GlobalExceptionHandler` (`@RestControllerAdvice`) catches remaining exceptions. Use `ForumException` / `ForumAdviceException` from forum-api for business errors.

### Unified Response
All REST endpoints return `ResVo<T>`:
```java
ResVo.ok(data)     // Status(200, "OK")
ResVo.fail(status) // Custom Status enum from forum-api
```
Page renders use `BaseViewController` which populates SEO tags, current user, and global sidebar data.

### Caching
Caffeine (local, configured in `ForumCoreAutoConfig` with `recordStats()`) + Redis (distributed). Stats logged every 30 seconds in dev.

### Database
Liquibase manages schema changes (`master.xml` references changelog files). MyBatis-Plus for ORM with XML mappers under `mapper/` directories. `@MapperScan` configured on `ForumApplication`.

### API Documentation
Knife4j (OpenAPI 3) at `/doc.html`. Two groups: admin (`com.example.forum.web.admin`) and front (`com.example.forum.web.front`).

## Profile-Specific Config

The active profile is `dev` by default. Resource files follow this priority:
1. `src/main/resources/application*.yml` (base configs)
2. `src/main/resources-env/dev/application*.yml` (dev overrides — filtered via Maven resource plugin)

Key config files: `application-dal.yml` (MySQL + Redis), `application-web.yml` (cookie domain, view prefix), `application-ai.yml` (model providers), `application-image.yml` (OSS), `application-rabbitmq.yml`, `application-pay.yml`, `application-login.yml`.

## Agent Module

An independent `Agent/` directory exists at project root — a separate Spring Boot app for AI chat with `src/main/resources/application.yml`. It runs alongside the main app and provides the `/chat/api/` endpoints used by the WebSocket stomp layer in `forum-web`.
