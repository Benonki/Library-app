package Manager;

import Classes.Manager.Util.Participant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    @Test
    void testConstructorAndGetters() {
        Participant participant = new Participant("John Doe", "john@example.com");

        assertEquals("John Doe", participant.getFullName());
        assertEquals("john@example.com", participant.getEmail());
    }
}