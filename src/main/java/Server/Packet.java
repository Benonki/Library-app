package Server;

import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    public String message;
    public String type;
    public String role;

    public Packet(String type,String message){
        this.type = type;
        this.message = message;
    }

    public Packet(String type, String message, String role){
        this.type = type;
        this.message = message;
        this.role = role;
    }
}
