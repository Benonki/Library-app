package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("Client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){
            closeServerSocket();
            e.printStackTrace();
            System.out.printf("Problem while starting server");
        }
    }


    public void closeServerSocket(){
        try{
            if(serverSocket !=null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Problem occured while closing the server");
        }
    }

    public static void main(String[] args) {
        try{
            int PORT = 1234;
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket);
            System.out.println("SERVER STARTED");
            server.startServer();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Problem while starting the server");
        }
    }
}
