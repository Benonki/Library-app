package Manager;

import Classes.Manager.Util.Event;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    void testConstructorAndGetters() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Event event = new Event(1, "Conference", date, time, "Room 101");

        assertEquals(1, event.getId());
        assertEquals("Conference", event.getTheme());
        assertEquals(date, event.getDate());
        assertEquals(time, event.getTime());
        assertEquals("Room 101", event.getPlace());
    }

    @Test
    void testSetters() {
        Event event = new Event(0, "", null, null, "");
        LocalDate newDate = LocalDate.of(2023, 12, 31);
        LocalTime newTime = LocalTime.of(14, 30);

        event.setId(2);
        event.setTheme("Meeting");
        event.setDate(newDate);
        event.setTime(newTime);
        event.setPlace("Room 202");

        assertEquals(2, event.getId());
        assertEquals("Meeting", event.getTheme());
        assertEquals(newDate, event.getDate());
        assertEquals(newTime, event.getTime());
        assertEquals("Room 202", event.getPlace());
    }

    @Test
    void testNullValuesInConstructor() {
        Event event = new Event(0, null, null, null, null);

        assertEquals("", event.getTheme());
        assertNotNull(event.getDate());
        assertNotNull(event.getTime());
        assertEquals("", event.getPlace());
    }
}