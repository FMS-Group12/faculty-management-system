
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class DegreeDAO {

    // Method to fetch all degrees to populate the table
    public Vector<Vector<Object>> getAllDegrees() {
        Vector<Vector<Object>> data = new Vector<>();
        String query = "SELECT degree, department_name, no_of_students FROM degrees";

        try (Connection conn = dbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("degree"));
                row.add(rs.getString("department_name"));
                row.add(rs.getInt("no_of_students"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean addDegree(String name, String dept, int students) {
        String sql = "INSERT INTO degrees (degree, department_name, no_of_students) VALUES (?, ?, ?)";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, dept);
            pstmt.setInt(3, students);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDegree(String oldName, String newName, String dept, int students) {
        // Note: Using degree name as the key since the ID isn't displayed in your table
        String sql = "UPDATE degrees SET degree = ?, department_name = ?, no_of_students = ? WHERE degree = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, dept);
            pstmt.setInt(3, students);
            pstmt.setString(4, oldName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDegree(String degreeName) {
        String sql = "DELETE FROM degrees WHERE degree = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, degreeName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
