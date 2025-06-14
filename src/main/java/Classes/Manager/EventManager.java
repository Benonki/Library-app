package Classes.Manager;

import Classes.Manager.Util.Event;
import Classes.Manager.Util.Participant;
import Server.DatabaseConnection;
import Server.Packet;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventManager {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Packet getEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT Wydarzenie_ID, Temat, Data, Godzina, Miejsce FROM Wydarzenie";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                try {
                    int id = rs.getInt("Wydarzenie_ID");
                    String theme = rs.getString("Temat");
                    String place = rs.getString("Miejsce");

                    LocalDate date = rs.getDate("Data").toLocalDate();

                    LocalTime time;
                    String timeString = rs.getString("Godzina");

                    try {
                        time = LocalTime.parse(timeString, TIME_FORMATTER);
                    } catch (DateTimeParseException e1) {
                        try {
                            String dateTimeString = date.toString() + " " + timeString;
                            time = LocalTime.parse(dateTimeString, DATE_TIME_FORMATTER);
                        } catch (DateTimeParseException e2) {
                            System.err.println("Could not parse time: " + timeString + ", using 00:00");
                            time = LocalTime.MIDNIGHT;
                        }
                    }

                    events.add(new Event(id, theme, date, time, place));
                } catch (Exception e) {
                    System.err.println("Error parsing event data: " + e.getMessage());
                    continue;
                }
            }
            return Packet.withEvents("GetEvents", "Events retrieved successfully", events);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("GetEvents", "Error retrieving events: " + e.getMessage());
        }
    }

    public static Packet getEventParticipants(int eventId) {
        List<Participant> participants = new ArrayList<>();
        String query = "SELECT u.Imie, u.Nazwisko, u.Email " +
                "FROM Uzytkownik u " +
                "JOIN Uczestnik_Wydarzenia uw ON u.Uzytkownik_ID = uw.Uzytkownik_ID " +
                "WHERE uw.Wydarzenie_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("Imie") + " " + rs.getString("Nazwisko");
                String email = rs.getString("Email");
                participants.add(new Participant(fullName, email));
            }

            return Packet.withParticipants("GetEventParticipants", participants);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("GetEventParticipants", "Error retrieving participants");
        }
    }

    public static Packet createEvent(String theme, LocalDate date, LocalTime time, String place) {
        String query = "INSERT INTO Wydarzenie (Temat, Data, Godzina, Miejsce) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, new String[]{"Wydarzenie_ID"})) {

            stmt.setString(1, theme);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setString(3, time.format(TIME_FORMATTER));
            stmt.setString(4, place);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return new Packet("CreateEvent", "Failed to create event");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int eventId = generatedKeys.getInt(1);
                    Packet response = new Packet("CreateEvent", "Event created successfully");
                    response.data = eventId;
                    return response;
                }
            }

            return new Packet("CreateEvent", "Failed to get event ID");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("CreateEvent", "Error creating event: " + e.getMessage());
        }
    }

    public static Packet addParticipants(int eventId, List<String> userEmails) {
        String checkQuery = "SELECT COUNT(*) FROM Uczestnik_Wydarzenia uw " +
                "JOIN Uzytkownik u ON uw.Uzytkownik_ID = u.Uzytkownik_ID " +
                "WHERE u.Email = ? AND uw.Wydarzenie_ID = ?";

        String insertQuery = "INSERT INTO Uczestnik_Wydarzenia (Uzytkownik_ID, Wydarzenie_ID) " +
                "SELECT Uzytkownik_ID, ? FROM Uzytkownik WHERE Email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            conn.setAutoCommit(false);
            int addedCount = 0;
            List<String> existingParticipants = new ArrayList<>();

            for (String email : userEmails) {
                checkStmt.setString(1, email);
                checkStmt.setInt(2, eventId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    existingParticipants.add(email);
                    continue;
                }

                insertStmt.setInt(1, eventId);
                insertStmt.setString(2, email);
                int affected = insertStmt.executeUpdate();
                if (affected > 0) {
                    addedCount++;
                }
            }

            conn.commit();

            String message = String.format(
                    "Dodano %d uczestników.",
                    addedCount
            );

            if (!existingParticipants.isEmpty()) {
                message += "\nIstniejący uczestnicy: " + String.join(", ", existingParticipants);
            }

            return new Packet("AddParticipants", message);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("AddParticipants", "Error adding participants: " + e.getMessage());
        }
    }

    public static Packet getAllUsers() {
        List<Participant> users = new ArrayList<>();
        String query = "SELECT Imie, Nazwisko, Email FROM Uzytkownik";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String fullName = rs.getString("Imie") + " " + rs.getString("Nazwisko");
                String email = rs.getString("Email");
                users.add(new Participant(fullName, email));
            }

            return Packet.withParticipants("GetAllUsers", users);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("GetAllUsers", "Error retrieving users: " + e.getMessage());
        }
    }
}