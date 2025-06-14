package Coordinator;

import Classes.Coordinator.Delivery;
import Classes.Coordinator.Order;
import Classes.Coordinator.Util.BookOrder;
import Server.DatabaseConnection;
import Server.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private Delivery mockDelivery;

    private Order testOrder;
    private List<BookOrder> testBooks;
    private Date testDate;

    @BeforeEach
    void setUp(){
        testDate = new Date();
        testBooks = new ArrayList<>();
        testBooks.add(new BookOrder(5,"Java Programming",1));
        testBooks.add(new BookOrder(4,"Java Tests",2));

        testOrder = new Order(
                123,
                mockDelivery,
                9,
                testDate,
                testDate,
                testBooks,
                "Realizowane"
        );
    }

    @Test
    void testOrderConstructorWithAllParameters(){

        assertEquals(123,testOrder.getOrderMaker());
        assertEquals(mockDelivery,testOrder.getDeliveryService());
        assertEquals(9,testOrder.getAmount());
        assertEquals(testDate,testOrder.getRealizationDate());
        assertEquals(testDate,testOrder.getCreationDate());
        assertEquals("Realizowane", testOrder.getStatus());
        assertEquals(2,testOrder.getBooksToOrder().size());
    }

    @Test
    void testOrderConstructorWithLimitedParameters() {
        Order limitedOrder = new Order(mockDelivery, 5, testDate, testDate, "Nowe", 101);

        assertEquals(101, limitedOrder.getOrderID());
        assertEquals(mockDelivery, limitedOrder.getDeliveryService());
        assertEquals(5, limitedOrder.getAmount());
        assertEquals(testDate, limitedOrder.getRealizationDate());
        assertEquals(testDate, limitedOrder.getCreationDate());
        assertEquals("Nowe", limitedOrder.getStatus());
        assertNull(limitedOrder.getBooksToOrder());
    }

    @Test
    void testCreateOrderSuccess() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {

            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            PreparedStatement mockInsertOrderStmt = mock(PreparedStatement.class);
            ResultSet mockGeneratedKeys = mock(ResultSet.class);

            when(mockConnection.prepareStatement(startsWith("INSERT INTO Zamowienie"), any(String[].class)))
                    .thenReturn(mockInsertOrderStmt);
            when(mockInsertOrderStmt.executeUpdate()).thenReturn(1);
            when(mockInsertOrderStmt.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
            when(mockGeneratedKeys.next()).thenReturn(true);
            when(mockGeneratedKeys.getInt(1)).thenReturn(100);

            PreparedStatement mockInsertBookStmt = mock(PreparedStatement.class);

            when(mockConnection.prepareStatement(startsWith("INSERT INTO Zamowienie_Ksiazka")))
                    .thenReturn(mockInsertBookStmt);
            when(mockInsertBookStmt.executeUpdate()).thenReturn(1);

            Packet result = Order.createOrder(testOrder);

            assertNotNull(result);
            assertEquals("CreateNewOrder", result.type);
            assertEquals("Success", result.message);

            verify(mockInsertOrderStmt).setInt(1, 123);
            verify(mockInsertOrderStmt).setInt(2, 1);
            verify(mockInsertOrderStmt).setInt(3, 9);
            verify(mockInsertOrderStmt).setDate(eq(4), any(java.sql.Date.class));
            verify(mockInsertOrderStmt).setString(5, "Realizowane");

            verify(mockInsertBookStmt, times(2)).setInt(eq(1), eq(100));
            verify(mockInsertBookStmt, times(2)).executeUpdate();
        }
    }

    @Test
    void testCreateOrderFailure() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString(), any(String[].class)))
                    .thenThrow(new SQLException("Database error"));

            Packet result = Order.createOrder(testOrder);

            assertEquals("CreateNewOrder", result.type);
            assertEquals("Failed", result.message);
        }
    }

    @Test
    void testCreateOrderWithFailedBookInsert() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            PreparedStatement mockInsertOrderStmt = mock(PreparedStatement.class);
            ResultSet mockGeneratedKeys = mock(ResultSet.class);

            when(mockConnection.prepareStatement(startsWith("INSERT INTO Zamowienie"), any(String[].class)))
                    .thenReturn(mockInsertOrderStmt);
            when(mockInsertOrderStmt.executeUpdate()).thenReturn(1);
            when(mockInsertOrderStmt.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
            when(mockGeneratedKeys.next()).thenReturn(true);
            when(mockGeneratedKeys.getInt(1)).thenReturn(100);

            when(mockConnection.prepareStatement(startsWith("INSERT INTO Zamowienie_Ksiazka")))
                    .thenThrow(new SQLException("Book insert failed"));

            Packet result = Order.createOrder(testOrder);

            assertEquals("CreateNewOrder", result.type);
            assertEquals("Failed", result.message);
        }
    }

    @Test
    void testGetOrderInfoSuccess() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, true, false);
            when(mockResultSet.getInt("zamowienie_id")).thenReturn(1, 2);
            when(mockResultSet.getString("nazwa")).thenReturn("DHL", "FedEx");
            when(mockResultSet.getInt("ilosc")).thenReturn(5, 10);
            when(mockResultSet.getDate("data_zamowienia")).thenReturn(new java.sql.Date(testDate.getTime()));
            when(mockResultSet.getDate("termin_realizacji")).thenReturn(new java.sql.Date(testDate.getTime()));
            when(mockResultSet.getString("status")).thenReturn("Nowe", "Realizowane");

            Packet result = Order.getOrderInfo();

            assertEquals("GetOrderInformation", result.type);
            assertEquals("Success", result.message);
            assertNotNull(result.ordersInfo);
            assertEquals(2, result.ordersInfo.size());
        }
    }

    @Test
    void testGetOrderInfoFailure() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString()))
                    .thenThrow(new SQLException("Database error"));

            Packet result = Order.getOrderInfo();

            assertEquals("GetOrderInformation", result.type);
            assertEquals("Error", result.message);
        }
    }

    @Test
    void testDeleteOrderSuccess() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Packet result = Order.deleteOrder(123);

            assertEquals("DeleteOrder", result.type);
            assertEquals("Success", result.message);

            verify(mockConnection).setAutoCommit(false);
            verify(mockConnection).commit();
            verify(mockConnection).setAutoCommit(true);
        }
    }

    @Test
    void testDeleteOrderFailure() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString()))
                    .thenThrow(new SQLException("Database error"));

            Packet result = Order.deleteOrder(123);

            assertEquals("DeleteOrder", result.type);
            assertEquals("Error", result.message);

            verify(mockConnection).rollback();
        }
    }

    @Test
    void testUpdateOrderStatusSuccess() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            testOrder.setStatus("Zakończone");
            Packet result = Order.updateOrderStatus(testOrder);

            assertEquals("UpdateOrderStatus", result.type);
            assertEquals("SUCCESS", result.message);

            verify(mockPreparedStatement).setString(1, "Zakończone");
            verify(mockPreparedStatement).setInt(2, testOrder.getOrderID());
        }
    }

    @Test
    void testUpdateOrderStatusFailure() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString()))
                    .thenThrow(new SQLException("Database error"));

            Packet result = Order.updateOrderStatus(testOrder);

            assertEquals("UpdateOrderStatus", result.type);
            assertEquals("FAILED", result.message);
        }
    }

    @Test
    void testUpdateOrderStatusNoRowsAffected() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDBConnection = mockStatic(DatabaseConnection.class)) {
            mockedDBConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(0);

            Packet result = Order.updateOrderStatus(testOrder);

            assertEquals("UpdateOrderStatus", result.type);
            assertEquals("FAILED", result.message);
        }
    }

}