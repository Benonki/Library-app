package Views.Employee;

import Classes.Employee.Util.Reader;
import Server.Client;
import Server.Packet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.util.Date;

public class EmployeeReaderServiceViewController {

    @FXML private TableView<Reader> readersTable;

    @FXML private TableColumn<Reader, Integer> idColumn;
    @FXML private TableColumn<Reader, String> firstNameColumn;
    @FXML private TableColumn<Reader, String> lastNameColumn;
    @FXML private TableColumn<Reader, String> emailColumn;
    @FXML private TableColumn<Reader, String> phoneColumn;
    @FXML private TableColumn<Reader, String> cardNumberColumn;
    @FXML private TableColumn<Reader, Date> issueDateColumn;
    @FXML private TableColumn<Reader, Date> expiryDateColumn;
    @FXML private TableColumn<Reader, String> cardStatusColumn;

    private ObservableList<Reader> readers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        cardStatusColumn.setCellValueFactory(new PropertyValueFactory<>("cardStatus"));

        readersTable.setItems(readers);

        Client.getInstance().setReadersCallback(receivedReaders -> {
            Platform.runLater(() -> {
                if (receivedReaders != null) {
                    readers.setAll(receivedReaders);
                }
            });
        });
        refreshReadersList();
    }

    private void refreshReadersList() {
        Client.getInstance().sendPacket(new Packet("getReadersList", "Request"));
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            new Views.SceneController().switchToEmployeeView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddNewReader(ActionEvent event) {
        try {
            new Views.SceneController().switchScene(event, "/Views/Employee/AddNewReaderView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteReader(ActionEvent event) {
        Reader selectedReader = readersTable.getSelectionModel().getSelectedItem();
        if (selectedReader != null) {
            int readerId = selectedReader.getId();
            Packet packet = new Packet("DeleteReader", "Usuń czytelnika");
            packet.data = readerId;
            Client.getInstance().sendPacket(packet);
            refreshReadersList();
        } else {
            System.out.println("Brak wyboru");
        }
    }

    @FXML
    public void handleEditReader(ActionEvent event) {
        Reader selectedReader = readersTable.getSelectionModel().getSelectedItem();
        if (selectedReader != null) {
            AddNewReaderController.setReaderToEdit(selectedReader);
            try {
                new Views.SceneController().switchScene(event, "/Views/Employee/AddNewReaderView.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Brak zaznaczonego czytelnika do edycji.");
        }
    }
}
