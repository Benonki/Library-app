package Classes.Employee;

import Classes.User.User;
import Server.Packet;
import Classes.Employee.Util.NewBookData;

public class EmployeeImpl extends User {

    public EmployeeImpl(String username) {
        super(username,"Employee");
    }

    @Override
    public Packet handlePacket(Packet packet) {
        switch (packet.type){
            case "GetLibraryResources":
                return Library.getLibraryResources();
            case "AddNewBook":
                return Library.addNewBook((NewBookData) packet.data);
            case "DeleteBookCopy":
                return Library.deleteBookCopy((int) packet.data);
            default:
                return new Packet(packet.type,"Unsupported in Employee");
        }
    }
}


