package Server;

import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public class Client {

    private static Client instance;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Packet rcvPacket;
    private BiConsumer<Boolean, String> callBack;

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

    public Packet getReceivedPacket() {
        return rcvPacket;
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

    private void handlePacket(Packet receivedPacket) {
        switch (receivedPacket.type) {
            case "Login":
                boolean success = receivedPacket.message.equals("Login Success");
                if (callBack != null) {
                    Platform.runLater(() -> callBack.accept(success, receivedPacket.role));
                }
                break;
            default:
                System.out.println("This type is not supported ");
        }
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
