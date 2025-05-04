package Classes.User;

import Server.DatabaseConnection;
import Server.Packet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class User implements Serializable {

    protected String username;
    protected String role;

    public User (String username, String role){
        this.username = username;
        this.role = role;
    }

    public String getUsername(){
        return username;
    }

    public String getRole(){
        return role;
    }

    public static String login(String username, String password){
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlQuery = "SELECT * FROM usersInfo WHERE username = ? AND password = ?";

            try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");
                        System.out.println("Login Successful for user: " + username + " with role: " + role);
                        return role;
                    } else {
                        System.out.println("Invalid credentials for user: " + username);
                        return "";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public abstract Packet handlePacket(Packet packet);
}
