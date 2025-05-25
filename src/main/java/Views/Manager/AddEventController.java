package Views.Manager;

import Classes.Manager.Util.Event;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddEventController {
    private SceneController sceneController = new SceneController();
    @FXML private TextField themeField;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextField placeField;

    @FXML
    public void handleNextButton(ActionEvent event) {
        try {
            String theme = themeField.getText();
            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.parse(timeField.getText());
            String place = placeField.getText();

            if (theme.isEmpty() || date == null || place.isEmpty()) {
                showAlert("Błąd", "Wszystkie pola muszą być wypełnione");
                return;
            }

            Client client = Client.getInstance();
            client.setCallBack((success, message) -> {
                Platform.runLater(() -> {
                    if (success) {
                        try {
                            int eventId = (Integer) client.getLastPacket().data;
                            SceneController.switchToAddParticipantsView(event, eventId);
                        } catch (Exception e) {
                            showAlert("Błąd", "Problem z przetworzeniem ID wydarzenia: " + e.getMessage());
                        }
                    } else {
                        showAlert("Błąd", "Nie udało się utworzyć wydarzenia: " + message);
                    }
                });
            });

            String eventData = theme + "|" + date + "|" + time + "|" + place;
            client.sendPacket(new Packet("CreateEvent", eventData));
        } catch (DateTimeParseException e) {
            showAlert("Błąd", "Nieprawidłowy format godziny. Użyj formatu HH:mm");
        } catch (Exception e) {
            showAlert("Błąd", "Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }


    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        new SceneController().switchToManagerView(event);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}