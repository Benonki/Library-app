package Server;

import Classes.Coordinator.Delivery;
import Classes.Coordinator.Order;
import Classes.Coordinator.Util.InventoryItem;

import Classes.Employee.Util.LibraryItem;
import Classes.Employee.Util.Reader;
import Classes.Manager.Util.Employee;
import Classes.Manager.Util.Event;
import Classes.Manager.Util.Participant;

import java.io.Serializable;
import java.util.List;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    public String message;
    public String type;
    public String role;
    public List<InventoryItem> warehouseItems;
    public Order orderInfo;
    public List<Order> ordersInfo;
    public List<Delivery> deliveryInfo;
    public Object data;
    public List<Employee> employees;
    public List<Event> events;
    public List<Participant> participants;
    public List<LibraryItem> libraryItems;
    public List<Reader> readersList;

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

    public static Packet withOrdersInfo(String type, String message, List<Order> orders){
        Packet p = new Packet(type, message);
        p.ordersInfo = orders;
        return p;
    }

    public static Packet withEmployees(String type, String message, List<Employee> employees) {
        Packet p = new Packet(type, message);
        p.employees = employees;
        return p;
    }

    public static Packet withEvents(String type, String message, List<Event> events) {
        Packet p = new Packet(type, message);
        p.events = events;
        return p;
    }

    public static Packet withParticipants(String type, List<Participant> participants) {
        Packet p = new Packet(type, "Participants data");
        p.participants = participants;
        return p;
    }

    public static Packet withLibraryItems(String type, String message, List<LibraryItem> items) {
        Packet p = new Packet(type, message);
        p.libraryItems = items;
        return p;
    }

    public static Packet withReadersList(String type, String message, List<Reader> readersList) {
        Packet p = new Packet(type, message);
        p.readersList = readersList;
        return p;
    }
}
