package Views.Manager;

import Classes.User.UserSession;
import Views.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ManagerViewController {

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
