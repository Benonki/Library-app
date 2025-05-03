package Views.Coordinator;

import Classes.Coordinator.InventoryItem;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CoordinatorViewController {

    private SceneController sceneController = new SceneController();
    @FXML
    private Button backButton;
    @FXML
    private Button testInventory;
    @FXML
    private Label testLabel;
    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableColumn<InventoryItem, Integer> magazynId;
    @FXML
    private TableColumn<InventoryItem, Integer> ksiazkaId;
    @FXML
    private TableColumn<InventoryItem, Integer> ilosc;
    @FXML
    private TableColumn<InventoryItem, Integer> sektor;
    @FXML
    private TableColumn<InventoryItem, Integer> rzad;
    @FXML
    private TableColumn<InventoryItem, Integer> polka;
    @FXML
    private TableColumn<InventoryItem, Integer> miejsce;

    @FXML
    public void initialize() {
        magazynId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getMagazynId()).asObject());
        ksiazkaId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getKsiazkaId()).asObject());
        ilosc.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIlosc()).asObject());
        sektor.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSektor()).asObject());
        rzad.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRzad()).asObject());
        polka.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPolka()).asObject());
        miejsce.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getMiejsceNaPolce()).asObject());
    }

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

        client.setInventoryCallback(items -> {
            Platform.runLater(() -> {
                inventoryTable.getItems().setAll(items);
            });
        });

        client.sendPacket(new Packet("GetInventoryStatus", "TEST"));
    }

}
