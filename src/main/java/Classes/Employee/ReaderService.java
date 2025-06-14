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
            String sqlQuery = "SELECT u.Uzytkownik_ID, u.Imie, u.Nazwisko, u.Email, u.Telefon FROM Uzytkownik u WHERE u.Rola_ID = 2";

            try (PreparedStatement statement = conn.prepareStatement(sqlQuery);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int uzytkownikID = rs.getInt("Uzytkownik_ID");
                    String imie = rs.getString("Imie");
                    String nazwisko = rs.getString("Nazwisko");
                    String email = rs.getString("Email");
                    String telefon = rs.getString("Telefon");

                    readersList.add(new Reader(uzytkownikID, imie, nazwisko, email, telefon));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("getReadersList", "Error");
        }

        return Packet.withReadersList("getReadersList", "Success", readersList);
    }
}