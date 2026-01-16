-- Database schema for Education project

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    age INT
);

CREATE TABLE IF NOT EXISTS teachers (
    name VARCHAR(100) PRIMARY KEY,
    age INT,
    subject VARCHAR(100),
    exp_years INT
);