INSERT INTO users (username, password, role) VALUES ("CS/2022/060","Sanda123","STUDENT");

INSERT INTO users (username, password, role) VALUES ("Silva","Silva123","LECTURER");

INSERT INTO departments (name, hod, no_of_staff) VALUES ('Software Engineering', 'Dr.S.P.Kasthuri Arachchi', 25);

INSERT INTO departments (name, hod, no_of_staff) VALUES ('Computer Systems Engineering', 'Prof.N.G.J.Dias', 32);

INSERT INTO departments (name, hod, no_of_staff) VALUES ('Applied Computing', 'Dr. L.S.I.Liyanage', 28);

INSERT INTO degrees (degree,department_name, no_of_students) VALUES ('BSc CS', 'Software Engineering', 280);

INSERT INTO degrees (degree, department_name, no_of_students) VALUES ('BICT','Applied Computing' , 360);

INSERT INTO degrees (degree, department_name, no_of_students) VALUES ('BET','Computer Systems Engineering' , 320);

INSERT INTO degrees (degree, department_name, no_of_students) VALUES ('BST', 'Computer Systems Engineering', 300);

INSERT INTO students (student_id, fullname, email, mobile_no, degree_id, user_id) VALUES ('CS/2022/060','Sandamini Madurawinda','sandamini@gmail.com','0712345678',1,1);
    
INSERT INTO lecturers(fullname, email, mobile_no, department_id, user_id) VALUES ('Kamal Silva','silva@gmail.com','0709876543',3,2);

INSERT INTO courses(course_code, course_name, credits, lecturer_id) VALUES ('CSCI 21052','OOP',2,1);

INSERT INTO enrollments(student_id, course_code, grade) VALUES ('CS/2022/060','CSCI 21052','A');




