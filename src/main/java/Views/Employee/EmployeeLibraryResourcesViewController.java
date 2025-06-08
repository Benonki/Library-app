package Views.Employee;

import Classes.Employee.Util.LibraryItem;
import Classes.User.UserSession;
import Server.Client;
import Server.Packet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

public class EmployeeLibraryResourcesViewController {

    @FXML
    private TableView<LibraryItem> libraryTable;

    @FXML private TableColumn<LibraryItem, Integer> egzemplarzIdColumn;
    @FXML private TableColumn<LibraryItem, String> titleColumn;
    @FXML private TableColumn<LibraryItem, String> authorColumn;
    @FXML private TableColumn<LibraryItem, String> statusColumn;
    @FXML private TableColumn<LibraryItem, String> releaseDateColumn;
    @FXML private TableColumn<LibraryItem, String> locationColumn;
    @FXML private TableColumn<LibraryItem, String> publisherColumn;
    @FXML private TableColumn<LibraryItem, String> coverTypeColumn;
    @FXML private TableColumn<LibraryItem, String> isbnColumn;

    @FXML
    public void initialize() {
        egzemplarzIdColumn.setCellValueFactory(new PropertyValueFactory<>("egzemplarzId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        releaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataWydania"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("lokalizacja"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("wydawnictwo"));
        coverTypeColumn.setCellValueFactory(new PropertyValueFactory<>("typOkladki"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        Client.getInstance().setLibraryCallback(items -> {
            Platform.runLater(() -> {
                if (items != null) {
                    System.out.println("Ładowanie " + items.size() + " egzemplarzy");
                    libraryTable.getItems().setAll(items);
                } else {
                    System.out.println("Brak danych: libraryItems == null");
                }
            });
        });

        refreshLibraryResources();
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            new Views.SceneController().switchToEmployeeView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteBookCopy() {
        LibraryItem selected = libraryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Packet p = new Packet("DeleteBookCopy", "Request");
            p.data = selected.getEgzemplarzId();
            Client.getInstance().sendPacket(p);
            refreshLibraryResources();
        }
    }

    private void refreshLibraryResources() {
        Packet request = new Packet("GetLibraryResources", "Request");
        request.role = UserSession.getRole();
        Client.getInstance().sendPacket(request);
    }

    @FXML
    public void handleAddNewBook(ActionEvent event) {
        try {
            new Views.SceneController().switchScene(event, "/Views/Employee/AddNewBookView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEditBook(ActionEvent event) {
        LibraryItem selectedItem = libraryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            AddNewBookController.setItemToEdit(selectedItem);
            try {
                new Views.SceneController().switchScene(event, "/Views/Employee/AddNewBookView.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
