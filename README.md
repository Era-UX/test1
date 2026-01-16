Education Management System (Java + PostgreSQL)
Project Overview
This project is a Java-based application for managing students and teachers using a PostgreSQL database. It demonstrates the use of JDBC, DAO (Data Access Object) pattern, and Custom Exception Handling.

Key Features (CRUD Operations)
Create: Adding new students and teachers to the database.
Read: Fetching and displaying lists of students/teachers from PostgreSQL.
Update: Updating student age or teacher's experience by their unique ID/Name.
Delete: Removing records from the database.

Project Structure
DatabaseManager.java: Handles the connection to the PostgreSQL server.
StudentDAO.java & TeacherDAO.java: Contain all SQL logic (CRUD) separated from the main business logic.
StudentNotFoundException.java & DatabaseConnectionException.java: Custom exceptions for robust error handling.
tables.sql: SQL script to initialize the database schema.

How to Run
Execute the tables.sql script in your PostgreSQL database to create necessary tables.
Update the credentials (URL, username, password) in DatabaseManager.java.
Run the Main.java class to see the CRUD operations in action.
