package Manager;

import Classes.Manager.EmployeeManager;
import Classes.Manager.EventManager;
import Classes.Manager.ManagerImpl;
import Classes.Manager.Util.Employee;
import Classes.Manager.Util.Event;
import Classes.Manager.Util.Participant;
import Server.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManagerImplTest {

    private ManagerImpl manager;
    private Packet testPacket;

    @BeforeEach
    void setUp() {
        manager = new ManagerImpl("testManager");
    }

    @Test
    void handlePacket_GetEmployees_CallsCorrectHandler() {
        testPacket = new Packet("GetEmployees", "");

        try (MockedStatic<EmployeeManager> mocked = Mockito.mockStatic(EmployeeManager.class)) {
            Packet expectedResponse = new Packet("GetEmployees", "success");
            mocked.when(EmployeeManager::getEmployees).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(EmployeeManager::getEmployees);
        }
    }

    @Test
    void handlePacket_CreateEmployee_CallsCorrectHandler() {
        Employee testEmployee = new Employee(0, "John", "Doe", "john@test.com", "123456789", "pass", 3);
        testPacket = new Packet("CreateEmployee", "");
        testPacket.data = testEmployee;

        try (MockedStatic<EmployeeManager> mocked = Mockito.mockStatic(EmployeeManager.class)) {
            Packet expectedResponse = new Packet("CreateEmployee", "success");
            mocked.when(() -> EmployeeManager.createEmployee(any())).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(() -> EmployeeManager.createEmployee(testPacket));
        }
    }

    @Test
    void handlePacket_UpdateEmployee_CallsCorrectHandler() {
        Employee testEmployee = new Employee(1, "John", "Doe", "john@test.com", "123456789", "pass", 3);
        testPacket = new Packet("UpdateEmployee", "");
        testPacket.data = testEmployee;

        try (MockedStatic<EmployeeManager> mocked = Mockito.mockStatic(EmployeeManager.class)) {
            Packet expectedResponse = new Packet("UpdateEmployee", "success");
            mocked.when(() -> EmployeeManager.updateEmployee(any())).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(() -> EmployeeManager.updateEmployee(testPacket));
        }
    }

    @Test
    void handlePacket_DeleteEmployee_CallsCorrectHandler() {
        testPacket = new Packet("DeleteEmployee", "");
        testPacket.data = 1;

        try (MockedStatic<EmployeeManager> mocked = Mockito.mockStatic(EmployeeManager.class)) {
            Packet expectedResponse = new Packet("DeleteEmployee", "success");
            mocked.when(() -> EmployeeManager.deleteEmployee(any())).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(() -> EmployeeManager.deleteEmployee(testPacket));
        }
    }

    @Test
    void handlePacket_GetEvents_CallsCorrectHandler() {
        testPacket = new Packet("GetEvents", "");

        try (MockedStatic<EventManager> mocked = Mockito.mockStatic(EventManager.class)) {
            Packet expectedResponse = new Packet("GetEvents", "success");
            mocked.when(EventManager::getEvents).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(EventManager::getEvents);
        }
    }

    @Test
    void handlePacket_GetEventParticipants_ValidId_CallsCorrectHandler() {
        testPacket = new Packet("GetEventParticipants", "1");

        try (MockedStatic<EventManager> mocked = Mockito.mockStatic(EventManager.class)) {
            Packet expectedResponse = new Packet("GetEventParticipants", "success");
            mocked.when(() -> EventManager.getEventParticipants(1)).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(() -> EventManager.getEventParticipants(1));
        }
    }

    @Test
    void handlePacket_GetEventParticipants_InvalidId_ReturnsError() {
        testPacket = new Packet("GetEventParticipants", "invalid");

        Packet response = manager.handlePacket(testPacket);

        assertEquals("GetEventParticipants", response.type);
        assertTrue(response.message.contains("Invalid event ID format"));
    }

    @Test
    void handlePacket_CreateEvent_ValidData_CallsCorrectHandler() {
        String eventData = "Test Event|2023-12-31|12:00|Test Location";
        testPacket = new Packet("CreateEvent", eventData);

        try (MockedStatic<EventManager> mocked = Mockito.mockStatic(EventManager.class)) {
            Packet expectedResponse = new Packet("CreateEvent", "success");
            mocked.when(() -> EventManager.createEvent(any(), any(), any(), any())).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(() -> EventManager.createEvent(
                    "Test Event",
                    LocalDate.of(2023, 12, 31),
                    LocalTime.of(12, 0),
                    "Test Location"
            ));
        }
    }

    @Test
    void handlePacket_CreateEvent_InvalidData_ReturnsError() {
        testPacket = new Packet("CreateEvent", "invalid|data|format");

        Packet response = manager.handlePacket(testPacket);

        assertEquals("CreateEvent", response.type);
        assertTrue(response.message.contains("Invalid event data format"));
    }

    @Test
    void handlePacket_AddParticipants_ValidData_CallsCorrectHandler() {
        String participantsData = "1|user1@test.com|user2@test.com";
        testPacket = new Packet("AddParticipants", participantsData);

        try (MockedStatic<EventManager> mocked = Mockito.mockStatic(EventManager.class)) {
            Packet expectedResponse = new Packet("AddParticipants", "success");
            List<String> expectedEmails = List.of("user1@test.com", "user2@test.com");

            mocked.when(() -> EventManager.addParticipants(1, expectedEmails)).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(() -> EventManager.addParticipants(1, expectedEmails));
        }
    }

    @Test
    void handlePacket_AddParticipants_InvalidData_ReturnsError() {
        testPacket = new Packet("AddParticipants", "invalid|data");

        Packet response = manager.handlePacket(testPacket);

        assertEquals("AddParticipants", response.type);
        assertTrue(response.message.contains("Invalid participant data format"));
    }

    @Test
    void handlePacket_GetAllUsers_CallsCorrectHandler() {
        testPacket = new Packet("GetAllUsers", "");

        try (MockedStatic<EventManager> mocked = Mockito.mockStatic(EventManager.class)) {
            List<Participant> participants = new ArrayList<>();
            participants.add(new Participant("User One", "user1@test.com"));
            Packet expectedResponse = Packet.withParticipants("GetAllUsers", participants);

            mocked.when(EventManager::getAllUsers).thenReturn(expectedResponse);

            Packet response = manager.handlePacket(testPacket);

            assertEquals(expectedResponse, response);
            mocked.verify(EventManager::getAllUsers);
        }
    }

    @Test
    void handlePacket_UnknownType_ForwardsToSuperclass() {
        testPacket = new Packet("UnknownType", "test");

        try (MockedStatic<EmployeeManager> mockedEmp = Mockito.mockStatic(EmployeeManager.class);
             MockedStatic<EventManager> mockedEvt = Mockito.mockStatic(EventManager.class)) {

            ManagerImpl spyManager = spy(manager);

            Packet response = spyManager.handlePacket(testPacket);

            assertNotEquals("GetEmployees", response.type);
            assertNotEquals("CreateEmployee", response.type);
            assertNotEquals("UpdateEmployee", response.type);
            assertNotEquals("DeleteEmployee", response.type);
            assertNotEquals("GetEvents", response.type);
            assertNotEquals("GetEventParticipants", response.type);
            assertNotEquals("CreateEvent", response.type);
            assertNotEquals("AddParticipants", response.type);
            assertNotEquals("GetAllUsers", response.type);

            assertEquals("UnknownType", response.type);
            assertNotNull(response.message);

            mockedEmp.verifyNoInteractions();
            mockedEvt.verifyNoInteractions();

            mockedEmp.verify(() -> EmployeeManager.getEmployees(), never());
            mockedEmp.verify(() -> EmployeeManager.createEmployee(any()), never());
            mockedEmp.verify(() -> EmployeeManager.updateEmployee(any()), never());
            mockedEmp.verify(() -> EmployeeManager.deleteEmployee(any()), never());

            mockedEvt.verify(() -> EventManager.getEvents(), never());
            mockedEvt.verify(() -> EventManager.getEventParticipants(anyInt()), never());
            mockedEvt.verify(() -> EventManager.createEvent(any(), any(), any(), any()), never());
            mockedEvt.verify(() -> EventManager.addParticipants(anyInt(), anyList()), never());
            mockedEvt.verify(() -> EventManager.getAllUsers(), never());
        }
    }

    @Test
    void handlePacket_UnknownType_ReturnsDefaultResponse() {
        testPacket = new Packet("UnknownType", "test");

        try (MockedStatic<EmployeeManager> mockedEmp = Mockito.mockStatic(EmployeeManager.class);
             MockedStatic<EventManager> mockedEvt = Mockito.mockStatic(EventManager.class)) {

            Packet response = manager.handlePacket(testPacket);

            assertEquals("UnknownType", response.type);
            assertEquals("Unsupported in Employee", response.message);

            mockedEmp.verifyNoInteractions();
            mockedEvt.verifyNoInteractions();
        }
    }


}