package Views.Manager;

import Classes.Manager.Employee;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEditEmployeeController implements Initializable {

    private SceneController sceneController = new SceneController();
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label titleLabel;

    private static Employee employeeToEdit;
    private boolean editMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (employeeToEdit != null) {
            editMode = true;
            titleLabel.setText("Edytuj pracownika");
            firstNameField.setText(employeeToEdit.getFirstName());
            lastNameField.setText(employeeToEdit.getLastName());
            emailField.setText(employeeToEdit.getEmail());
            phoneField.setText(employeeToEdit.getPhone());
            emailField.setDisable(true);
        } else {
            titleLabel.setText("Dodaj nowego pracownika");
        }
    }

    public static void setEmployeeToEdit(Employee employee) {
        employeeToEdit = employee;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Missing Fields", "First name, last name, and email are required.");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Error", "Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!editMode && password.isEmpty()) {
            showAlert("Error", "Missing Password", "Password is required when creating a new employee.");
            return;
        }

        Client client = Client.getInstance();
        client.setCallBack((success, message) -> {
            Platform.runLater(() -> {
                showAlert(success ? "Success" : "Error",
                        success ? "Operation Successful" : "Operation Failed",
                        message);
                if (success) {
                    ((Stage) saveButton.getScene().getWindow()).close();
                }
            });
        });

        Employee employee = new Employee(
                editMode ? employeeToEdit.getId() : 0,
                firstName,
                lastName,
                email,
                phone,
                password,
                3
        );

        Packet packet;
        if (editMode) {
            packet = new Packet("UpdateEmployee", "Update employee request");
        } else {
            packet = new Packet("CreateEmployee", "Create employee request");
        }
        packet.data = employee;
        client.sendPacket(packet);

        try {
            sceneController.switchToManageEmployees(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        employeeToEdit = null;
        try {
            new SceneController().switchToManageEmployees(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}