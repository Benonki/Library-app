package Views.Manager;

import Classes.Manager.Event;
import Classes.Manager.Participant;
import Classes.User.UserSession;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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
    private Button switchToEmployeeButton;
    @FXML
    private Button viewDetailsButton;

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
    public void viewEventDetails(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak wybranego wydarzenia");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wybrać wydarzenie z tabeli");
            alert.showAndWait();
            return;
        }

        try {
            Client client = Client.getInstance();
            client.setParticipantsCallback(participants -> {
                Platform.runLater(() -> {
                    showEventDetailsDialog(selectedEvent, participants);
                });
            });
            client.sendPacket(new Packet("GetEventParticipants", String.valueOf(selectedEvent.getId())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEventDetailsDialog(Event event, List<Participant> participants) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Szczegóły wydarzenia");
        alert.setHeaderText("Szczegóły wydarzenia: " + event.getTheme());

        StringBuilder content = new StringBuilder();
        content.append("ID: ").append(event.getId()).append("\n");
        content.append("Temat: ").append(event.getTheme()).append("\n");
        content.append("Data: ").append(event.getDate()).append("\n");
        content.append("Godzina: ").append(event.getTime()).append("\n");
        content.append("Miejsce: ").append(event.getPlace()).append("\n\n");
        content.append("Uczestnicy:\n");

        if (participants.isEmpty()) {
            content.append("Brak uczestników");
        } else {
            for (Participant p : participants) {
                content.append("- ").append(p.getFullName()).append(" (").append(p.getEmail()).append(")\n");
            }
        }

        alert.setContentText(content.toString());
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(400, 300);
        alert.showAndWait();
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
