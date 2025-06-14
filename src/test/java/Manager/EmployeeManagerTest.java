package Classes.Manager;

import Classes.Manager.Util.Employee;
import Server.DatabaseConnection;
import Server.Packet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeManagerTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private PreparedStatement mockCheckStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private ResultSet mockGeneratedKeys;

    @Test
    void testGetEmployees_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            Statement mockStatement = mock(Statement.class);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("Uzytkownik_ID")).thenReturn(1);
            when(mockResultSet.getString("Imie")).thenReturn("John");
            when(mockResultSet.getString("Nazwisko")).thenReturn("Doe");
            when(mockResultSet.getString("Email")).thenReturn("john@example.com");
            when(mockResultSet.getString("Telefon")).thenReturn("123456789");

            Packet result = EmployeeManager.getEmployees();

            assertEquals("GetEmployees", result.type);
            assertEquals("Employees list", result.message);
            assertNotNull(result.employees);
            assertEquals(1, result.employees.size());

            Employee employee = result.employees.get(0);
            assertEquals(1, employee.getId());
            assertEquals("John", employee.getFirstName());
            assertEquals("Doe", employee.getLastName());
            assertEquals("john@example.com", employee.getEmail());
            assertEquals("123456789", employee.getPhone());
        }
    }

    @Test
    void testCreateEmployee_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            Employee newEmployee = new Employee(0, "John", "Doe", "john@example.com", "123456789", "password", 3);
            Packet packet = new Packet("CreateEmployee", "Create employee");
            packet.data = newEmployee;

            when(mockConnection.prepareStatement("SELECT COUNT(*) FROM Uzytkownik WHERE LOWER(Email) = ?"))
                    .thenReturn(mockCheckStatement);
            when(mockCheckStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(0);

            when(mockConnection.prepareStatement(
                    "INSERT INTO Uzytkownik (Imie, Nazwisko, Email, Telefon, Haslo, Rola_ID) VALUES (?, ?, ?, ?, ?, ?)",
                    new String[] {"Uzytkownik_ID"}))
                    .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
            when(mockGeneratedKeys.next()).thenReturn(true);
            when(mockGeneratedKeys.getInt(1)).thenReturn(1);

            Packet result = EmployeeManager.createEmployee(packet);

            assertEquals("CreateEmployee", result.type);
            assertEquals("Employee created successfully", result.message);

            verify(mockCheckStatement).setString(1, "john@example.com");
            verify(mockCheckStatement).executeQuery();

            verify(mockPreparedStatement).setString(1, "John");
            verify(mockPreparedStatement).setString(2, "Doe");
            verify(mockPreparedStatement).setString(3, "john@example.com");
            verify(mockPreparedStatement).setString(4, "123456789");
            verify(mockPreparedStatement).setString(5, "password");
            verify(mockPreparedStatement).setInt(6, 3);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void testUpdateEmployee_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            Employee employee = new Employee(1, "John", "Doe", "john@example.com", "123456789", "", 3);
            Packet packet = new Packet("UpdateEmployee", "Update employee");
            packet.data = employee;

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Packet result = EmployeeManager.updateEmployee(packet);

            assertEquals("UpdateEmployee", result.type);
            assertEquals("Employee updated successfully", result.message);

            verify(mockPreparedStatement).setString(1, "John");
            verify(mockPreparedStatement).setString(2, "Doe");
            verify(mockPreparedStatement).setString(3, "john@example.com");
            verify(mockPreparedStatement).setString(4, "123456789");
            verify(mockPreparedStatement).setInt(5, 1);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void testDeleteEmployee_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            Packet packet = new Packet("DeleteEmployee", "Delete employee");
            packet.data = 1;

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Packet result = EmployeeManager.deleteEmployee(packet);

            assertEquals("DeleteEmployee", result.type);
            assertEquals("Employee deleted", result.message);

            verify(mockPreparedStatement).setInt(1, 1);
            verify(mockPreparedStatement).executeUpdate();
        }
    }
}