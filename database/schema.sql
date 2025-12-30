USE faculty_management_system;

INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('student1', 'student123', 'STUDENT'),
('lecturer1', 'lecturer123', 'LECTURER');

INSERT INTO departments (department_name, hod_name, staff_count) VALUES
('Computing', 'Dr. Silva', 20),
('Technology', 'Dr. Perera', 15);

INSERT INTO degrees (degree_name, department_id) VALUES
('BSc Computer Science', 1),
('BSc Information Technology', 2);

INSERT INTO students (registration_no, name, email, mobile, degree_id, user_id) VALUES
('CS001', 'Nimal Perera', 'nimal@gmail.com', '0771234567', 1, 2);

INSERT INTO lecturers (name, email, department_id, user_id) VALUES
('Dr. Kamal', 'kamal@gmail.com', 1, 3);

INSERT INTO courses (course_code, course_name, lecturer_id) VALUES
('CS101', 'Programming Fundamentals', 1),
('CS102', 'Database Systems', 1);

INSERT INTO enrollments (student_id, course_id, grade) VALUES
(1, 1, 'A'),
(1, 2, 'B+');
