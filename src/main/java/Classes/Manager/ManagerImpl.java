package Classes.Manager;

import Classes.User.User;
import Server.Packet;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            case "CreateEvent":
                return handleCreateEvent(packet);
            case "AddParticipants":
                return handleAddParticipants(packet);
            case "GetAllUsers":
                return handleGetAllUsers();
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

    private Packet handleCreateEvent(Packet packet) {
        try {
            String[] parts = packet.message.split("\\|");
            String theme = parts[0];
            LocalDate date = LocalDate.parse(parts[1]);
            LocalTime time = LocalTime.parse(parts[2]);
            String place = parts[3];

            return EventManager.createEvent(theme, date, time, place);
        } catch (Exception e) {
            return new Packet("CreateEvent", "Invalid event data format");
        }
    }

    private Packet handleAddParticipants(Packet packet) {
        try {
            String[] parts = packet.message.split("\\|");
            int eventId = Integer.parseInt(parts[0]);
            List<String> userEmails = new ArrayList<>();

            for (int i = 1; i < parts.length; i++) {
                userEmails.add(parts[i].trim());
            }

            return EventManager.addParticipants(eventId, userEmails);
        } catch (Exception e) {
            return new Packet("AddParticipants", "Invalid participant data format");
        }
    }

    private Packet handleGetAllUsers() {
        return EventManager.getAllUsers();
    }
}