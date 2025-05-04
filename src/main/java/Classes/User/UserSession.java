package Classes.User;

public class UserSession {
    private static String username;
    private static String role;

    public static void setUser(String username, String role) {
        UserSession.username = username;
        UserSession.role = role;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static void clear() {
        username = null;
        role = null;
    }
}