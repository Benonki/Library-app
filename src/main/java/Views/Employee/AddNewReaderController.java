package Views.Employee;

import Classes.Employee.Util.Reader;
import Server.Client;
import Server.Packet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AddNewReaderController implements Initializable {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;

    private static Reader readerToEdit = null;
    private boolean isEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (readerToEdit != null) {
            isEditMode = true;
            firstNameField.setText(readerToEdit.getFirstName());
            lastNameField.setText(readerToEdit.getLastName());
            emailField.setText(readerToEdit.getEmail());
            phoneField.setText(readerToEdit.getPhone());
            passwordField.setText(readerToEdit.getPassword());
        }
    }

    @FXML
    public void handleAddReader(ActionEvent event) {
        try {
            String imie = firstNameField.getText().trim();
            String nazwisko = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String telefon = phoneField.getText().trim();
            String haslo = passwordField.getText();

            if (imie.isBlank() || nazwisko.isBlank() || email.isBlank() || haslo.isBlank()) {
                System.out.println("Wszystkie pola wymagane (oprócz telefonu) muszą być wypełnione.");
                return;
            }

            if (imie.length() < 2 || nazwisko.length() < 2) {
                System.out.println("Imię i nazwisko muszą mieć co najmniej 2 znaki.");
                return;
            }

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("Niepoprawny format adresu e-mail.");
                return;
            }

            if (!telefon.isBlank() && !telefon.matches("^\\+?[0-9]{7,15}$")) {
                System.out.println("Niepoprawny numer telefonu");
                return;
            }

            if (haslo.length() < 6) {
                System.out.println("Hasło musi mieć co najmniej 6 znaków.");
                return;
            }

            Reader reader = new Reader(
                    isEditMode ? readerToEdit.getId() : 0,
                    imie, nazwisko, email, telefon, haslo
            );

            Packet packet = new Packet(isEditMode ? "EditReader" : "AddNewReader", "Czytelnik zapisany");
            packet.data = reader;
            Client.getInstance().sendPacket(packet);

            readerToEdit = null;
            isEditMode = false;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Wystąpił błąd podczas zapisu czytelnika.");
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        try {
            readerToEdit = null;
            isEditMode = false;
            new Views.SceneController().switchToReaderServiceView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setReaderToEdit(Reader reader) {
        readerToEdit = reader;
    }
}