package Classes.Employee;

import Classes.Employee.Util.LibraryItem;
import Classes.Employee.Util.NewBookData;
import Server.DatabaseConnection;
import Server.Packet;

import java.sql.*;
import java.util.*;

public class Library {

    public static Packet getLibraryResources() {
        List<LibraryItem> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlQuery = """
            SELECT 
                bk.Egzemplarz_ID,
                k.Tytul,
                a.Imie || ' ' || a.Nazwisko AS Autor,
                bk.Status,
                bk.Lokalizacja,
                w.Nazwa AS Wydawnictwo,
                tokl.Nazwa AS Typ_Okladki
            FROM 
                Biblioteka_Ksiazka bk
            JOIN 
                Ksiazka k ON bk.Ksiazka_ID = k.Ksiazka_ID
            JOIN 
                Autor a ON k.Autor_ID = a.Autor_ID
            JOIN 
                Wydawnictwo w ON k.Wydawnictwo_ID = w.Wydawnictwo_ID
            JOIN 
                Typ_Okladki tokl ON k.Typ_Okladki_ID = tokl.Typ_Okladki_ID
            ORDER BY 
                bk.Egzemplarz_ID
        """;

            try (PreparedStatement statement = conn.prepareStatement(sqlQuery);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int egzId = rs.getInt("Egzemplarz_ID");
                    String tytul = rs.getString("Tytul");
                    String autor = rs.getString("Autor");
                    String status = rs.getString("Status");
                    String lokalizacja = rs.getString("Lokalizacja");
                    String wydawnictwo = rs.getString("Wydawnictwo");
                    String typOkladki = rs.getString("Typ_Okladki");

                    books.add(new LibraryItem(egzId, tytul, autor, status, lokalizacja, wydawnictwo, typOkladki));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("GetLibraryResources", "Error");
        }

        return Packet.withLibraryItems("GetLibraryResources", "Success", books);
    }

    public static Packet addNewBook(NewBookData data) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            System.out.println("Dodawanie nowej książki: " + data.getTytul());

            int autorId = getOrCreateId(conn,
                    "SELECT Autor_ID FROM Autor WHERE Imie = ? AND Nazwisko = ?",
                    "INSERT INTO Autor (Imie, Nazwisko) VALUES (?, ?)",
                    data.getImieAutora(), data.getNazwiskoAutora());

            int wydawnictwoId = getOrCreateId(conn,
                    "SELECT Wydawnictwo_ID FROM Wydawnictwo WHERE Nazwa = ?",
                    "INSERT INTO Wydawnictwo (Nazwa) VALUES (?)",
                    data.getWydawnictwo());

            int typOkladkiId = data.getTypOkladki();

            Integer ksiazkaId = null;
            String selectSql = " SELECT Ksiazka_ID FROM Ksiazka WHERE Tytul = ? AND Autor_ID = ? AND ISBN = ? AND Data_Wydania = ? AND Wydawnictwo_ID = ? AND Typ_Okladki_ID = ? ";

            try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
                stmt.setString(1, data.getTytul());
                stmt.setInt(2, autorId);
                stmt.setString(3, data.getIsbn());

                if (data.getDataWydania() != null) {
                    stmt.setDate(4, new java.sql.Date(data.getDataWydania().getTime()));
                } else {
                    stmt.setNull(4, java.sql.Types.DATE);
                }

                stmt.setInt(5, wydawnictwoId);
                stmt.setInt(6, typOkladkiId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    ksiazkaId = rs.getInt("Ksiazka_ID");
                }
            }

            if (ksiazkaId == null) {
                String insertSql = " INSERT INTO Ksiazka (Tytul, Autor_ID, ISBN, Data_Wydania, Wydawnictwo_ID, Typ_Okladki_ID) VALUES (?, ?, ?, ?, ?, ?) ";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, data.getTytul());
                    stmt.setInt(2, autorId);
                    stmt.setString(3, data.getIsbn());

                    if (data.getDataWydania() != null) {
                        stmt.setDate(4, new java.sql.Date(data.getDataWydania().getTime()));
                    } else {
                        stmt.setNull(4, java.sql.Types.DATE);
                    }

                    stmt.setInt(5, wydawnictwoId);
                    stmt.setInt(6, typOkladkiId);

                    stmt.executeUpdate();
                    ResultSet keys = stmt.getGeneratedKeys();
                    if (keys.next()) {
                        ksiazkaId = keys.getInt(1);
                    } else {
                        conn.rollback();
                        return new Packet("AddNewBook", "Nie udało się pobrać Ksiazka_ID");
                    }
                }
            }

            String insertExemplarSql = "INSERT INTO Biblioteka_Ksiazka (Ksiazka_ID, Status, Lokalizacja) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertExemplarSql)) {
                for (int i = 0; i < data.getIloscEgzemplarzy(); i++) {
                    stmt.setInt(1, ksiazkaId);
                    stmt.setString(2, "Dostepna");
                    stmt.setString(3, data.getLokalizacja());
                    stmt.executeUpdate();
                }
            }
            conn.commit();
            System.out.println("Ksiazka dodana pomyslnie");
            return new Packet("AddNewBook", "Success");

        } catch (Exception e) {
            System.out.println("Błąd podczas dodawania książki:");
            e.printStackTrace();
            return new Packet("AddNewBook", "Error");
        }
    }

    private static int getOrCreateId(Connection conn, String selectSql, String insertSql, String... params) throws SQLException {
        try (PreparedStatement select = conn.prepareStatement(selectSql)) {
            for (int i = 0; i < params.length; i++) {
                select.setString(i + 1, params[i]);
            }
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
            for (int i = 0; i < params.length; i++) {
                insert.setString(i + 1, params[i]);
            }
            insert.executeUpdate();
        }

        try (PreparedStatement select = conn.prepareStatement(selectSql)) {
            for (int i = 0; i < params.length; i++) {
                select.setString(i + 1, params[i]);
            }
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("Nie udało się uzyskać ID po wstawieniu");
    }

    public static Packet deleteBookCopy(int egzemplarzId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Biblioteka_Ksiazka WHERE Egzemplarz_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, egzemplarzId);
                int result = stmt.executeUpdate();
                if (result > 0) {
                    return new Packet("DeleteBookCopy", "Success");
                } else {
                    return new Packet("DeleteBookCopy", "No copy to delete");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("DeleteBookCopy", "Error");
        }
    }
}
