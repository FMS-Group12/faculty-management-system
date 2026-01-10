INSERT INTO users (username, password, role) VALUES ("CS/01","Stu01","STUDENT");

INSERT INTO users (username, password, role) VALUES ("LC/01","Lec01","LECTURER");

INSERT INTO users( username, password, role) VALUES ('AD/00','Admin00','ADMIN');

INSERT INTO departments (name, hod, no_of_staff) VALUES ('Software Engineering', 'Dr.S.P.Kasthuri Arachchi', 25);

INSERT INTO departments (name, hod, no_of_staff) VALUES ('Computer Systems Engineering', 'Prof.N.G.J.Dias', 32);

INSERT INTO departments (name, hod, no_of_staff) VALUES ('Applied Computing', 'Dr. L.S.I.Liyanage', 28);

INSERT INTO degrees (degree,department_name, no_of_students) VALUES ('BSc CS', 'Software Engineering', 280);

INSERT INTO degrees (degree, department_name, no_of_students) VALUES ('BICT','Applied Computing' , 360);

INSERT INTO degrees (degree, department_name, no_of_students) VALUES ('BET','Computer Systems Engineering' , 320);

INSERT INTO degrees (degree, department_name, no_of_students) VALUES ('BST', 'Computer Systems Engineering', 300);

INSERT INTO students (student_id, fullname, email, mobile_no, degree_id, user_id) VALUES ('CS/01','Sanduni Perera','sanduni@gmail.com','0712345678',1,1);
    
INSERT INTO lecturers(fullname, email, mobile_no, department_id,courses, user_id) VALUES ('Kamal Silva','silva@gmail.com','0709876543',3,'ETEC21062',2);

INSERT INTO courses(course_code, course_name, credits, lecturer_id) VALUES ('CSCI 21052','OOP',2,1);

INSERT INTO enrollments(student_id, course_code, grade) VALUES ('CS/01','CSCI 21052','A');







