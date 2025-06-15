package Classes.Employee;

import Classes.Employee.Util.Reader;
import Server.DatabaseConnection;
import Server.Packet;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ReaderService {

    public static Packet getReadersList() {
        List<Reader> readersList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlQuery = " SELECT u.Uzytkownik_ID, u.Imie, u.Nazwisko, u.Email, u.Telefon, u.Haslo, k.Numer_Karty, k.Data_Wydania, k.Data_Waznosci, k.Status FROM Uzytkownik u LEFT JOIN Karta_Biblioteczna k ON u.Uzytkownik_ID = k.Uzytkownik_ID WHERE u.Rola_ID = 2 ";

            try (PreparedStatement statement = conn.prepareStatement(sqlQuery);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int uzytkownikID = rs.getInt("Uzytkownik_ID");
                    String imie = rs.getString("Imie");
                    String nazwisko = rs.getString("Nazwisko");
                    String email = rs.getString("Email");
                    String telefon = rs.getString("Telefon");
                    String haslo = rs.getString("Haslo");
                    String numerKarty = rs.getString("Numer_Karty");
                    Date dataWydania = rs.getDate("Data_Wydania");
                    Date dataWaznosci = rs.getDate("Data_Waznosci");
                    String statusKarty = rs.getString("Status");

                    readersList.add(new Reader(uzytkownikID, imie, nazwisko, email, telefon, haslo,
                            numerKarty, dataWydania, dataWaznosci, statusKarty));
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

            String insertSql = "INSERT INTO Uzytkownik (Imie, Nazwisko, Email, Telefon, Haslo, Rola_ID) VALUES (?, ?, ?, ?, ?, ?)";
            Integer userId = null;

            try (PreparedStatement stmt = conn.prepareStatement(insertSql, new String[]{"Uzytkownik_ID"})) {
                stmt.setString(1, reader.getFirstName());
                stmt.setString(2, reader.getLastName());
                stmt.setString(3, reader.getEmail());
                stmt.setString(4, reader.getPhone());
                stmt.setString(5, reader.getPassword());
                stmt.setInt(6, 2);

                stmt.executeUpdate();
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    userId = keys.getInt(1);
                } else {
                    conn.rollback();
                    return new Packet("AddNewReader", "Brak wygenerowanego ID użytkownika");
                }
            }

            String cardInsert = "INSERT INTO Karta_Biblioteczna (Numer_Karty, Data_Wydania, Data_Waznosci, Status, Uzytkownik_ID) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement cardStmt = conn.prepareStatement(cardInsert)) {
                cardStmt.setString(1, reader.getCardNumber());
                cardStmt.setDate(2, new java.sql.Date(reader.getIssueDate().getTime()));
                cardStmt.setDate(3, new java.sql.Date(reader.getExpiryDate().getTime()));
                cardStmt.setString(4, reader.getCardStatus());
                cardStmt.setInt(5, userId);
                cardStmt.executeUpdate();
            }

            conn.commit();
            return new Packet("AddNewReader", "Success");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("AddNewReader", "Error");
        }
    }

    public static Packet editReader(Reader reader) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String updateUser = "UPDATE Uzytkownik SET Imie = ?, Nazwisko = ?, Email = ?, Telefon = ?, Haslo = ? WHERE Uzytkownik_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateUser)) {
                stmt.setString(1, reader.getFirstName());
                stmt.setString(2, reader.getLastName());
                stmt.setString(3, reader.getEmail());
                stmt.setString(4, reader.getPhone());
                stmt.setString(5, reader.getPassword());
                stmt.setInt(6, reader.getId());
                stmt.executeUpdate();
            }

            String updateCard = "UPDATE Karta_Biblioteczna SET Numer_Karty = ?, Data_Wydania = ?, Data_Waznosci = ?, Status = ? WHERE Uzytkownik_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateCard)) {
                stmt.setString(1, reader.getCardNumber());
                stmt.setDate(2, new java.sql.Date(reader.getIssueDate().getTime()));
                stmt.setDate(3, new java.sql.Date(reader.getExpiryDate().getTime()));
                stmt.setString(4, reader.getCardStatus());
                stmt.setInt(5, reader.getId());
                stmt.executeUpdate();
            }

            conn.commit();
            return new Packet("EditReader", "Success");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("EditReader", "Error");
        }
    }

    public static Packet deleteReader(int readerId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement cardStmt = conn.prepareStatement("DELETE FROM Karta_Biblioteczna WHERE Uzytkownik_ID = ?")) {
                cardStmt.setInt(1, readerId);
                cardStmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Uzytkownik WHERE Uzytkownik_ID = ? AND Rola_ID = 2")) {
                stmt.setInt(1, readerId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    return new Packet("DeleteReader", "Success");
                } else {
                    conn.rollback();
                    return new Packet("DeleteReader", "Nie znaleziono czytelnika o podanym ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("DeleteReader", "Error");
        }
    }
}