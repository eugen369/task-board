# Task Board – Backend

Spring Boot REST API for the Task Board project.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 (file-based, configurable via `DB_PATH`)
- Maven

## Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/boards` | Get all boards |
| POST | `/api/boards` | Create board |
| GET | `/api/boards/{id}` | Get board by ID |
| DELETE | `/api/boards/{id}` | Delete board |
| GET | `/api/boards/{id}/tasks` | Get tasks for a board |
| POST | `/api/boards/{id}/tasks` | Create task |
| PATCH | `/api/boards/{id}/tasks/{taskId}/status` | Update task status |
| DELETE | `/api/boards/{id}/tasks/{taskId}` | Delete task |

## Run

```bash
# Development (H2 console active, DB stored locally under ./data/)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Production (H2 console disabled, DB path via environment variable)
DB_PATH=/var/lib/task-board/db java -jar task-board.jar
```

## Build

```bash
./mvnw clean package -DskipTests
```
→ JAR at `target/task-board-backend-0.0.1-SNAPSHOT.jar`

---
---

# Task Board – Backend

Spring Boot REST-API für das Task-Board-Projekt.

## Tech-Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 (dateibasiert, konfigurierbar per `DB_PATH`)
- Maven

## Endpunkte

| Methode | Pfad | Beschreibung |
|---|---|---|
| GET | `/api/boards` | Alle Boards |
| POST | `/api/boards` | Board anlegen |
| GET | `/api/boards/{id}` | Board by ID |
| DELETE | `/api/boards/{id}` | Board löschen |
| GET | `/api/boards/{id}/tasks` | Tasks eines Boards |
| POST | `/api/boards/{id}/tasks` | Task anlegen |
| PATCH | `/api/boards/{id}/tasks/{taskId}/status` | Status ändern |
| DELETE | `/api/boards/{id}/tasks/{taskId}` | Task löschen |

## Starten

```bash
# Entwicklung (H2-Konsole aktiv, DB lokal unter ./data/)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Produktion (H2-Konsole deaktiviert, DB-Pfad per Umgebungsvariable)
DB_PATH=/var/lib/task-board/db java -jar task-board.jar
```

## Paket bauen

```bash
./mvnw clean package -DskipTests
```
→ JAR unter `target/task-board-backend-0.0.1-SNAPSHOT.jar`
