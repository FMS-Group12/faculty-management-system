CREATE DATABASE faculty_management_system;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','STUDENT','LECTURER') NOT NULL
);

CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    hod VARCHAR(100),
    no_of_staff INT   
);

CREATE TABLE degrees (
    degree_id INT AUTO_INCREMENT PRIMARY KEY,
    degree VARCHAR(100),
    department_name VARCHAR(200),
    no_of_students INT
);

CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    fullname VARCHAR(100),
    email VARCHAR(100),
    mobile_no VARCHAR(15),
    degree_id INT,
    user_id INT,
    FOREIGN KEY (degree_id) REFERENCES degrees(degree_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE lecturers (
    lecturer_id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100),
    email VARCHAR(100),
    mobile_no VARCHAR(15),
    department_id INT,
    user_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE courses (
    course_code VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100),
    credits INT,
    lecturer_id INT,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id)
);

CREATE TABLE enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20),
    course_code VARCHAR(20),
    grade VARCHAR(5),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_code) REFERENCES courses(course_code)
);



