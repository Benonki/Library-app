package Coordinator;

import Classes.Coordinator.Delivery;
import Server.DatabaseConnection;
import Server.Packet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @Test
    void testConstructorAndGetters() {
        Delivery delivery = new Delivery("DHL");
        assertEquals("DHL", delivery.getName());
    }

    @Test
    void testGetDeliveryInfo_Success() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, true, false);
            when(mockResultSet.getString("nazwa")).thenReturn("DHL", "FedEx");

            Packet result = Delivery.getDeliveryInfo();

            assertEquals("GetDeliveryInformation", result.type);
            assertEquals("Success", result.message);
            assertNotNull(result.deliveryInfo);
            assertEquals(2, result.deliveryInfo.size());
            assertEquals("DHL", result.deliveryInfo.get(0).getName());
            assertEquals("FedEx", result.deliveryInfo.get(1).getName());

            verify(mockConnection).prepareStatement("SELECT nazwa FROM dostawca");
            verify(mockPreparedStatement).executeQuery();

        }
    }

    @Test
    void testGetDeliveryInfo_EmptyResult() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Packet result = Delivery.getDeliveryInfo();

            assertEquals("GetDeliveryInformation", result.type);
            assertEquals("Success", result.message);
            assertNotNull(result.deliveryInfo);
            assertTrue(result.deliveryInfo.isEmpty());


        }
    }

    @Test
    void testGetDeliveryInfo_DatabaseError() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString()))
                    .thenThrow(new SQLException("Database error"));

            Packet result = Delivery.getDeliveryInfo();

            assertEquals("GetDeliveryInformation", result.type);
            assertEquals("Error", result.message);
            assertNull(result.deliveryInfo);

        }
    }

    @Test
    void testGetDeliveryInfo_ConnectionError() {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection)
                    .thenThrow(new RuntimeException("Connection failed"));

            Packet result = Delivery.getDeliveryInfo();

            assertEquals("GetDeliveryInformation", result.type);
            assertEquals("Error", result.message);
            assertNull(result.deliveryInfo);

        }
    }
}