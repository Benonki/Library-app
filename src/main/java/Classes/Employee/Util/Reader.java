package Classes.Employee.Util;

import java.io.Serializable;
import java.util.Date;

public class Reader implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String cardNumber;
    private Date issueDate;
    private Date expiryDate;
    private String cardStatus;

    public Reader(int id, String firstName, String lastName, String email, String phone, String password,
                  String cardNumber, Date issueDate, Date expiryDate, String cardStatus) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.cardNumber = cardNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.cardStatus = cardStatus;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }

    public String getCardNumber() { return cardNumber; }
    public Date getIssueDate() { return issueDate; }
    public Date getExpiryDate() { return expiryDate; }
    public String getCardStatus() { return cardStatus; }

    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }

    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }
}
