# University Management System (Fullstack Web App)

A professional management system for educational institutions designed to handle student and teacher records with real-time interaction and persistent storage.

## Key Features
- **Full CRUD Operations:** Create, Read, Update, and Delete records for both Students and Teachers.
- **Relational Data:** Established One-to-Many relationships between Curators (Teachers) and Students.
- **Advanced UI/UX:** Interactive dashboard with real-time search, multi-column sorting, and optimized row numbering.
- **RESTful API:** Decoupled backend architecture serving JSON data to a responsive frontend.

## Tech Stack
- **Backend:** Java 21, `com.sun.net.httpserver` (Lightweight REST API)
- **Database:** PostgreSQL 16 (Relational Schema with Foreign Keys)
- **ORM/Data Access:** JDBC with `PreparedStatement` and DAO Pattern
- **Frontend:** Vanilla JavaScript (Fetch API), Bootstrap 5, CSS3
- **Serialization:** Google Gson

## Architecture & Design Patterns
The project follows **SOLID** principles and clean code practices:
- **DAO (Data Access Object):** Encapsulates all database logic.
- **Singleton Pattern:** Manages global database connection pool.
- **Builder Pattern:** Provides flexible object construction for the Student entity.
- **Dependency Inversion:** Controllers depend on repository interfaces, not concrete implementations.

## 📂 Project Structure
```text
src/
├── controller/    # HTTP Handlers & API Endpoints
├── domain/        # Model Entities (Student, Teacher)
├── repository/    # DAO Interfaces & JDBC Implementations
├── db/            # Database Connection Management
└── resources/     # Web Assets (HTML, CSS, JS)
