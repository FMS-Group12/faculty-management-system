import java.sql.*;
import java.util.*;

public class LectureDAO {

 
    public Vector<Vector<Object>> getAllLecturers() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT l.fullname, d.name, l.courses, l.email, l.mobile_no " +
                "FROM lecturers l " +
                "LEFT JOIN departments d ON l.department_id = d.department_id";

        try (Connection conn = dbc.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("fullname"));
                row.add(rs.getString("name") != null ? rs.getString("name") : "Unassigned");
                row.add(rs.getString("courses"));
                row.add(rs.getString("email"));
                row.add(rs.getString("mobile_no"));
                data.add(row);
            }
        } catch (SQLException e) {
            System.out.println("DAO Error (Fetch): " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

   
    public Map<String, Integer> getDepartmentMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT department_id, name FROM departments";
        try (Connection conn = dbc.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("name"), rs.getInt("department_id"));
            }
        } catch (SQLException e) {
            System.out.println("DAO Error (Map): " + e.getMessage());
        }
        return map;
    }

 
    public boolean addLecturer(String name, String deptId, String courses, String email, String mobile) {
        // Subquery looks for user_id based on name; returns NULL if not found.
        String sql = "INSERT INTO lecturers (fullname, department_id, courses, email, mobile_no, user_id) " +
                "VALUES (?, ?, ?, ?, ?, (SELECT user_id FROM users WHERE username = ? LIMIT 1))";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(deptId));
            pstmt.setString(3, courses);
            pstmt.setString(4, email);
            pstmt.setString(5, mobile);
            pstmt.setString(6, name); // Used for user_id lookup

            return pstmt.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Database Error (Add): " + e.getMessage());
            return false;
        }
    }

    
    public boolean updateLecturer(String name, String deptId, String courses, String email, String mobile, String originalEmail) {
        String sql = "UPDATE lecturers SET fullname=?, department_id=?, courses=?, email=?, mobile_no=? WHERE email=?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(deptId));
            pstmt.setString(3, courses);
            pstmt.setString(4, email);
            pstmt.setString(5, mobile);
            pstmt.setString(6, originalEmail);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.out.println("DAO Error (Update): " + e.getMessage());
            return false;
        }
    }

  
    public boolean deleteLecturer(String email) {
        String sql = "DELETE FROM lecturers WHERE email=?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("DAO Error (Delete): " + e.getMessage());
            return false;
        }
    }
}

