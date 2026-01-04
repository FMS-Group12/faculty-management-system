package view;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class LectureDAO {

    /**
     * READ: Fetches data using an INNER JOIN to show Department Name
     * instead of the Department ID.
     */
    public void getAllLecturers(DefaultTableModel model) {
        model.setRowCount(0);
        // Updated: Selecting d.name instead of d.department_name
        String sql = "SELECT l.fullname, d.name, l.courses, l.email, l.mobile_no " +
                "FROM lecturers l " +
                "LEFT JOIN departments d ON l.department_id = d.department_id";

        try (Connection conn = dbc.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("fullname"),
                        rs.getString("name"),       // Matches the 'name' column in departments table
                        rs.getString("courses"),
                        rs.getString("email"),
                        rs.getString("mobile_no")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching lecturers: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Inside LectureDAO.java

    // 1. Method to get departments for the dropdown
    public java.util.Map<String, Integer> getDepartmentMap() {
        java.util.Map<String, Integer> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT department_id, name FROM departments";
        try (Connection conn = dbc.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("name"), rs.getInt("department_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // 2. Updated Add method to include user_id lookup
    public boolean addLecturer(String name, String deptId, String courses, String email, String mobile) {
        // We look up the user_id from the users table using the name/username
        String sql = "INSERT INTO lecturers (fullname, department_id, courses, email, mobile_no, user_id) " +
                "VALUES (?, ?, ?, ?, ?, (SELECT user_id FROM users WHERE username = ? LIMIT 1))";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(deptId));
            pstmt.setString(3, courses);
            pstmt.setString(4, email);
            pstmt.setString(5, mobile);
            pstmt.setString(6, name); // Using name to find the matching user_id

            return pstmt.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.out.println("DAO Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * UPDATE: Updates a record based on the unique email.
     */
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
            System.out.println("Error updating lecturer: " + e.getMessage());
            return false;
        }
    }

    /**
     * DELETE: Deletes a record from the database using email.
     */
    public boolean deleteLecturer(String email) {
        String sql = "DELETE FROM lecturers WHERE email=?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting lecturer: " + e.getMessage());
            return false;
        }
    }
}