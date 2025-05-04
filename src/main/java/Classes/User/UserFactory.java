package Classes.User;

import Classes.Coordinator.CoordinatorImpl;
import Classes.Employee.EmployeeImpl;
import Classes.Manager.ManagerImpl;
import Classes.Reader.ReaderImpl;

public class UserFactory {
    public static User createUser(String username, String role){
        return switch (role){
            case "Coordinator" -> new CoordinatorImpl(username);
            case "Employee" -> new EmployeeImpl(username);
             case "Manager" -> new ManagerImpl(username);
             case "Reader" -> new ReaderImpl(username);
            default -> null;
        };
    }
}
