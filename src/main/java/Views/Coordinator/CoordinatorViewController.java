package Views.Coordinator;

import Classes.Coordinator.Util.InventoryItem;
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
    private Button checkOrders;
    @FXML
    private Button getInventoryBtn;
    @FXML
    private Button makeAnOrderBtn;
    @FXML
    private Label testLabel;
    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableColumn<InventoryItem, Integer> magazynId;
    @FXML
    private TableColumn<InventoryItem, String> tytulKsiazki;
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
        tytulKsiazki.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTytul()));
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
    public void getInventory(ActionEvent event){


        Client client = Client.getInstance();

        client.setInventoryCallback(items -> {
            Platform.runLater(() -> {
                inventoryTable.getItems().setAll(items);
            });
        });

        client.sendPacket(new Packet("GetInventoryStatus", "TEST"));
    }

    @FXML
    public void switchToOrderView(ActionEvent event) {
        try {
            sceneController.switchToCoordinatorOrderView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToOrdersInfo(ActionEvent event) {
        try {
            sceneController.switchToCoordinatorOrdersInfo(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
