package Server;

import Classes.Coordinator.InventoryItem;

import java.io.Serializable;
import java.util.List;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    public String message;
    public String type;
    public String role;
    public List<InventoryItem> warehouseItems;

    public Packet(String type,String message){
        this.type = type;
        this.message = message;
    }

    public Packet(String type, String message, String role){
        this.type = type;
        this.message = message;
        this.role = role;
    }

    public Packet(String type,String message, List<InventoryItem> warehouseItems){
        this.type = type;
        this.message = message;
        this.warehouseItems = warehouseItems;
    }
}
