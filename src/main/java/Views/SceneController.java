package Views;

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
        switchScene(event, "/Views/ReaderView.fxml");
    }

    public void switchToEmployeeView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/EmployeeView.fxml");
    }

    public void switchToManagerView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/ManagerView.fxml");
    }

    public void switchToCoordinatorView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/CoordinatorView.fxml");
    }

    public void switchToCoordinatorOrderView(ActionEvent event) throws IOException {
        switchScene(event, "/Views/CoordinatorOrderView.fxml");
    }
}
