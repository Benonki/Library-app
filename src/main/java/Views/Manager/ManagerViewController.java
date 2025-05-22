package Views.Manager;

import Classes.Manager.Event;
import Classes.User.UserSession;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManagerViewController {

    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> idColumn;
    @FXML private TableColumn<Event, String> themeColumn;
    @FXML private TableColumn<Event, String> dateColumn;
    @FXML private TableColumn<Event, String> timeColumn;
    @FXML private TableColumn<Event, String> placeColumn;
    private SceneController sceneController = new SceneController();
    @FXML
    private Button backButton;
    @FXML
    private Label testLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button manageEmployeesButton;

    @FXML
    public void initialize() {
        String username = UserSession.getUsername();
        welcomeLabel.setText("Witaj " + username + "!");

        initializeTableColumns();
        loadEvents();
    }

    private void initializeTableColumns() {
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        } catch (Exception e) {
            System.err.println("Error initializing table columns: " + e.getMessage());
        }
    }

    private void loadEvents() {
        try {
            Client client = Client.getInstance();
            client.setEventsCallback(events -> {
                if (events != null && !events.isEmpty()) {
                    ObservableList<Event> eventData = FXCollections.observableArrayList(events);
                    Platform.runLater(() -> {
                        eventTable.setItems(eventData);
                        System.out.println("Loaded " + events.size() + " events");
                    });
                } else {
                    Platform.runLater(() -> {
                        System.out.println("No events received or empty list");
                    });
                }
            });
            client.sendPacket(new Packet("GetEvents", "Requesting events"));
        } catch (Exception e) {
            System.err.println("Error loading events: " + e.getMessage());
        }
    }

    @FXML
    public void switchToLoginView(ActionEvent event) {
        try {
            UserSession.clear();
            sceneController.switchToLoginView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToManageEmployees(ActionEvent event) {
        try {
            sceneController.switchToManageEmployees(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToEmployeeView(ActionEvent event) {
        try {
            sceneController.switchToEmployeeView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
