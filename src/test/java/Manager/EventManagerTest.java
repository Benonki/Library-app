package Manager;

import Classes.Manager.EventManager;
import Classes.Manager.Util.Event;
import Classes.Manager.Util.Participant;
import Server.DatabaseConnection;
import Server.Packet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventManagerTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @Test
    void testGetEvents_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("Wydarzenie_ID")).thenReturn(1);
            when(mockResultSet.getString("Temat")).thenReturn("Conference");
            when(mockResultSet.getDate("Data")).thenReturn(Date.valueOf(LocalDate.now()));
            when(mockResultSet.getString("Godzina")).thenReturn("14:30");
            when(mockResultSet.getString("Miejsce")).thenReturn("Room 101");

            Packet result = EventManager.getEvents();

            assertEquals("GetEvents", result.type);
            assertEquals("Events retrieved successfully", result.message);
            assertNotNull(result.events);
            assertEquals(1, result.events.size());

            Event event = result.events.get(0);
            assertEquals(1, event.getId());
            assertEquals("Conference", event.getTheme());
            assertNotNull(event.getDate());
            assertNotNull(event.getTime());
            assertEquals("Room 101", event.getPlace());
        }
    }

    @Test
    void testGetEventParticipants_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getString("Imie")).thenReturn("John");
            when(mockResultSet.getString("Nazwisko")).thenReturn("Doe");
            when(mockResultSet.getString("Email")).thenReturn("john@example.com");

            Packet result = EventManager.getEventParticipants(1);

            assertEquals("GetEventParticipants", result.type);
            assertNotNull(result.participants);
            assertEquals(1, result.participants.size());

            Participant participant = result.participants.get(0);
            assertEquals("John Doe", participant.getFullName());
            assertEquals("john@example.com", participant.getEmail());
        }
    }

    @Test
    void testCreateEvent_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString(), any(String[].class))).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            ResultSet mockGeneratedKeys = mock(ResultSet.class);
            when(mockGeneratedKeys.next()).thenReturn(true);
            when(mockGeneratedKeys.getInt(1)).thenReturn(1);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);

            Packet result = EventManager.createEvent("Conference", LocalDate.now(), LocalTime.now(), "Room 101");

            assertEquals("CreateEvent", result.type);
            assertEquals("Event created successfully", result.message);
            assertEquals(1, result.data);
        }
    }

    @Test
    void testAddParticipants_Success() throws SQLException {
        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            List<String> emails = new ArrayList<>();
            emails.add("john@example.com");

            Packet result = EventManager.addParticipants(1, emails);

            assertEquals("AddParticipants", result.type);
            assertTrue(result.message.contains("Dodano 1 uczestników"));
        }
    }
}