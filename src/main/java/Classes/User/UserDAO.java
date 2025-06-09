package Classes.User;

import Server.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public static int getUserIdByUsername(String username) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "SELECT uzytkownik_id FROM uzytkownik WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("uzytkownik_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
