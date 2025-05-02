package Views.Coordinator;

import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CoordinatorViewController {

    private SceneController sceneController = new SceneController();
    @FXML
    private Button backButton;
    @FXML
    private Button testInventory;
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

    @FXML
    public void testInvetory(ActionEvent event){
        System.out.println("BUTTON WORKS");
        Client client = Client.getInstance();
        client.sendPacket(new Packet("GetInventoryStatus","TEST"));
    }

}
