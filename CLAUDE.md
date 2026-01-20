# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Environment

**IMPORTANT**: This project is developed on Windows 11 with Git Bash as the shell. Due to this mixed environment, Maven commands must be executed using this specific syntax:

```bash
JAVA_HOME="C:\\Program Files\\Zulu\\zulu-25" cmd //c "cd /d C:\\Users\\eduardo.gonzalezest\\eclipse-workspace\\runnerz && .\\mvnw.cmd <maven-goal>"
```

Replace `<maven-goal>` with the desired Maven command (e.g., `clean package`, `test`, `javadoc:javadoc`).

## Build and Run Commands

This project uses Maven Wrapper (no global Maven installation required).

### Standard syntax (native Windows CMD/PowerShell)

```bash
# Build the project
mvnw.cmd clean package

# Run the application
mvnw.cmd spring-boot:run

# Run all tests
mvnw.cmd test

# Generate Javadoc
mvnw.cmd javadoc:javadoc
```

### Git Bash on Windows syntax (current environment)

```bash
# Build the project
JAVA_HOME="C:\\Program Files\\Zulu\\zulu-25" cmd //c "cd /d C:\\Users\\eduardo.gonzalezest\\eclipse-workspace\\runnerz && .\\mvnw.cmd clean package"

# Run all tests
JAVA_HOME="C:\\Program Files\\Zulu\\zulu-25" cmd //c "cd /d C:\\Users\\eduardo.gonzalezest\\eclipse-workspace\\runnerz && .\\mvnw.cmd test"

# Generate Javadoc
JAVA_HOME="C:\\Program Files\\Zulu\\zulu-25" cmd //c "cd /d C:\\Users\\eduardo.gonzalezest\\eclipse-workspace\\runnerz && .\\mvnw.cmd javadoc:javadoc"

# Run a single test class
JAVA_HOME="C:\\Program Files\\Zulu\\zulu-25" cmd //c "cd /d C:\\Users\\eduardo.gonzalezest\\eclipse-workspace\\runnerz && .\\mvnw.cmd test -Dtest=RunControllerTest"
```

### Linux/macOS syntax

```bash
./mvnw clean package
./mvnw spring-boot:run
./mvnw test
./mvnw javadoc:javadoc
```

## Architecture

This is a Spring Boot 3.5 REST API for tracking running activities, using Java 21.

### Layered Structure

```
dev.danvega.runnerz/
├── Application.java           # Spring Boot entry point
└── run/
    ├── RunController.java     # REST endpoints at /api/runs
    ├── RunRepository.java     # Data access using JdbcClient
    ├── Run.java               # Domain model (Java Record with validation)
    ├── Location.java          # Enum: INDOOR/OUTDOOR
    └── RunJsonDataLoader.java # Loads sample data from runs.json on startup
```

### Key Patterns

- **Repository uses `JdbcClient`** (not JPA/Hibernate) - write raw SQL for database operations
- **Domain model uses Java Records** with Jakarta Bean Validation (`@NotEmpty`, `@Positive`)
- **Run record has a compact constructor** that validates `completedOn` must be after `startedOn`
- **Data initialization**: `RunJsonDataLoader` implements `CommandLineRunner` and loads `/data/runs.json` if the database is empty

### REST API Endpoints

All endpoints are under `/api/runs`:
- `GET /api/runs` - List all runs
- `GET /api/runs/{id}` - Get run by ID
- `POST /api/runs` - Create run (returns 201)
- `PUT /api/runs/{id}` - Update run (returns 204)
- `DELETE /api/runs/{id}` - Delete run (returns 204)

### Database

- H2 in-memory database (auto-configured)
- Schema defined in `src/main/resources/schema.sql`
- H2 console available at `/h2-console` (database name: `runnerz`)
