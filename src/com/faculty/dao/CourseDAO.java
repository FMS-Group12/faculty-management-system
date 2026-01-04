import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // ======================================
    // GET ALL COURSES
    // ======================================
    public List<Object[]> getAllCourses() {
        List<Object[]> data = new ArrayList<>();
        // Fetching lecturer_name directly as requested
        String query = "SELECT course_code, course_name, lecturer_name, credits FROM courses";

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

    // ======================================
    // UPDATE COURSE (The logic that allows editing)
    // ======================================
    public boolean updateCourse(String oldCode, String newCode, String name, String lecturer, int credits) {
        String query = "UPDATE courses SET course_code = ?, course_name = ?, lecturer_name = ?, credits = ? WHERE course_code = ?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newCode);
            pstmt.setString(2, name);
            pstmt.setString(3, lecturer);
            pstmt.setInt(4, credits);
            pstmt.setString(5, oldCode); // Locate the original record by its old code

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertCourse(String code, String name, int credits, String lecturerName) {
        String query = "INSERT INTO courses (course_code, course_name, credits, lecturer_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.setString(4, lecturerName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCourse(String code) {
        String query = "DELETE FROM courses WHERE course_code = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, code);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
