package Classes.Coordinator;

import Classes.User.User;
import Server.Packet;

public class CoordinatorImpl extends User {

    public CoordinatorImpl(String username){
        super(username,"Coordinator");
    }

    @Override
    public Packet handlePacket(Packet packet) {
        switch (packet.type){
            case "GetInventoryStatus":
                return getInventoryStatus();
            default:
                return new Packet(packet.type,"Unsupported in Coordinator");
        }
    }

    private Packet getInventoryStatus(){
        System.out.println("IN COORDINATOR INV WAS CALLED");
        return new Packet("GetInventoryStatus","Success");
    }
}


