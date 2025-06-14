package Classes.Reader;

import Server.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        String sql = "SELECT k.ksiazka_id, k.tytul, a.imie || ' ' || a.nazwisko AS autor " +
                "FROM ksiazka k " +
                "JOIN autor a ON k.autor_id = a.autor_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ksiazka_id");
                String title = rs.getString("tytul");
                String author = rs.getString("autor");
                books.add(new Book(id, title, author));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    public static boolean czyKsiazkaDostepna(int ksiazkaId) {
        String sql = "SELECT COUNT(*) AS liczba FROM wypozyczenie " +
                "WHERE ksiazka_id = ? AND rzeczywista_data_zwrotu IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ksiazkaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int liczbaAktywnych = rs.getInt("liczba");
                return liczbaAktywnych == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
