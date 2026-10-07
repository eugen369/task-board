# Task Board

A mini Trello clone as a full-stack learning project.

**Live demo:** [task-board.deibert.me](https://task-board.deibert.me)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 22, TypeScript, RxJS |
| Backend | Java 21, Spring Boot 3, Spring Data JPA |
| Database | H2 (file-based) |
| Deployment | nginx, systemd, Rocky Linux 8 |

---

## Features

- Create and delete boards
- Create tasks, change status (Todo → In Progress → Done), delete tasks
- REST API with correct HTTP semantics (GET, POST, PATCH, DELETE)
- Global exception handling in the backend (`@RestControllerAdvice`)
- HTTP interceptor in the frontend for centralized error handling
- Angular environments for local development and production
- CORS configuration
- Dark theme

---

## Project Structure

```
task-board/
├── task-board-backend/    # Spring Boot REST API
└── task-board-frontend/   # Angular SPA
```

---

## Run Locally

**Backend:**
```bash
cd task-board-backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
→ API at `http://localhost:8080`, H2 console at `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:file:./data/taskboard`)

**Frontend:**
```bash
cd task-board-frontend
npm install
npm start
```
→ App at `http://localhost:4200`

---

## Background

This project was built as a learning project to explore Java and Spring Boot –
as a complement to my main stack of JavaScript/TypeScript/Angular.

Developed with the support of [pi](https://pi.dev) (AI coding agent) and
Claude Sonnet as the model. Using AI tools was itself part of the learning goal:
how to integrate coding agents effectively and securely into everyday development
(Docker isolation, configurable extensions).

---
---

# Task Board

Ein Mini-Trello als Full-Stack-Lernprojekt.

**Live-Demo:** [task-board.deibert.me](https://task-board.deibert.me)

---

## Tech-Stack

| Schicht | Technologie |
|---|---|
| Frontend | Angular 22, TypeScript, RxJS |
| Backend | Java 21, Spring Boot 3, Spring Data JPA |
| Datenbank | H2 (dateibasiert) |
| Deployment | nginx, systemd, Rocky Linux 8 |

---

## Features

- Boards anlegen und löschen
- Tasks erstellen, Status ändern (Todo → In Bearbeitung → Erledigt), löschen
- REST-API mit korrekter HTTP-Semantik (GET, POST, PATCH, DELETE)
- Globales Exception Handling im Backend (`@RestControllerAdvice`)
- HTTP Interceptor im Frontend für zentrale Fehlerbehandlung
- Angular Environments für lokale Entwicklung und Produktion
- CORS-Konfiguration
- Dark Theme

---

## Projektstruktur

```
task-board/
├── task-board-backend/    # Spring Boot REST-API
└── task-board-frontend/   # Angular SPA
```

---

## Lokal starten

**Backend:**
```bash
cd task-board-backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
→ API unter `http://localhost:8080`, H2-Konsole unter `http://localhost:8080/h2-console`
(JDBC-URL: `jdbc:h2:file:./data/taskboard`)

**Frontend:**
```bash
cd task-board-frontend
npm install
npm start
```
→ App unter `http://localhost:4200`

---

## Hintergrund

Dieses Projekt entstand als Lernprojekt, um Java und Spring Boot zu erkunden.

Entwickelt mit Unterstützung von [pi](https://pi.dev) (AI Coding Agent) und
Claude Sonnet 4.6 als Modell. Der Einsatz von AI-Tools war dabei selbst Teil des
Lernziels: wie integriert man Coding Agents sinnvoll und sicher in den
Entwickleralltag (Docker-Isolation, konfigurierbare Extensions)?
