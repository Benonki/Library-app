package Classes.Coordinator;

import Server.DatabaseConnection;
import Server.Packet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Warehouse {

    public Warehouse(){}

    public void createReport(){

    }

    public static Packet getInventoryStatus(){
        List<InventoryItem> inventory = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlQuery = "SELECT * FROM Magazyn";

            try(PreparedStatement statement = conn.prepareStatement(sqlQuery)){
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()){
                        int id = rs.getInt("Magazyn_ID");
                        int ksiazka_id = rs.getInt("Ksiazka_ID");
                        int ilosc = rs.getInt("Ilosc");
                        int rzad = rs.getInt("Rzad");
                        int sektor = rs.getInt("Sektor");
                        int polka = rs.getInt("Polka");
                        int miejsceNaPolce = rs.getInt("MiejsceNaPolce");
                        inventory.add(new InventoryItem(id, ksiazka_id, ilosc, rzad, sektor, polka, miejsceNaPolce));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Packet("GetInventoryStatus","Error");
        }
        System.out.println("IN COORDINATOR INV WAS CALLED");
        return new Packet("GetInventoryStatus","Success", inventory);
    }
}
