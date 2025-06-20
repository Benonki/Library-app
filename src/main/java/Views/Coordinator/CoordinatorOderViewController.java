package Views.Coordinator;

import Classes.Coordinator.Delivery;
import Classes.Coordinator.Order;
import Classes.Coordinator.Util.BookOrder;
import Classes.Coordinator.Util.InventoryItem;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CoordinatorOderViewController {
    private SceneController sceneController = new SceneController();


    @FXML
    private Button backBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button finishBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button getInventoryBtn;
    @FXML
    private Button selectBtn;
    @FXML
    private TableView<Delivery> deliveryTableView;
    @FXML
    private TableColumn<Delivery, String> deliveryStringTableColumn;
    @FXML
    private TableView<BookOrder> bookOrderTableView;
    @FXML
    private TableColumn<BookOrder, String> bookOrderStringTableColumn;
    @FXML
    private TableColumn<BookOrder, Integer> bookOrderIntegerTableColumn;
    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableColumn<InventoryItem, String> bookTitle;
    @FXML
    private TableColumn<InventoryItem, Integer> amount;

    private List<BookOrder> booksToOrder = new ArrayList<>();
    private Delivery selectedDeliveryOption = new Delivery("");

    @FXML
    public void initialize() {
        bookTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTytul()));
        amount.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIlosc()).asObject());

        bookOrderStringTableColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        bookOrderIntegerTableColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAmount()).asObject());

        deliveryStringTableColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
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
    protected void getInventory(ActionEvent event){
        System.out.println("BUTTON WORKS");

        Client client = Client.getInstance();

        client.setInventoryCallback(items -> {
            Platform.runLater(() -> {
                inventoryTable.getItems().setAll(items);
            });
        });

        client.setDeliveryCallback(items ->{
            Platform.runLater(() -> {
                deliveryTableView.getItems().setAll(items);
            });
        });

        client.sendPacket(new Packet("GetInventoryStatus", "TEST"));
        client.sendPacket(new Packet("GetDeliveryInformation","TEST"));
    }

    @FXML
    protected void addBookToOrder(){
        ObservableList<InventoryItem> selectedBooks;
        selectedBooks = inventoryTable.getSelectionModel().getSelectedItems();

        if(!selectedBooks.isEmpty()){
            String selectedTitle = selectedBooks.get(0).getTytul();
            int selectedBookId = selectedBooks.get(0).getKsiazkaID();
            Boolean bookAlreadyAdded = false;
            for(BookOrder book : booksToOrder) {
                if (book.getTitle().equals(selectedTitle)) {
                    bookAlreadyAdded = true;
                    break;
                }
            }

            if(!bookAlreadyAdded){
                TextInputDialog tiDialog = new TextInputDialog();
                tiDialog.setTitle("Ilość do zamówienia");
                tiDialog.setHeaderText("Podaj ilość: ");
                tiDialog.setContentText("Ilość: ");

                Optional<String> result = tiDialog.showAndWait();
                if(result.isPresent()){
                    int resultInt = Integer.parseInt(result.get());
                    booksToOrder.add(new BookOrder(resultInt, selectedTitle, selectedBookId));
                    Platform.runLater(() -> {
                        bookOrderTableView.getItems().setAll(booksToOrder);
                    });
                }
            }
            for(BookOrder book : booksToOrder){
                System.out.println(book.getTitle() + " w ilości: " + book.getAmount());
            }
        }

    }

    @FXML
    protected void daleteBookFromOrder(){

        ObservableList<BookOrder> selectedBooksFromOrder;
        selectedBooksFromOrder = bookOrderTableView.getSelectionModel().getSelectedItems();

        if(!selectedBooksFromOrder.isEmpty()){
            String selectedTitle = selectedBooksFromOrder.get(0).getTitle();

            List<BookOrder> toRemove = new ArrayList<>();

            for(BookOrder book : booksToOrder){
                if(book.getTitle().equals(selectedTitle)){
                    toRemove.add(book);
                }
            }

            booksToOrder.removeAll(toRemove);

            Platform.runLater(() -> {
                bookOrderTableView.getItems().setAll(booksToOrder);
                System.out.println("Current books in order after deletion:");
                for (BookOrder book : booksToOrder) {
                    System.out.println(book.getTitle() + " - " + book.getAmount());
                }
            });
        }
    }

    @FXML
    protected void selectDelivery(){
        ObservableList<Delivery> selectedDelivery;
        selectedDelivery = deliveryTableView.getSelectionModel().getSelectedItems();

        if(!selectedDelivery.isEmpty()){
            if(selectedDeliveryOption.getName().isEmpty() || !selectedDeliveryOption.getName().equals(selectedDelivery.get(0).getName())){
                selectedDeliveryOption = selectedDelivery.get(0);
            }

        }
    }

    @FXML
    protected void finishOrder(){
        if(booksToOrder.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak książek");
            alert.setHeaderText("Nie wybrano żadnych książek");
            alert.setContentText("Wybierz książki które chcesz zamówić");
            alert.showAndWait();
            return;
        }

        if(selectedDeliveryOption.getName().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak dostawcy");
            alert.setHeaderText("Nie wybrano żadnego dostawcy");
            alert.setContentText("Wybierz dostawcę");
            alert.showAndWait();
            return;
        }

        StringBuilder booksList = new StringBuilder();

        for (BookOrder book : booksToOrder) {
            booksList.append("- ").append(book.getTitle())
                    .append(" ilość: ").append(book.getAmount()).append("\n");
        }

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Potwierdzenie zamówienia");
        confirmationDialog.setHeaderText("Sprawdź czy zamówienia się zgadza");
        confirmationDialog.setContentText("Dostawca: " + selectedDeliveryOption.getName() +
                "\n" + "Wybrane książki: \n" + booksList.toString()
        );

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){

            int amountOfBooks = 0;

            for(BookOrder book : booksToOrder){
                amountOfBooks += book.getAmount();
            }

            LocalDate localDate = LocalDate.now();
            Date currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Client client = Client.getInstance();
            Order order = new Order(2, selectedDeliveryOption, amountOfBooks, currentDate ,currentDate, booksToOrder,"Nowe");
            Packet packet = Packet.withOrderInfo("CreateNewOrder","TEST", order);
            client.sendPacket(packet);

            Alert succesAlert = new Alert(Alert.AlertType.INFORMATION);
            succesAlert.setTitle("Złożono zamówienie");
            succesAlert.setHeaderText("Zamówienie zostało złożone pomyślnie");
            succesAlert.showAndWait();

            booksToOrder.clear();
            bookOrderTableView.getItems().clear();
            selectedDeliveryOption = new Delivery("");
        }
    }

}
