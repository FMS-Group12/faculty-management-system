import java.sql.*;

public class dbc {
    private static final String url = "jdbc:mysql://localhost:3306/faculty_management_system?serverTimezone=UTC";
    private static final String uname = "root";
    private static final String password = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // optional for modern Java
            return DriverManager.getConnection(url, uname, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
