package Classes.Manager;

import java.io.Serializable;

public class Participant implements Serializable {
    private String fullName;
    private String email;

    public Participant(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}