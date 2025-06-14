package Classes.Employee;

import Classes.Employee.Util.Reader;
import Server.DatabaseConnection;
import Server.Packet;

import java.sql.*;
import java.util.*;

public class ReaderService {

    public static Packet getReadersList() {
        List<Reader> readersList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlQuery = "SELECT u.Uzytkownik_ID, u.Imie, u.Nazwisko, u.Email, u.Telefon, u.Haslo FROM Uzytkownik u WHERE u.Rola_ID = 2";

            try (PreparedStatement statement = conn.prepareStatement(sqlQuery);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int uzytkownikID = rs.getInt("Uzytkownik_ID");
                    String imie = rs.getString("Imie");
                    String nazwisko = rs.getString("Nazwisko");
                    String email = rs.getString("Email");
                    String telefon = rs.getString("Telefon");
                    String  haslo = rs.getString("Haslo");

                    readersList.add(new Reader(uzytkownikID, imie, nazwisko, email, telefon, haslo));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("getReadersList", "Error");
        }

        return Packet.withReadersList("getReadersList", "Success", readersList);
    }

    public static Packet addNewReader(Reader reader) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            System.out.println("Dodawanie nowego czytelnika: " + reader.getEmail());

            String insertSql = " INSERT INTO Uzytkownik (Imie, Nazwisko, Email, Telefon, Haslo, Rola_ID) VALUES (?, ?, ?, ?, ?, ?) ";

            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, reader.getFirstName());
                stmt.setString(2, reader.getLastName());
                stmt.setString(3, reader.getEmail());
                stmt.setString(4, reader.getPhone());
                stmt.setString(5, reader.getPassword());
                stmt.setInt(6, 2);

                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Czytelnik dodany pomyślnie");
            return new Packet("AddNewReader", "Success");

        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("AddNewReader", "Error");
        }
    }

    public static Packet deleteReader(int readerId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "DELETE FROM Uzytkownik WHERE Uzytkownik_ID = ? AND Rola_ID = 2";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, readerId);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit();
                    return new Packet("DeleteReader", "Success");
                } else {
                    return new Packet("DeleteReader", "Nie znaleziono czytelnika o podanym ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("DeleteReader", "Error");
        }
    }
}