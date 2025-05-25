package Classes.Manager;

import java.io.Serializable;

public class Employee implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private int roleId;

    public Employee(int id, String firstName, String lastName, String email, String phone, String password, int roleId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roleId = roleId;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public int getRoleId() { return roleId; }

    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}