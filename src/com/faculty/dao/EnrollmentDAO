import java.sql.*;
import java.util.Vector;

public class EnrollmentDAO {

    public Vector<Vector<Object>> getEnrollmentsByUsername(String username) {
        Vector<Vector<Object>> data = new Vector<>();

        // Simplified query: matches username directly against student_id
        String sql = "SELECT e.course_code, c.course_name, e.credits, e.grade " +
                "FROM enrollments e " +
                "JOIN courses c ON e.course_code = c.course_code " +
                "WHERE e.student_id = ?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("course_code"));
                row.add(rs.getString("course_name"));
                row.add(rs.getInt("credits"));
                row.add(rs.getString("grade"));
                data.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching enrollments: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }
}
