package view;



import java.sql.*;

public class SignUpDAO {

    public boolean registerUser(String username, String password, String role){
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try(Connection conn = dbc.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            if(conn==null) return false;
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role.toUpperCase());
            return pst.executeUpdate()>0;
        } catch(SQLException e){ e.printStackTrace(); return false; }
    }

    public boolean isUsernameTaken(String username){
        String sql = "SELECT COUNT(*) FROM users WHERE username=?";
        try(Connection conn = dbc.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            if(conn==null) return false;
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1)>0;
        } catch(SQLException e){ e.printStackTrace(); return false; }
    }

    public boolean loginUserWithRole(String username, String password, String role){
        String sql = "SELECT COUNT(*) FROM users WHERE username=? AND password=? AND role=?";
        try(Connection conn = dbc.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            if(conn==null) return false;
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role.toUpperCase());
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1)>0;
        } catch(SQLException e){ e.printStackTrace(); return false; }
    }
}
