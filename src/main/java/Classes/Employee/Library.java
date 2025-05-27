package Classes.Employee;

import Classes.Employee.Util.LibraryItem;
import Server.DatabaseConnection;
import Server.Packet;

import java.sql.*;
import java.util.*;

public class Library {

    public static Packet getLibraryResources() {
        List<LibraryItem> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()){
            String sqlQuery = "SELECT k.Ksiazka_ID, k.Tytul, a.Imie || ' ' || a.Nazwisko AS Autor, COUNT(bk.Egzemplarz_ID) AS Ilosc " +
                    "FROM Ksiazka k " +
                    "JOIN Autor a ON k.Autor_ID = a.Autor_ID " +
                    "JOIN Biblioteka_Ksiazka bk ON k.Ksiazka_ID = bk.Ksiazka_ID " +
                    "GROUP BY k.Ksiazka_ID, k.Tytul, a.Imie, a.Nazwisko";

            try(PreparedStatement statement = conn.prepareStatement(sqlQuery)){
            try(ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    int ksiazkaID = rs.getInt("Ksiazka_ID");
                    String tytul = rs.getString("Tytul");
                    String autor = rs.getString("Autor");
                    int ilosc = rs.getInt("Ilosc");
                    books.add(new LibraryItem(ksiazkaID, tytul, autor, ilosc));
                }
            }
        }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("GetLibraryResources","Error");
        }

        Packet packet = Packet.withLibraryItems("GetLibraryResources", "Success", books);
        return packet;
    }
}
