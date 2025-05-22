package Classes.Manager;

import Classes.Manager.Util.EmployeeManager;
import Classes.Manager.Util.EventManager;
import Classes.User.User;
import Server.Packet;

public class ManagerImpl extends User {
    public ManagerImpl(String username) {
        super(username, "Manager");
    }

    @Override
    public Packet handlePacket(Packet packet) {
        switch (packet.type) {
            case "GetEmployees":
                return handleGetEmployees();
            case "CreateEmployee":
                return handleCreateEmployee(packet);
            case "UpdateEmployee":
                return handleUpdateEmployee(packet);
            case "DeleteEmployee":
                return handleDeleteEmployee(packet);
            case "GetEvents":
                return handleGetEvents();
            case "GetEventParticipants":
                return handleGetEventParticipants(packet);
            default:
                return new Packet(packet.type, "Unsupported in Manager");
        }
    }

    private Packet handleGetEmployees() {
        return EmployeeManager.getEmployees();
    }

    private Packet handleCreateEmployee(Packet packet) {
        return EmployeeManager.createEmployee(packet);
    }

    private Packet handleUpdateEmployee(Packet packet) {
        return EmployeeManager.updateEmployee(packet);
    }

    private Packet handleDeleteEmployee(Packet packet) {
        return EmployeeManager.deleteEmployee(packet);
    }

    private Packet handleGetEvents() {
        return EventManager.getEvents();
    }

    private Packet handleGetEventParticipants(Packet packet) {
        try {
            int eventId = Integer.parseInt(packet.message);
            return EventManager.getEventParticipants(eventId);
        } catch (NumberFormatException e) {
            return new Packet("GetEventParticipants", "Invalid event ID format");
        }
    }
}