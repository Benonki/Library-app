package Manager;

import Classes.Manager.Util.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    void testConstructorAndGetters() {
        Employee employee = new Employee(1, "John", "Doe", "john@example.com", "123456789", "password", 3);

        assertEquals(1, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("john@example.com", employee.getEmail());
        assertEquals("123456789", employee.getPhone());
        assertEquals("password", employee.getPassword());
        assertEquals(3, employee.getRoleId());
    }

    @Test
    void testSetters() {
        Employee employee = new Employee(0, "", "", "", "", "", 0);

        employee.setId(2);
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setEmail("jane@example.com");
        employee.setPhone("987654321");
        employee.setPassword("newpass");
        employee.setRoleId(2);

        assertEquals(2, employee.getId());
        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("jane@example.com", employee.getEmail());
        assertEquals("987654321", employee.getPhone());
        assertEquals("newpass", employee.getPassword());
        assertEquals(2, employee.getRoleId());
    }
}