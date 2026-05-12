# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Use the Maven wrapper (`mvnw.cmd` on Windows, `./mvnw` on Unix). Examples below use the Windows form since this repo is developed on Windows.

- Start the database: `docker compose up -d` (Postgres 15 on host port **5433**, container port 5432)
- Run the app: `mvnw.cmd spring-boot:run` — listens on **8081**
- Build a jar: `mvnw.cmd -DskipTests package`
- Run all tests: `mvnw.cmd test`
- Run a single test class: `mvnw.cmd test "-Dtest=FlowerRepositoryTest"`
- Run a single test method: `mvnw.cmd test "-Dtest=FlowerRepositoryTest#saveAndRetrieveFlower"`
- Build the Docker image: `docker build -t catalog-service .` (multi-stage; final image exposes 8081)

Tests use `@SpringBootTest` and hit the real Postgres configured in `application.yml` — the database container must be up and reachable, otherwise tests fail at context startup.

## Architecture

Spring Boot 3.5.7 / Java 17 microservice exposing a flower catalog over REST. Standard three-layer Spring stack:

- `controller/FlowerController` — `/api/flowers` CRUD (GET list, GET by id, POST, PUT, DELETE). Uses `@Valid` on request bodies to trigger bean-validation on the `Flower` entity. The entity is currently used directly as the request/response DTO.
- `service/FlowerService` — thin pass-through to the repository; `updateFlower` does a load-then-mutate inside `findById(...).map(...)` so it returns `Optional.empty()` when the id is missing (controller maps that to 404).
- `repository/FlowerRepository` — `JpaRepository<Flower, Long>` with no custom queries.
- `model/Flower` — JPA `@Entity` (table `flowers`) with Lombok-generated accessors/builder and Jakarta Validation constraints (`@NotBlank` name, `@NotNull @PositiveOrZero` price).

Schema is managed by Hibernate `ddl-auto: update` against Postgres — no Flyway/Liquibase. There is no global `@ControllerAdvice`, so validation/JPA errors surface as Spring's default error responses.

## Configuration gotchas

- **Datasource credentials disagree between files**: `application.yml` connects as `catalog`/`password`, but `docker-compose.yml` provisions Postgres with `POSTGRES_PASSWORD: catalog`. As-checked-in, the app cannot connect to the compose-managed database without aligning these. Update one side before running the app or tests.
- **Time zone is pinned to `Europe/Kyiv`** in three places: Hibernate JDBC time zone, Jackson serialization, and the Postgres container's `TZ`. Keep them in sync when changing.
- Server port (`8081`) and DB port (`5433` host-side) are non-default — remember when calling the API or connecting a client.
