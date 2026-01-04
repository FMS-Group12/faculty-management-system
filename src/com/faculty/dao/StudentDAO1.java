import java.sql.*;

public class StudentDAO1 {

    // ==========================================
    // FETCH STUDENT DATA
    // ==========================================
    public Student1 getStudentById(String id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student1(
                        rs.getString("student_id"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("mobile_no"),
                        rs.getString("degree_id"),
                        rs.getString("user_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student: " + e.getMessage());
        }
        return null;
    }

    // ==========================================
    // UPDATE STUDENT DATA (The Fixed Method)
    // ==========================================
    public boolean updateStudent(Student1 student) {
        // Query matches your structure: student_id, fullname, email, mobile_no, degree_id, user_id
        String sql = "UPDATE students SET fullname = ?, email = ?, mobile_no = ?, degree_id = ?, user_id = ? WHERE student_id = ?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting parameters
            pstmt.setString(1, student.fullname);
            pstmt.setString(2, student.email);
            pstmt.setString(3, student.mobile_no);

            // NOTE: If your DB columns degree_id and user_id are INT,
            // MySQL usually handles the conversion from String automatically.
            pstmt.setString(4, student.degree_id);
            pstmt.setString(5, student.user_id);

            // The Primary Key for the WHERE clause
            pstmt.setString(6, student.student_id);

            int rows = pstmt.executeUpdate();

            if (rows == 0) {
                System.out.println("Update failed: No student found with ID: " + student.student_id);
            }

            return rows > 0;

        } catch (SQLException e) {
            // Check your IDE console/terminal for this output!
            // It will tell you if it's a "Duplicate Entry" or "Foreign Key Constraint"
            System.err.println("DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
