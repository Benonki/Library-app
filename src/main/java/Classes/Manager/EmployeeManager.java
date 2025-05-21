package Classes.Manager;

import Server.DatabaseConnection;
import Server.Packet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {

    public static Packet getEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT u.Uzytkownik_ID, u.Imie, u.Nazwisko, u.Email, u.Telefon " +
                "FROM Uzytkownik u WHERE u.Rola_ID = 3";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("Uzytkownik_ID"),
                        rs.getString("Imie"),
                        rs.getString("Nazwisko"),
                        rs.getString("Email"),
                        rs.getString("Telefon"),
                        "",
                        3
                );
                employees.add(employee);
            }
            return Packet.withEmployees("GetEmployees", "Employees list", employees);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("GetEmployees", "Error getting employees");
        }
    }


    public static Packet createEmployee(Packet packet) {
        Employee newEmployee = (Employee) packet.data;

        if (newEmployee.getFirstName() == null || newEmployee.getFirstName().trim().isEmpty() ||
                newEmployee.getLastName() == null || newEmployee.getLastName().trim().isEmpty() ||
                newEmployee.getEmail() == null || newEmployee.getEmail().trim().isEmpty() ||
                newEmployee.getPassword() == null || newEmployee.getPassword().trim().isEmpty()) {
            return new Packet("CreateEmployee", "All required fields must be filled");
        }

        String normalizedEmail = newEmployee.getEmail().toLowerCase().trim();

        if (!normalizedEmail.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return new Packet("CreateEmployee", "Invalid email format");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                String checkQuery = "SELECT COUNT(*) FROM Uzytkownik WHERE LOWER(Email) = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, normalizedEmail);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        conn.rollback();
                        return new Packet("CreateEmployee", "Email already exists");
                    }
                }

                String hashedPassword = newEmployee.getPassword();

                String insertQuery = "INSERT INTO Uzytkownik (Imie, Nazwisko, Email, Telefon, Haslo, Rola_ID) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, new String[] {"Uzytkownik_ID"})) {
                    pstmt.setString(1, newEmployee.getFirstName().trim());
                    pstmt.setString(2, newEmployee.getLastName().trim());
                    pstmt.setString(3, normalizedEmail);
                    pstmt.setString(4, newEmployee.getPhone() != null ? newEmployee.getPhone().trim() : "");
                    pstmt.setString(5, hashedPassword);
                    pstmt.setInt(6, newEmployee.getRoleId());

                    int affectedRows = pstmt.executeUpdate();

                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                newEmployee.setId(generatedKeys.getInt(1));
                                conn.commit();
                                return new Packet("CreateEmployee", "Employee created successfully");
                            }
                        }
                        conn.rollback();
                        return new Packet("CreateEmployee", "Employee created but couldn't retrieve ID");
                    } else {
                        conn.rollback();
                        return new Packet("CreateEmployee", "Failed to create employee");
                    }
                }

            } catch (SQLException e) {
                conn.rollback();

                if (e.getMessage() != null && e.getMessage().contains("ORA-00001")) {
                    return new Packet("CreateEmployee", "Email already exists");
                }

                e.printStackTrace();
                return new Packet("CreateEmployee", "Database error: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("CreateEmployee", "Database connection error");
        }
    }

    public static Packet updateEmployee(Packet packet) {
        Employee employee = (Employee) packet.data;

        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty() ||
                employee.getLastName() == null || employee.getLastName().trim().isEmpty() ||
                employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            return new Packet("UpdateEmployee", "All required fields must be filled");
        }

        String query = "UPDATE Uzytkownik SET Imie = ?, Nazwisko = ?, Email = ?, Telefon = ? " +
                (employee.getPassword().isEmpty() ? "" : ", Haslo = ? ") +
                "WHERE Uzytkownik_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            pstmt.setString(paramIndex++, employee.getFirstName());
            pstmt.setString(paramIndex++, employee.getLastName());
            pstmt.setString(paramIndex++, employee.getEmail());
            pstmt.setString(paramIndex++, employee.getPhone() != null ? employee.getPhone() : "");

            if (!employee.getPassword().isEmpty()) {
                pstmt.setString(paramIndex++, employee.getPassword());
            }

            pstmt.setInt(paramIndex, employee.getId());

            int affectedRows = pstmt.executeUpdate();
            return new Packet("UpdateEmployee",
                    affectedRows > 0 ? "Employee updated successfully" : "No employee found with given ID");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("UpdateEmployee", "Database error during update");
        }
    }

    public static Packet deleteEmployee(Packet packet) {
        int employeeId = (int) packet.data;
        String query = "DELETE FROM Uzytkownik WHERE Uzytkownik_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employeeId);
            int affectedRows = pstmt.executeUpdate();
            return new Packet("DeleteEmployee", affectedRows > 0 ? "Employee deleted" : "Failed to delete employee");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("DeleteEmployee", "Failed to delete employee");
        }
    }
}