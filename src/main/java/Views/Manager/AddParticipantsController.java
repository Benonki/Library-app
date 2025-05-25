package Views.Manager;

import Classes.Manager.Util.Participant;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddParticipantsController {
    @FXML private TableView<Participant> allUsersTable;
    @FXML private ListView<String> invitedUsersList;

    private int eventId;
    private ObservableList<String> invitedEmails = FXCollections.observableArrayList();

    public void initializeWithEventId(int eventId) {
        this.eventId = eventId;
        loadAllUsers();
        invitedUsersList.setItems(invitedEmails);
    }

    private void loadAllUsers() {
        Client client = Client.getInstance();
        client.setParticipantsCallback(participants -> {
            ObservableList<Participant> users = FXCollections.observableArrayList(participants);

            allUsersTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("fullName"));
            allUsersTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("email"));
            allUsersTable.setItems(users);
        });
        client.sendPacket(new Packet("GetAllUsers", "Requesting all users"));
    }

    @FXML
    public void handleInviteButton(ActionEvent event) {
        Participant selected = allUsersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String formatted = selected.getFullName() + " <" + selected.getEmail() + ">";
            if (!invitedEmails.contains(formatted)) {
                invitedEmails.add(formatted);
            }
        }
    }

    @FXML
    public void handleFinishButton(ActionEvent event) throws IOException {
        if (invitedEmails.isEmpty()) {
            new SceneController().switchToManagerView(event);
            return;
        }

        Client client = Client.getInstance();
        client.setCallBack((success, message) -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setTitle("Dodawanie uczestników");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
                try {
                    new SceneController().switchToManagerView(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        List<String> emails = invitedEmails.stream()
                .map(s -> s.substring(s.indexOf("<") + 1, s.indexOf(">")))
                .collect(Collectors.toList());

        String participantsData = eventId + "|" + String.join("|", emails);
        client.sendPacket(new Packet("AddParticipants", participantsData));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}