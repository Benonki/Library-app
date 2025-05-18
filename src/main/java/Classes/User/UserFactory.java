package Classes.User;

import Classes.Coordinator.CoordinatorImpl;
import Classes.Employee.EmployeeImpl;
import Classes.Manager.ManagerImpl;
import Classes.Reader.ReaderImpl;

public class UserFactory {
    public static User createUser(String username, String role){
        return switch (role){
            case "koordynator" -> new CoordinatorImpl(username);
            case "bibliotekarz" -> new EmployeeImpl(username);
             case "kierownik" -> new ManagerImpl(username);
             case "czytelnik" -> new ReaderImpl(username);
            default -> null;
        };
    }
}
