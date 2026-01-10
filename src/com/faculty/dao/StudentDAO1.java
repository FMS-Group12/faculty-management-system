import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO1 {
    
    public Student1 getStudentByUsername(String username) {
        String sql = "SELECT s.*, d.degree FROM students s " +
                "JOIN degrees d ON s.degree_id = d.degree_id " +
                "WHERE s.user_id = (SELECT user_id FROM users WHERE username = ?)";
        return executeFetch(sql, username);
    }
    
    public Student1 getStudentById(String id) {
        String sql = "SELECT s.*, d.degree FROM students s " +
                "JOIN degrees d ON s.degree_id = d.degree_id " +
                "WHERE s.student_id = ?";
        return executeFetch(sql, id);
    }
    
    private Student1 executeFetch(String sql, String param) {
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, param);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student1(
                        rs.getString("student_id"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("mobile_no"),
                        rs.getString("degree"),
                        rs.getString("user_id")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public int getDegreeIdByName(String degreeName) {
        String sql = "SELECT degree_id FROM degrees WHERE degree = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, degreeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("degree_id");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public boolean updateStudent(Student1 student) {
       
        int realDegreeId = getDegreeIdByName(student.degree_id);

        String sql = "UPDATE students SET fullname = ?, email = ?, mobile_no = ?, degree_id = ?, user_id = ? WHERE student_id = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.fullname);
            pstmt.setString(2, student.email);
            pstmt.setString(3, student.mobile_no);
            pstmt.setInt(4, realDegreeId);
            pstmt.setString(5, student.user_id);
            pstmt.setString(6, student.student_id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
