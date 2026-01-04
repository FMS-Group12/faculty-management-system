import java.sql.*;
import java.util.Vector;

public class StudentDAO {

    /**
     * Fetch all students with degree info
     */
    public static ResultSet getAllStudents(Connection con) throws SQLException {
        String sql = "SELECT s.fullname, s.student_id, d.degree, s.email, s.mobile_no, " +
                "u.username AS user_name " +
                "FROM students s " +
                "JOIN degrees d ON s.degree_id = d.degree_id " +
                "LEFT JOIN users u ON s.user_id = u.user_id";

        Statement st = con.createStatement();
        return st.executeQuery(sql);
    }

    /**
     * Insert new student with automatic user_id lookup
     */
    public static void insertStudent(Connection con, String studentId, String name, String degree, String email, String mobile) throws SQLException {
        String sql = "INSERT INTO students(student_id, fullname, degree_id, email, mobile_no, user_id) " +
                "VALUES (?, ?, (SELECT degree_id FROM degrees WHERE degree = ? LIMIT 1), ?, ?, " +
                "(SELECT user_id FROM users WHERE username = ? LIMIT 1))";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, studentId);
            pst.setString(2, name);
            pst.setString(3, degree);
            pst.setString(4, email);
            pst.setString(5, mobile);
            pst.setString(6, name); // username lookup in users table
            pst.executeUpdate();
        }
    }

    /**
     * Update student info
     */
    public static void updateStudent(Connection con, String studentId, String name, String degree, String email, String mobile) throws SQLException {
        String sql = "UPDATE students SET fullname = ?, " +
                "degree_id = (SELECT degree_id FROM degrees WHERE degree = ? LIMIT 1), " +
                "email = ?, mobile_no = ? WHERE student_id = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, degree);
            pst.setString(3, email);
            pst.setString(4, mobile);
            pst.setString(5, studentId);
            pst.executeUpdate();
        }
    }

    /**
     * Delete student by student_id
     */
    public static void deleteStudent(Connection con, String studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, studentId);
            pst.executeUpdate();
        }
    }
}
