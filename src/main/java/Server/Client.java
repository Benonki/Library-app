package Server;

import Classes.Coordinator.Delivery;
import Classes.Coordinator.Order;
import Classes.Coordinator.Util.InventoryItem;
import Classes.Manager.Event;
import Classes.Manager.Participant;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.BiConsumer;

import Classes.Manager.Employee;
import java.util.List;
import java.util.function.Consumer;

public class Client {

    private static Client instance;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private BiConsumer<Boolean, String> callBack;
    private Consumer<List<Employee>> employeesCallback;
    private Consumer<List<Event>> eventsCallback;
    private Consumer<List<Participant>> participantsCallback;
    private java.util.function.Consumer<java.util.List<InventoryItem>> inventoryCallback;
    private java.util.function.Consumer<java.util.List<Delivery>> deliveryCallback;
    private java.util.function.Consumer<java.util.List<Order>> ordersCallback;


    private Client(){
        try{
            this.socket = new Socket("localhost",1234);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.outputStream.flush();
            this.inputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to server");
            listenForPacket();

        }catch (IOException e){
            e.printStackTrace();
            closeEverything(socket,outputStream,inputStream);
        }
    }

    public static Client getInstance(){
        if(instance == null){
            instance = new Client();
        }
        return instance;
    }

    public void setCallBack(BiConsumer<Boolean, String> callback) {  // Changed parameter type
        this.callBack = callback;
    }

    public void sendPacket(Packet packet){
        try{
            outputStream.writeObject(packet);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void listenForPacket() {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    Packet receivedPacket = (Packet)inputStream.readObject();
                    handlePacket(receivedPacket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handlePacket(Packet receivedPacket) { //Handle answer from server
        switch (receivedPacket.type) {
            case "Login":
                boolean success = receivedPacket.message.equals("Login Success");
                if (callBack != null) {
                    Platform.runLater(() -> callBack.accept(success, receivedPacket.role));
                }
                break;
            case"GetInventoryStatus":
                System.out.println("INVENTORY RETURNED");
                if (inventoryCallback != null) {
                    inventoryCallback.accept(receivedPacket.warehouseItems);
                }
                break;
            case"GetDeliveryInformation":
                System.out.println("DELIVERY INFORMATION RETURNED");
                if(deliveryCallback != null){
                    deliveryCallback.accept(receivedPacket.deliveryInfo);
                }
                break;
            case "CreateNewOrder":
                System.out.println("Order created");
                break;
            case "GetOrderInformation":
                System.out.println("ORDERS INFORMATION RETURNED");
                if(ordersCallback != null){
                    ordersCallback.accept(receivedPacket.ordersInfo);
                }
                break;
            case "DeleteOrder":
                System.out.println("ORDER WAS SUCCESSFULLY DELETED " + receivedPacket.message);
                break;
            case "UpdateOrderStatus":
                System.out.println("ORDER STATUS CHANGED " + receivedPacket.message);
                break;
            case "GetEmployees":
                if (employeesCallback != null && receivedPacket.employees != null) {
                    Platform.runLater(() -> employeesCallback.accept(receivedPacket.employees));
                }
                break;
            case "CreateEmployee":
                System.out.println("Employee creation result: " + receivedPacket.message);
                if (callBack != null) {
                    Platform.runLater(() -> callBack.accept(
                            receivedPacket.message.startsWith("Employee created"),
                            receivedPacket.message
                    ));
                }
                break;
            case "GetEvents":
                if (eventsCallback != null && receivedPacket.events != null) {
                    Platform.runLater(() -> eventsCallback.accept(receivedPacket.events));
                }
                break;
            case "GetEventParticipants":
                if (participantsCallback != null && receivedPacket.data instanceof List) {
                    Platform.runLater(() -> participantsCallback.accept((List<Participant>) receivedPacket.data));
                }
                break;
            default:
                System.out.println("This type is not supported ");
                System.out.println(receivedPacket.type);
        }
    }

    public void setInventoryCallback(java.util.function.Consumer<java.util.List<InventoryItem>> callback) {
        this.inventoryCallback = callback;
    }

    public void setDeliveryCallback(java.util.function.Consumer<java.util.List<Delivery>> callback) {
        this.deliveryCallback = callback;
    }

    public void setOrdersCallback(java.util.function.Consumer<java.util.List<Order>> callback){
        this.ordersCallback = callback;
    }

    public void setEmployeesCallback(Consumer<List<Employee>> callback) {
        this.employeesCallback = callback;
    }

    public void setEventsCallback(Consumer<List<Event>> callback) {
        this.eventsCallback = callback;
    }

    public void setParticipantsCallback(Consumer<List<Participant>> callback) {
        this.participantsCallback = callback;
    }


    public void closeEverything(Socket socket,ObjectOutputStream outputStream,ObjectInputStream inputStream){
        try{
            if(inputStream != null){
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
