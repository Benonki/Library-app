package Server;

import Classes.User.User;
import Classes.User.UserFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String token;
    private User user;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.token = UUID.randomUUID().toString();
            clientHandlers.add(this);
        }catch (IOException e){
            e.printStackTrace();
            closeEverything(socket,outputStream,inputStream);
        }

    }

    @Override
    public void run() {
        Packet rcvPacket;
        while (socket.isConnected()) {
            try {
                System.out.println("WAITING FOR MESSAGE");
                rcvPacket = (Packet) inputStream.readObject();
                checkPacketType(rcvPacket);
            } catch (SocketException e) {
                System.out.println("Client disconnected");
                closeEverything(socket, outputStream, inputStream);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                closeEverything(socket, outputStream, inputStream);
                break;
            }
        }
    }

    private void checkPacketType(Packet packet){ // Handle request sent by client
        if(packet.type.equals("Login")){
            loginHandler(packet);
        }else if(user != null){
            Packet response = user.handlePacket(packet);
            sendPacket(response);
        }else{
            sendPacket(new Packet(packet.type, "Unauthorized"));
        }
    }

    private void loginHandler(Packet rcvPacket){
        String[] credentials = rcvPacket.message.split(";");
        if (credentials.length == 2) {
            String username = credentials[0];
            String password = credentials[1];

            String role = User.login(username, password);
            if(!role.isEmpty()){
                this.user = UserFactory.createUser(username, role);
                System.out.println("Login Successful for user: " + username + " with role: " + role);
                sendPacket(new Packet("Login", "Login Success", role));
            }else{
                System.out.println("Invalid credentials for user: " + username);
                sendPacket(new Packet("Login", "Login Failed"));
            }
        }
    }

    public void sendPacket(Packet packet){
            try{
                    outputStream.writeObject(packet);
                    outputStream.flush();
            }catch (IOException e){
                e.printStackTrace();
                closeEverything(socket,outputStream,inputStream);
            }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
    }

    public void closeEverything(Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream){
        removeClientHandler();
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
