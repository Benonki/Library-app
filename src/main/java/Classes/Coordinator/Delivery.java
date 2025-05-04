package Classes.Coordinator;

import Server.DatabaseConnection;
import Server.Packet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Delivery implements Serializable {

    private String name;

    public Delivery(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Packet getDeliveryInfo(){
        List<Delivery> deliveryInfo = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlQuery = "SELECT nazwa FROM dostawca";

            try(PreparedStatement statement = conn.prepareStatement(sqlQuery)){
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()){
                        deliveryInfo.add(new Delivery(rs.getString("nazwa")));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Packet("GetDeliveryInformation","Error");
        }
        Packet packet = Packet.withDeliveries("GetDeliveryInformation","Success", deliveryInfo);
        return packet;
    }
}
