package Classes.Coordinator;

import Classes.Coordinator.Util.InventoryItem;
import Server.DatabaseConnection;
import Server.Packet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Warehouse {

    public Warehouse(){}

    public static Packet getInventoryStatus(){
        List<InventoryItem> inventory = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlQuery = "SELECT m.Magazyn_ID, m.Ilosc, m.Rzad, m.Sektor, m.Polka, m.MiejsceNaPolce, k.Tytul, k.ksiazka_id FROM Magazyn m JOIN Ksiazka k ON m.Ksiazka_ID = k.Ksiazka_ID";

            try(PreparedStatement statement = conn.prepareStatement(sqlQuery)){
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()){
                        int id = rs.getInt("Magazyn_ID");
                        String tytul = rs.getString("Tytul");
                        int ksiazkaID = rs.getInt("ksiazka_id");
                        int ilosc = rs.getInt("Ilosc");
                        int rzad = rs.getInt("Rzad");
                        int sektor = rs.getInt("Sektor");
                        int polka = rs.getInt("Polka");
                        int miejsceNaPolce = rs.getInt("MiejsceNaPolce");
                        inventory.add(new InventoryItem(id, ksiazkaID ,tytul, ilosc, rzad, sektor, polka, miejsceNaPolce));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Packet("GetInventoryStatus","Error");
        }
        System.out.println("IN COORDINATOR INV WAS CALLED");
        Packet packet = Packet.withWarehouseItems("GetInventoryStatus","Success", inventory);
        return packet;
    }
}
