# 🎲 RPG Character Creator (Fullstack Project)

A fullstack application for creating, viewing, and managing your own RPG characters.  
The backend is built with **Java using Spring Boot and MySQL**, and the frontend with **React and JavaScript**.  
Great practice for building REST APIs, working with relational databases, and designing component-based UI.

---

## 🛠️ Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- JDBC
- MySQL
- Maven

### Frontend
- React
- JavaScript (ES6)
- HTML5 & CSS3

---

### 📡 API – Example Endpoints

#### 👤 User Management
- `GET /users/email/{email}` – Get user by email
- `POST /users` – Create a new user

#### 🧙 Character Management
- `GET /characters` – List all characters (supports pagination and search)
- `GET /characters/search` – Search characters by name or class
- `POST /characters` – Create a new character

#### 🧩 Class Management
- `GET /classes` – List all classes
- `GET /classes/name/{name}` – Get class by name
- `POST /classes` – Create a new class
- `PUT /classes/{id}` – Update a class
- `PUT /classes` – Update multiple classes
- `DELETE /classes/{id}` – Delete a class

#### ⚙️ Backend Features
- DTOs to hide sensitive info (passwords)
- ResponseEntity with HTTP statuses
- Custom exception handling with @ControllerAdvice
- Transactions with @Transactional
- Entity relationships: users, characters, classes, skills
- Classes can have weapons, armorType, and roles

---

## ✅ Included Tests

- Unit test for `CharacterService.createCharacter()`
- Integration test for `POST /characters`
- Simple repository test using `@DataJpaTest`
- Works for creating users, characters, and classes

---

## 📚 What I Learned

- Building a REST API with Spring Boot
- Working with JPA and entity relationships
- How to use service layers and exception handling
- Connecting React frontend to backend via Axios
- Creating and using DTOs to clean API responses
- How to write unit tests and integration tests

---

## 🛠️ Planned Improvements / To-Do

- Frontend styling with sprites and polish (what I'll continue during summer)
- Login/auth (starting Spring Security course after summer)
- More helpful form validation and error handling
- Dashboard page: “My Characters”
- Expand the character sheet with more animations
- Add more RPG elements (races, gear, quests etc.)
