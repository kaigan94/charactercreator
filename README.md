# 🧙‍♂️ Character Creator – RPG Edition

A fullstack web application for creating and managing fantasy RPG characters.  
Built with **Java Spring Boot** (JDBC/MySQL) in the backend and **React, JavaScript, HTML/CSS** in the frontend.  
Includes a visual UI where you can create characters, assign attributes, manage races and classes – perfect for game dev, world-building or just for fun.

---

## 🧰 Tech Stack

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

## 🚀 Getting Started

### 🔧 Prerequisites
- Java 17+ (preferably 21)
- Node.js & npm
- MySQL Server running (or Docker)
- IntelliJ IDEA or preferred IDE

---

### 📦 Backend Setup (Spring Boot)

```bash
# 1. Klona projektet
git clone https://github.com/kaigan94/charactercreator.git

# 2. Öppna i IntelliJ och bygg Maven-projektet

# 3. Starta MySQL och skapa en databas:
CREATE DATABASE characterdb;

# 4. Uppdatera src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/characterdb
spring.datasource.username=yourUser
spring.datasource.password=yourPassword

# 5. Kör projektet!
