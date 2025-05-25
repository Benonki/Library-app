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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

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
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Szczegóły wydarzenia");
        dialog.setHeaderText("Szczegóły wydarzenia: " + event.getTheme());

        // Główny kontener
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // Informacje o wydarzeniu
        Label eventInfo = new Label(
                "ID: " + event.getId() + "\n" +
                        "Temat: " + event.getTheme() + "\n" +
                        "Data: " + event.getDate() + "\n" +
                        "Godzina: " + event.getTime() + "\n" +
                        "Miejsce: " + event.getPlace()
        );

        // Lista uczestników
        Label participantsLabel = new Label("Uczestnicy:");
        ListView<String> participantsList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        if (participants.isEmpty()) {
            items.add("Brak uczestników");
        } else {
            for (Participant p : participants) {
                items.add("- " + p.getFullName() + " (" + p.getEmail() + ")");
            }
        }

        participantsList.setItems(items);
        participantsList.setPrefHeight(200); // Ustaw wysokość listy

        // Dodaj elementy do kontenera
        vbox.getChildren().addAll(eventInfo, participantsLabel, participantsList);

        // Ustaw zawartość dialogu
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().setPrefSize(400, 400); // Większe wymiary dla lepszej czytelności
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        // Wyświetl dialog
        dialog.showAndWait();
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
