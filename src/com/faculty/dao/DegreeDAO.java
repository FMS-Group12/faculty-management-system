import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DegreeDAO {

    // ================= READ =================
    // Used to populate JTable
    public Vector<Vector<Object>> getAllDegrees() {

        Vector<Vector<Object>> data = new Vector<>();

        String sql = "SELECT degree, department_name, no_of_students FROM degrees";

        try (Connection conn = dbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    // ================= CREATE =================
    public boolean addDegree(String degreeName,
                             String departmentName,
                             int numberOfStudents) {

        String sql =
                "INSERT INTO degrees (degree, department_name, no_of_students) " +
                        "VALUES (?, ?, ?)";

        try (Connection conn = dbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, degreeName);
            ps.setString(2, departmentName);
            ps.setInt(3, numberOfStudents);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= UPDATE =================
    public boolean updateDegree(String oldDegreeName,
                                String newDegreeName,
                                String departmentName,
                                int numberOfStudents) {

        String sql =
                "UPDATE degrees " +
                        "SET degree = ?, department_name = ?, no_of_students = ? " +
                        "WHERE degree = ?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newDegreeName);
            ps.setString(2, departmentName);
            ps.setInt(3, numberOfStudents);
            ps.setString(4, oldDegreeName);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= DELETE =================
    public boolean deleteDegree(String degreeName) {

        String sql = "DELETE FROM degrees WHERE degree = ?";

        try (Connection conn = dbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, degreeName);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
