package Views.Manager;

import Classes.Manager.Employee;
import Classes.User.UserSession;
import Views.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ManagerViewController {

    @FXML private TableView<Employee> eventTable;
    @FXML private TableColumn<Employee, Integer> idColumn;
    @FXML private TableColumn<Employee, String> themeColumn;
    @FXML private TableColumn<Employee, String> dateColumn;
    @FXML private TableColumn<Employee, String> timeColumn;
    @FXML private TableColumn<Employee, String> placeColumn;
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
