package Classes.Reader;

import Server.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WypozyczenieDAO {

    public static boolean wypozyczKsiazke(int uzytkownikId, int ksiazkaId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String check = "SELECT COUNT(*) FROM wypozyczenie WHERE ksiazka_id = ? AND rzeczywista_data_zwrotu IS NULL";
            try (PreparedStatement stmt = conn.prepareStatement(check)) {
                stmt.setInt(1, ksiazkaId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) return false;
            }

            String insert = "INSERT INTO wypozyczenie (data_wypozyczenia, uzytkownik_id, ksiazka_id) VALUES (SYSDATE, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setInt(1, uzytkownikId);
                stmt.setInt(2, ksiazkaId);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Book> getWypozyczoneKsiazki(int userId) {
        List<Book> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT k.ksiazka_id, k.tytul, a.imie || ' ' || a.nazwisko AS autor
                FROM wypozyczenie w
                JOIN ksiazka k ON w.ksiazka_id = k.ksiazka_id
                JOIN autor a ON k.autor_id = a.autor_id
                WHERE w.uzytkownik_id = ? AND w.rzeczywista_data_zwrotu IS NULL
            """;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    list.add(new Book(
                            rs.getInt("ksiazka_id"),
                            rs.getString("tytul"),
                            rs.getString("autor")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
