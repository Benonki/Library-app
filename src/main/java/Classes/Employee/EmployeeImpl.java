package Classes.Employee;

import Classes.User.User;
import Server.Packet;
import Classes.Employee.Util.NewBookData;
import Classes.Employee.Util.Reader;

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
            case "EditBook":
                return Library.editBook((NewBookData) packet.data);
            case "getReadersList":
                return ReaderService.getReadersList();
            case "AddNewReader":
                return ReaderService.addNewReader((Reader) packet.data);
            case "DeleteReader":
                return ReaderService.deleteReader((int) packet.data);
            case "EditReader":
                return ReaderService.editReader((Reader) packet.data);
            default:
                return new Packet(packet.type,"Unsupported in Employee");
        }
    }
}


