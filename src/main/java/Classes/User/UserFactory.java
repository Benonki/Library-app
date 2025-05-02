package Classes.User;

import Classes.Coordinator.CoordinatorImpl;

public class UserFactory {
    public static User createUser(String username, String role){
        return switch (role){
            case "Coordinator" -> new CoordinatorImpl(username);
            // case "Worker" -> new
            // case "Manager" -> new
            // case "Customer" -> new
            default -> null;
        };
    }
}
