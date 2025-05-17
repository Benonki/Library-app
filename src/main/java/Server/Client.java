package Server;

import Classes.Coordinator.Delivery;
import Classes.Coordinator.Order;
import Classes.Coordinator.Util.InventoryItem;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.BiConsumer;

public class Client {

    private static Client instance;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private BiConsumer<Boolean, String> callBack;
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
                System.out.println("ORDER WAS SUCCESSFULLY DELETED");
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
