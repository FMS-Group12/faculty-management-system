import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Object[]> getAllCourses() {
        List<Object[]> data = new ArrayList<>();

        String query = "SELECT c.course_code, c.course_name, l.fullname AS lecturer_name, c.credits FROM courses c JOIN lecturers l ON c.lecturer_id = l.lecturer_id;";

        try (Connection conn = dbc.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                data.add(new Object[]{
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("lecturer_name"),
                        rs.getInt("credits")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean updateCourse(String oldCode, String newCode, String name, int lecturerId, int credits) {

        String query = """
        UPDATE courses
        SET course_code = ?, course_name = ?, lecturer_id = ?, credits = ?
        WHERE course_code = ?
    """;

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newCode);
            pstmt.setString(2, name);
            pstmt.setInt(3, lecturerId);
            pstmt.setInt(4, credits);
            pstmt.setString(5, oldCode);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean insertCourse(String code, String name, int credits, int lecturerId) {
        String query = """
        INSERT INTO courses (course_code, course_name, credits, lecturer_id)
        VALUES (?, ?, ?, ?)
    """;

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, code);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.setInt(4, lecturerId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteCourse(String code) {
        String query = "DELETE FROM courses WHERE course_code = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, code);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public int getLecturerIdByName(String name) {
        String query = "SELECT lecturer_id FROM lecturers WHERE fullname = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("lecturer_id");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }
}
