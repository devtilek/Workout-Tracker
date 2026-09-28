# Workout Tracker API

A backend REST API for planning workouts, tracking training sessions, logging sets, and viewing basic progress statistics.

Built as a portfolio project with Java and Spring Boot, with a focus on layered architecture, JWT authentication, PostgreSQL, Flyway migrations, validation, and testable business logic.

## Features

- JWT-based registration and login
- BCrypt password hashing
- Exercise catalog with muscle-group filtering
- Personal workout plans
- Add/remove exercises from plans
- Workout sessions linked to plans
- Log sets with reps, weight, duration, and distance
- Complete and delete workout sessions
- Paginated workout history
- Basic training summary
- PostgreSQL schema managed by Flyway
- Global validation and HTTP error handling
- Unit tests for authentication and session rules

## Tech Stack

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- JWT (JJWT)
- PostgreSQL
- Flyway
- Gradle
- Lombok
- JUnit 5 + Mockito + AssertJ

## Architecture

The project follows a simple layered architecture:

```
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL

Security: JWT → CurrentUser → protected services
Database: Flyway migrations → Hibernate validation
```

### Package structure

```
com.workouttracker
├── config
├── controller
├── domain
├── dto
├── repository
├── security
└── service
```

Controllers handle HTTP requests, services contain business rules, repositories handle persistence, and DTOs keep the API contract separate from JPA entities.

## API

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Login and receive JWT |

### Exercises

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/exercises` | List exercises |
| GET | `/api/exercises?muscleGroup=Chest` | Filter by muscle group |
| GET | `/api/exercises/{id}` | Get exercise |

### Workout plans

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/plans` | Create a plan |
| GET | `/api/plans` | List current user's plans |
| GET | `/api/plans/{id}` | Get a plan |
| POST | `/api/plans/{id}/exercises` | Add exercise |
| DELETE | `/api/plans/{id}/exercises/{planExerciseId}` | Remove exercise |
| DELETE | `/api/plans/{id}` | Delete plan |

### Workout sessions

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/sessions` | Start a workout |
| GET | `/api/sessions` | Paginated workout history |
| GET | `/api/sessions/summary` | User training summary |
| GET | `/api/sessions/{id}` | Get workout details |
| POST | `/api/sessions/{id}/sets` | Log a set |
| POST | `/api/sessions/{id}/complete` | Complete workout |
| DELETE | `/api/sessions/{id}` | Delete workout |

Protected endpoints require:

```
Authorization: Bearer <jwt-token>
```

## Database

Flyway creates:

- `users`
- `exercises`
- `workout_plans`
- `plan_exercises`
- `workout_sessions`
- `session_sets`

The initial migration also seeds a basic exercise catalog.

## Run locally

### Requirements

- JDK 25
- PostgreSQL
- Gradle Wrapper

Create a PostgreSQL database:

```sql
CREATE DATABASE workout_tracker;
```

Set environment variables if you do not want to use the local defaults:

```bash
DB_URL=jdbc:postgresql://localhost:5432/workout_tracker
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=replace-with-a-long-random-secret
JWT_EXPIRATION_MS=604800000
```

Run:

```bash
./gradlew bootRun
```

Windows:

```bash
gradlew.bat bootRun
```

The API starts on `http://localhost:8080`.

## Testing

Run unit tests with:

```bash
./gradlew test
```

The current tests cover JWT round-trip behavior, registration, and session business rules.

## Roadmap

- CRUD for exercise catalog administration
- Update workout plans and plan exercises
- Edit/delete individual logged sets
- Progress endpoints and personal records
- Better statistics and volume calculations
- OpenAPI / Swagger documentation
- Docker Compose for PostgreSQL + API
- Integration tests with an isolated test database
