package Views.Main;

import Views.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainViewController {

    private SceneController sceneController = new SceneController();
    @FXML
    private Button backButton;
    @FXML
    private Label testLabel;

    @FXML
    public void switchToLoginView(ActionEvent event) {
        try {
            sceneController.switchToLoginView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
