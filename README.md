# RPG Character Creator

A fullstack playground for building, viewing, and managing tabletop RPG characters. The backend is a Spring Boot + MySQL REST API and this repository contains the React/Vite frontend that talks to it via Axios. Together they cover user onboarding, class browsing, character creation, and persistence with CSRF-protected sessions.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [API Reference](#api-reference)
- [Project Structure](#project-structure)
- [Roadmap](#roadmap)
- [What I Learned](#what-i-learned)
- [Acknowledgements](#acknowledgements)

## Overview
This project started as a way to practice building a real-world Spring Boot API and wiring it up to a modern React UI. The backend exposes endpoints for users, classes, and characters, enforces relationships between entities, and returns DTOs to keep sensitive data hidden. The frontend consumes those endpoints to let players register, log in, browse available classes, assemble their party, and manage saved characters with a polished UI.

## Features
- Auth flow with Spring Session cookies, CSRF protection, and automatic re-login after sign-up.
- Character creator that fetches class details, validates picked skills, and tracks starter equipment.
- Dashboard view that lists characters for the signed-in user with delete and detail panes.
- Global error toasts and optimistic UI updates using Axios interceptors.
- Typed DTOs on the backend plus `@ControllerAdvice` for consistent error responses.
- Service layer encapsulating business rules (skill limits, item defaults, relationships).

## Tech Stack
- **Backend:** Java 21, Spring Boot, Spring Data JPA, Spring Web, Spring Security (session + CSRF), MySQL, Maven.
- **Frontend:** React 19, Vite, React Router, Axios, React Toastify, Framer Motion, Tailwind utilities + custom CSS.
- **Tooling:** ESLint, Prettier-style formatting, JUnit 5, Spring Test, Testcontainers (optional), Maven Surefire.

## Architecture
```
React (Vite dev server) --> Spring Boot REST API --> MySQL
        ^                         |                 |
        |                         +-- DTO / Service |
        +-- Session cookies + CSRF token flow ------+
```
- The frontend stores almost no state locally beyond the logged-in user and fetched resources.
- Axios is configured to send credentials, mirror Spring's CSRF cookie/header names, and show toast notifications on failures.
- Service classes wrap repository calls and run inside `@Transactional` boundaries to keep writes consistent.
- DTOs shape responses so the client never sees hashed passwords or internals.

## Getting Started
### Prerequisites
- Java 21+
- Maven 3.9+
- Node.js 20+
- MySQL 8+ (local instance or Docker container)

### 1. Clone the projects
If you keep backend and frontend in separate folders, clone both:
```bash
# backend
git clone <your-backend-repo-url> rpg-backend

# frontend (this repo)
git clone <this-repo-url> rpg-frontend
```

### 2. Configure the backend
Inside `rpg-backend`:
1. Duplicate `src/main/resources/application-example.yml` (or `application.properties`) to `application.yml`.
2. Update database credentials and session secret:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/rpg
       username: rpg_user
       password: change-me
     jpa:
       hibernate:
         ddl-auto: update
   app:
     security:
       cors-origins: http://localhost:5173
   ```
3. Create the database and user if needed:
   ```sql
   CREATE DATABASE rpg CHARACTER SET utf8mb4;
   CREATE USER 'rpg_user'@'%' IDENTIFIED BY 'change-me';
   GRANT ALL PRIVILEGES ON rpg.* TO 'rpg_user'@'%';
   ```
4. Start the API:
   ```bash
   ./mvnw spring-boot:run
   # or
   mvn spring-boot:run
   ```
   The service listens on `http://localhost:8080` and exposes `/csrf-token`, `/auth/*`, `/users`, `/classes`, `/characters`, etc.

### 3. Configure the frontend
Inside `rpg-frontend`:
1. Install dependencies:
   ```bash
   npm install
   ```
2. The Axios client defaults to `http://localhost:8080`. If your backend lives elsewhere, update `src/api/api.js`.
3. Start the dev server:
   ```bash
   npm run dev
   ```
4. Visit `http://localhost:5173` to sign up and start building characters.

## Running Tests
- **Backend:** `./mvnw test` runs unit, repository, and integration tests for services and controllers.
- **Frontend:** `npm run lint` keeps the React codebase clean. (Add Vitest + React Testing Library when you expand the UI.)

## API Reference
| Endpoint | Method | Description |
| --- | --- | --- |
| `/users/email/{email}` | GET | Retrieve a user DTO by email |
| `/users` | POST | Create a new user |
| `/auth/register` | POST | Register and return a sanitized `UserDto` |
| `/auth/me` | GET | Return the currently authenticated user |
| `/classes` | GET | List all RPG classes |
| `/classes/name/{name}` | GET | Fetch a class with stats, role, and default gear |
| `/classes` | POST | Create a class (admin) |
| `/classes/{id}` | PUT | Update an existing class |
| `/classes` | PUT | Bulk update classes |
| `/classes/{id}` | DELETE | Remove a class |
| `/characters` | GET | List characters (pagination + search) |
| `/characters/search` | GET | Search by name or class |
| `/characters` | POST | Create a new character |
| `/characters/user/{userId}` | GET | List characters for a user |

All endpoints return `ResponseEntity` payloads with explicit HTTP statuses and JSON error bodies defined in the global `@ControllerAdvice`.

## Project Structure
```
rpg-frontend/
|-- src/
|   |-- api/              # Axios instance + typed request helpers
|   |-- components/       # UI building blocks (forms, lists, summaries)
|   |-- contexts/         # React context for authenticated user state
|   |-- pages/            # Route-level screens (auth, dashboard, creator)
|   |-- styles/           # Tailwind overrides + bespoke pixel art CSS
|   |-- utils/            # Shared helpers (formatters, constants)
|   \-- main.jsx          # Vite entry point
\-- package.json
```
The backend project mirrors a standard Spring Boot layout:
```
rpg-backend/
\-- src/main/java/.../character
    |-- controller     # REST controllers & DTO mappers
    |-- service        # Business logic + transactions
    |-- repository     # Spring Data JPA repositories
    \-- entity         # JPA entities (User, Character, Class, Skill, Item)
```

## Roadmap
- Refine the UI with sprite artwork, hover states, and accessibility improvements.
- Add Spring Security login flows (JWT sessions or OAuth) and role-based access control.
- Improve form validation and inline error handling on both client and server.
- Build a "My Characters" dashboard card view with filters and sorting.
- Expand the domain with races, gear, quest tracking, and animations.
- Deploy a hosted demo (Render/Fly.io for backend, Netlify/Vercel for frontend).

## What I Learned
- Designing a layered Spring Boot application with DTOs, service logic, and repository tests.
- Configuring CSRF-protected sessions and sharing them with a React frontend.
- Coordinating React Router, context, and async state while keeping UX smooth.
- Using Axios interceptors for CSRF headers, toast notifications, and auth fallback flows.
- Writing unit, integration, and repository tests to keep regressions in check.

## Acknowledgements
- Thanks to Spring, React, and the open-source community for the tooling.
- Pixel art placeholders from the itch.io community; swap in your own assets when ready.
