package Views;

import Views.Manager.AddParticipantsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/LoginView.fxml");
    }

    public void switchToReaderView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Reader/ReaderView.fxml");
    }

    public void switchToEmployeeView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Employee/EmployeeView.fxml");
    }

    public void switchToManagerView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Manager/ManagerView.fxml");
    }

    public void switchToManageEmployees(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Manager/EmployeeManagementView.fxml");
    }

    public void switchToAddEditEmployees(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Manager/AddEditEmployeeView.fxml");
    }

    public void switchToCoordinatorView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Coordinator/CoordinatorView.fxml");
    }

    public void switchToCoordinatorOrderView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Coordinator/CoordinatorOrderView.fxml");
    }

    public void switchToCoordinatorOrdersInfo(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Coordinator/CoordinatorOrdersInfoView.fxml");
    }

    public void switchToAddEventView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Manager/AddEventView.fxml");
    }

    public static void switchToAddParticipantsView(ActionEvent event, int eventId) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/Views/Manager/AddParticipantsView.fxml"));
        Parent root = loader.load();

        AddParticipantsController controller = loader.getController();
        controller.initializeWithEventId(eventId);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
