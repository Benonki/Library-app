package Server;

import Classes.Coordinator.Delivery;
import Classes.Coordinator.Order;
import Classes.Coordinator.Util.InventoryItem;

import java.io.Serializable;
import java.util.List;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    public String message;
    public String type;
    public String role;
    public List<InventoryItem> warehouseItems;
    public Order orderInfo;
    public List<Delivery> deliveryInfo;

    public Packet(String type,String message){
        this.type = type;
        this.message = message;
    }

    public Packet(String type, String message, String role){
        this.type = type;
        this.message = message;
        this.role = role;
    }

    public static Packet withOrderInfo(String type, String message, Order order) {
        Packet p = new Packet(type, message);
        p.orderInfo = order;
        return p;
    }

    public static Packet withWarehouseItems(String type, String message, List<InventoryItem> items) {
        Packet p = new Packet(type, message);
        p.warehouseItems = items;
        return p;
    }

    public static Packet withDeliveries(String type, String message, List<Delivery> deliveries) {
        Packet p = new Packet(type, message);
        p.deliveryInfo = deliveries;
        return p;
    }




}
