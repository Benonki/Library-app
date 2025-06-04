package Views.Employee;

import Classes.User.UserSession;
import Views.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EmployeeViewController {

    private SceneController sceneController = new SceneController();
    @FXML
    private Button backButton;
    @FXML
    private Label testLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button returnToManagerButton;
    @FXML
    private Button libraryResourcesButton;

    @FXML
    public void initialize() {
        String username = UserSession.getUsername();
        welcomeLabel.setText("Witaj " + username + "!");

        if ("Kierownik".equals(UserSession.getRole())) {
            returnToManagerButton.setVisible(true);
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
    public void returnToManagerView(ActionEvent event) {
        try {
            sceneController.switchToManagerView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showLibraryResources(ActionEvent event) {
        try {
            sceneController.switchToLibraryResourcesView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
