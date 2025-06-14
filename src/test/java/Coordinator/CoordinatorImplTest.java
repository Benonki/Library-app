package Coordinator;

import Classes.Coordinator.CoordinatorImpl;
import Classes.Coordinator.Delivery;
import Classes.Coordinator.Order;
import Classes.Coordinator.Util.InventoryItem;
import Classes.Coordinator.Warehouse;
import Classes.User.User;
import Server.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.internal.matchers.Or;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoordinatorImplTest {

    private CoordinatorImpl coordinator;

    @Mock
    private Packet mockPacket;

    @Mock
    private Order mockOrder;

    @Mock
    private Delivery mockDelivery;

    @Mock
    private Warehouse mockWarehouse;

    @BeforeEach
    void setUp() {
        coordinator = new CoordinatorImpl("test_coordinator");
    }

    @Test
    void testHandlePacket_GetInventoryStatus() {
        try (MockedStatic<Warehouse> mockedWarehouse = mockStatic(Warehouse.class)) {
            Packet expectedPacket = new Packet("GetInventoryStatus", "Success");
            expectedPacket.warehouseItems = List.of(new InventoryItem(1, 101, "Book", 5, 1, 1, 1, 1));

            Packet inputPacket = new Packet("GetInventoryStatus", "");

            mockedWarehouse.when(Warehouse::getInventoryStatus).thenReturn(expectedPacket);

            Packet result = coordinator.handlePacket(inputPacket);

            assertEquals(expectedPacket.type, result.type);
            assertEquals(expectedPacket.message, result.message);
            assertNotNull(result.warehouseItems);
            assertEquals(1, result.warehouseItems.size());
            mockedWarehouse.verify(Warehouse::getInventoryStatus);
        }
    }

    @Test
    void testHandlePacket_GetDeliveryInformation() {
        try (MockedStatic<Delivery> mockedDelivery = mockStatic(Delivery.class)) {
            Packet expectedPacket = new Packet("GetDeliveryInformation", "Success");
            Packet inputPacket = new Packet("GetDeliveryInformation", "");

            mockedDelivery.when(Delivery::getDeliveryInfo).thenReturn(expectedPacket);
            Packet result = coordinator.handlePacket(inputPacket);

            assertEquals(expectedPacket, result);
            mockedDelivery.verify(Delivery::getDeliveryInfo);
        }
    }

    @Test
    void testHandlePacket_CreateNewOrder(){
        try(MockedStatic<Order> mockedOrder = mockStatic(Order.class)){
            Order order = new Order();
            Packet expectedPacket = new Packet("CreateNewOrder","Success");
            Packet inputPacket = new Packet("CreateNewOrder","");
            inputPacket.orderInfo = order;

            mockedOrder.when(() -> Order.createOrder(order)).thenReturn(expectedPacket);
            Packet result = coordinator.handlePacket(inputPacket);

            assertEquals(expectedPacket,result);
            mockedOrder.verify(()-> Order.createOrder(order));

        }
    }

    @Test
    void testHandlePacket_GetOrderInformation() {
        try (MockedStatic<Order> mockedOrder = mockStatic(Order.class)) {
            Packet expectedPacket = new Packet("GetOrderInformation", "Success");
            Packet inputPacket = new Packet("GetOrderInformation", "");

            mockedOrder.when(Order::getOrderInfo).thenReturn(expectedPacket);
            Packet result = coordinator.handlePacket(inputPacket);

            assertEquals(expectedPacket.type, result.type);
            assertEquals(expectedPacket.message, result.message);

            mockedOrder.verify(Order::getOrderInfo);

        }
    }

    @Test
    void testHandlePacket_UpdateOrderStatus() {
        try (MockedStatic<Order> mockedOrder = mockStatic(Order.class)) {
            Order order = new Order();
            order.setOrderID(123);

            Packet expectedPacket = new Packet("UpdateOrderStatus", "Success");
            Packet inputPacket = new Packet("UpdateOrderStatus", "");
            inputPacket.orderInfo = order;

            mockedOrder.when(() -> Order.updateOrderStatus(order)).thenReturn(expectedPacket);

            Packet result = coordinator.handlePacket(inputPacket);

            assertEquals(expectedPacket, result);
            mockedOrder.verify(() -> Order.updateOrderStatus(order));
        }
    }

    @Test
    void testHandlePacket_UnsupportedType() {
        Packet inputPacket = new Packet("UnsupportedType", "");
        Packet result = coordinator.handlePacket(inputPacket);

        assertEquals("UnsupportedType", result.type);
        assertEquals("Unsupported in Coordinator", result.message);
    }


}