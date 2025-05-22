package Classes.Manager.Util;

import Classes.Manager.Event;
import Classes.Manager.Participant;
import Server.DatabaseConnection;
import Server.Packet;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        String query = "SELECT u.Imie, u.Nazwisko, u.Email FROM Uzytkownik u " +
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

            return Packet.withParticipants("GetEventParticipants", "Participants retrieved", participants);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Packet("GetEventParticipants", "Error retrieving participants: " + e.getMessage());
        }
    }
}