package Views.Manager;

import Classes.Manager.Util.Employee;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeManagementController implements Initializable {

    @FXML private TableView<Employee> employeesTable;
    @FXML private TableColumn<Employee, Integer> idColumn;
    @FXML private TableColumn<Employee, String> firstNameColumn;
    @FXML private TableColumn<Employee, String> lastNameColumn;
    @FXML private TableColumn<Employee, String> emailColumn;
    @FXML private TableColumn<Employee, String> phoneColumn;
    @FXML private Button loadEmployeesButton;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private ObservableList<Employee> employeesData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    @FXML
    private void handleLoadEmployees(ActionEvent event) {
        loadEmployees();
    }

    private void loadEmployees() {
        Client client = Client.getInstance();
        client.sendPacket(new Packet("GetEmployees", "Request employees list"));

        client.setEmployeesCallback(employees -> {
            employeesData.clear();
            employeesData.addAll(employees);
            employeesTable.setItems(employeesData);
        });
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        try {
            AddEditEmployeeController.setEmployeeToEdit(null);
            new SceneController().switchToAddEditEmployees(event);
            loadEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditEmployee(ActionEvent event) {
        Employee selectedEmployee = employeesTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Błąd", "Nie wybrano pracownika", "Proszę wybrać pracownika do edycji.");
            return;
        }

        try {
            AddEditEmployeeController.setEmployeeToEdit(selectedEmployee);
            new SceneController().switchToAddEditEmployees(event);
            loadEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteEmployee(ActionEvent event) {
        Employee selectedEmployee = employeesTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Błąd", "Nie wybrano pracownika", "Proszę wybrać pracownika do usunięcia.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie usunięcia");
        alert.setHeaderText("Czy na pewno chcesz usunąć pracownika?");
        alert.setContentText("Ta operacja jest nieodwracalna.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            Client client = Client.getInstance();
            Packet packet = new Packet("DeleteEmployee", "Delete employee");
            packet.data = selectedEmployee.getId();
            client.sendPacket(packet);
            loadEmployees();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            new SceneController().switchToManagerView(event);
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