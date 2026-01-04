import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class DepartmentDAO {

    private final String DB_URL = "jdbc:mysql://localhost:3306/faculty_management_system";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    
    public Vector<Vector<Object>> getAllDepartments() {
        Vector<Vector<Object>> data = new Vector<>();
        String query = "SELECT * FROM departments";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("department_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("hod"));
                row.add(rs.getInt("no_of_staff"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage());
        }
        return data;
    }


    public boolean addDepartment(String name, String hod, int staff) {
        String query = "INSERT INTO departments (name, hod, no_of_staff) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, hod);
            pstmt.setInt(3, staff);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

 
    public boolean updateDepartment(int id, String name, String hod, int staff) {
        String query = "UPDATE departments SET name=?, hod=?, no_of_staff=? WHERE department_id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, hod);
            pstmt.setInt(3, staff);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public boolean deleteDepartment(int id) {
        String query = "DELETE FROM departments WHERE department_id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
