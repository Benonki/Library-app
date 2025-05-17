package Views.Coordinator;

import Classes.Coordinator.Order;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoordinatorOrdersInfoViewController {

    private SceneController sceneController = new SceneController();
    private Order selectedOrderToDelete = new Order();

    @FXML
    private Button backButton;
    @FXML
    private Button deleteOrder;
    @FXML
    private TableView<Order> ordersTableView;
    @FXML
    private TableColumn<Order, String> orderDeliveryNameTableColumn;
    @FXML
    private TableColumn<Order, Integer> orderAmountTableColumn;
    @FXML
    private TableColumn<Order, Integer> orderIDTableColumn;
    @FXML
    private TableColumn<Order, Date> orderCreationDateTableColumn;
    @FXML
    private TableColumn<Order, Date> orderRealizationDateTableColumn;
    @FXML
    private TableColumn<Order, String> orderStatusTableColumn;


    @FXML
    public void initialize() {
        orderDeliveryNameTableColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDeliveryService().getName()));

        orderAmountTableColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAmount()).asObject());

        orderStatusTableColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        orderCreationDateTableColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getCreationDate()));

        orderRealizationDateTableColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getRealizationDate()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        orderCreationDateTableColumn.setCellFactory(column ->
                new TextFieldTableCell<>(new StringConverter<Date>() {
                    @Override
                    public String toString(Date date) {
                        return date != null ? dateFormat.format(date) : "";
                    }

                    @Override
                    public Date fromString(String string) {
                        try {
                            return dateFormat.parse(string);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }));

        orderRealizationDateTableColumn.setCellFactory(column ->
                new TextFieldTableCell<>(new StringConverter<Date>() {
                    @Override
                    public String toString(Date date) {
                        return date != null ? dateFormat.format(date) : "";
                    }

                    @Override
                    public Date fromString(String string) {
                        try {
                            return dateFormat.parse(string);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }));

        Client client = Client.getInstance();

        client.setOrdersCallback(items ->{
            Platform.runLater(()->{
                ordersTableView.getItems().setAll(items);
            });
        });

        client.sendPacket(new Packet("GetOrderInformation", "TEST"));

        selectOrder();

    }

    @FXML
    protected void goBack(ActionEvent event){
        try {
            sceneController.switchToCoordinatorView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void selectOrder(){
        ordersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.getStatus().equals("Zakończone")) {
                selectedOrderToDelete = newVal;
                System.out.println(newVal.getOrderID());
            }else {
                selectedOrderToDelete = null;
            }
        });
    }

    @FXML
    protected void deleteOrder(){
        if(selectedOrderToDelete != null){
            Client client = Client.getInstance();
            Packet packet = Packet.withOrderInfo("DeleteOrder","TEST",selectedOrderToDelete);
            client.sendPacket(packet);
            ordersTableView.getItems().remove(selectedOrderToDelete);
        }else {
            System.out.println("Nie wybrano zamówienia");
        }
    }


}
