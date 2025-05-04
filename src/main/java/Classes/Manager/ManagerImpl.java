package Classes.Manager;

import Classes.User.User;
import Server.Packet;

public class ManagerImpl extends User {

    public ManagerImpl(String username) {
        super(username,"Manager");
    }

    @Override
    public Packet handlePacket(Packet packet) {
        switch (packet.type){
            default:
                return new Packet(packet.type,"Unsupported in Manager");
        }
    }


}


