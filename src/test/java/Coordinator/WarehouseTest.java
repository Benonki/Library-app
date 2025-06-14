package Coordinator;

import Classes.Coordinator.Util.InventoryItem;
import Classes.Coordinator.Warehouse;
import Server.DatabaseConnection;
import Server.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
    }

    @Test
    void testGetInventoryStatusSuccess() throws Exception {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, true, false);

            when(mockResultSet.getInt("Magazyn_ID")).thenReturn(1, 2);
            when(mockResultSet.getInt("ksiazka_id")).thenReturn(101, 102);
            when(mockResultSet.getString("Tytul")).thenReturn("Java Programming", "Clean Code");
            when(mockResultSet.getInt("Ilosc")).thenReturn(5, 3);
            when(mockResultSet.getInt("Rzad")).thenReturn(1, 2);
            when(mockResultSet.getInt("Sektor")).thenReturn(2, 3);
            when(mockResultSet.getInt("Polka")).thenReturn(3, 4);
            when(mockResultSet.getInt("MiejsceNaPolce")).thenReturn(4, 5);

            Packet result = Warehouse.getInventoryStatus();

            assertEquals("GetInventoryStatus", result.type);
            assertEquals("Success", result.message);
            assertNotNull(result.warehouseItems);
            assertEquals(2, result.warehouseItems.size());

            InventoryItem firstItem = result.warehouseItems.get(0);
            assertEquals(1, firstItem.getMagazynId());
            assertEquals(101, firstItem.getKsiazkaID());
            assertEquals("Java Programming", firstItem.getTytul());
            assertEquals(5, firstItem.getIlosc());
            assertEquals(1, firstItem.getRzad());
            assertEquals(2, firstItem.getSektor());
            assertEquals(3, firstItem.getPolka());
            assertEquals(4, firstItem.getMiejsceNaPolce());

            InventoryItem secondItem = result.warehouseItems.get(1);
            assertEquals(2, secondItem.getMagazynId());
            assertEquals(102, secondItem.getKsiazkaID());
            assertEquals("Clean Code", secondItem.getTytul());
            assertEquals(3, secondItem.getIlosc());
            assertEquals(2, secondItem.getRzad());
            assertEquals(3, secondItem.getSektor());
            assertEquals(4, secondItem.getPolka());
            assertEquals(5, secondItem.getMiejsceNaPolce());

            verify(mockConnection).prepareStatement(anyString());
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testGetInventoryStatusEmptyResult() throws Exception {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Packet result = Warehouse.getInventoryStatus();

            assertEquals("GetInventoryStatus", result.type);
            assertEquals("Success", result.message);
            assertNotNull(result.warehouseItems);
            assertTrue(result.warehouseItems.isEmpty());
        }
    }

    @Test
    void testGetInventoryStatusDatabaseError() throws Exception {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection)
                    .thenThrow(new RuntimeException("Connection error"));

            Packet result = Warehouse.getInventoryStatus();

            assertEquals("GetInventoryStatus", result.type);
            assertEquals("Error", result.message);
            assertNull(result.warehouseItems);
        }
    }

}

