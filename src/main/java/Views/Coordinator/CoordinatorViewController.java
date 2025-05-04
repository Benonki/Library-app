package Views.Coordinator;

import Classes.Coordinator.InventoryItem;
import Classes.User.UserSession;
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
    private Button getInventoryBtn;
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
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        String username = UserSession.getUsername();
        welcomeLabel.setText("Witaj " + username + "!");
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
            UserSession.clear();
            sceneController.switchToLoginView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getInventory(ActionEvent event){
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
