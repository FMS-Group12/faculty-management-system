import java.sql.Connection;
import java.sql.ResultSet;

public class StudentController {

    public ResultSet getAllStudents(Connection con) throws Exception {
        return StudentDAO.getAllStudents(con);
    }

    public void addStudent(Connection con, String studentId, String name,
                           String degree, String email, String mobile) throws Exception {
        StudentDAO.insertStudent(con, studentId, name, degree, email, mobile);
    }

    public void updateStudent(Connection con, String studentId, String name,
                              String degree, String email, String mobile) throws Exception {
        StudentDAO.updateStudent(con, studentId, name, degree, email, mobile);
    }

    public void deleteStudent(Connection con, String studentId) throws Exception {
        StudentDAO.deleteStudent(con, studentId);
    }
}
