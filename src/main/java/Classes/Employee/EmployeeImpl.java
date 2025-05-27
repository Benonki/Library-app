package Classes.Employee;

import Classes.User.User;
import Server.Packet;

public class EmployeeImpl extends User {

    public EmployeeImpl(String username) {
        super(username,"Employee");
    }

    @Override
    public Packet handlePacket(Packet packet) {
        switch (packet.type){
            case "GetLibraryResources":
                return Library.getLibraryResources();
            default:
                return new Packet(packet.type,"Unsupported in Employee");
        }
    }
}


