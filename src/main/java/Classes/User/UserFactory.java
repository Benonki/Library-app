package Classes.User;

import Classes.Coordinator.CoordinatorImpl;
import Classes.Employee.EmployeeImpl;
import Classes.Manager.ManagerImpl;
import Classes.Reader.ReaderImpl;

public class UserFactory {
    public static User createUser(String username, String role){
        return switch (role){
            case "Koordynator" -> new CoordinatorImpl(username);
            case "Bibliotekarz" -> new EmployeeImpl(username);
             case "Kierownik" -> new ManagerImpl(username);
             case "Czytelnik" -> new ReaderImpl(username);
            default -> null;
        };
    }
}
