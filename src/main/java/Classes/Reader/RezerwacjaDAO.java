package Classes.Reader;

import Server.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RezerwacjaDAO {

    public static boolean zarezerwujKsiazke(int uzytkownikId, int ksiazkaId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Sprawdź czy książka jest wypożyczona (czyli brak dostępnych egzemplarzy)
            String check = "SELECT COUNT(*) FROM wypozyczenie WHERE ksiazka_id = ? AND rzeczywista_data_zwrotu IS NULL";
            try (PreparedStatement stmt = conn.prepareStatement(check)) {
                stmt.setInt(1, ksiazkaId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    return false; // Książka dostępna, więc nie rezerwujemy
                }
            }

            // Sprawdź, czy użytkownik już zarezerwował tę książkę
            String already = "SELECT COUNT(*) FROM rezerwacja WHERE ksiazka_id = ? AND uzytkownik_id = ? AND status = 'Aktywna'";
            try (PreparedStatement stmt = conn.prepareStatement(already)) {
                stmt.setInt(1, ksiazkaId);
                stmt.setInt(2, uzytkownikId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            // Wstaw rezerwację
            String insert = "INSERT INTO rezerwacja (data_rezerwacji, data_wygasniecia, status, uzytkownik_id, ksiazka_id) VALUES (?, ?, 'Aktywna', ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setDate(1, Date.valueOf(LocalDate.now()));
                stmt.setDate(2, Date.valueOf(LocalDate.now().plusDays(3)));
                stmt.setInt(3, uzytkownikId);
                stmt.setInt(4, ksiazkaId);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean anulujRezerwacje(int uzytkownikId, int ksiazkaId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String delete = "DELETE FROM rezerwacja WHERE uzytkownik_id = ? AND ksiazka_id = ? AND status = 'Aktywna'";
            try (PreparedStatement stmt = conn.prepareStatement(delete)) {
                stmt.setInt(1, uzytkownikId);
                stmt.setInt(2, ksiazkaId);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Book> getZarezerwowaneKsiazki(int uzytkownikId) {
        List<Book> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT k.ksiazka_id, k.tytul, a.imie || ' ' || a.nazwisko AS autor,
                       r.data_rezerwacji, r.data_wygasniecia
                FROM rezerwacja r
                JOIN ksiazka k ON r.ksiazka_id = k.ksiazka_id
                JOIN autor a ON k.autor_id = a.autor_id
                WHERE r.uzytkownik_id = ? AND r.status = 'Aktywna'
            """;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, uzytkownikId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    lista.add(new Book(
                            rs.getInt("ksiazka_id"),
                            rs.getString("tytul"),
                            rs.getString("autor"),
                            rs.getDate("data_rezerwacji").toLocalDate(),
                            rs.getDate("data_wygasniecia").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
