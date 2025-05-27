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

import java.util.List;

public class EmployeeLibraryResourcesViewController {

    @FXML
    private TableView<LibraryItem> libraryTable;

    @FXML
    private TableColumn<LibraryItem, String> titleColumn;

    @FXML
    private TableColumn<LibraryItem, String> authorColumn;

    @FXML
    private TableColumn<LibraryItem, Integer> quantityColumn;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTytul()));
        authorColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAutor()));
        quantityColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIlosc()).asObject());

        Client.getInstance().setLibraryCallback(items -> {
            Platform.runLater(() -> {
                if (items != null) {
                    System.out.println("Ładowanie " + items.size() + " książek");
                    libraryTable.getItems().setAll(items);
                } else {
                    System.out.println("Brak danych: libraryItems == null");
                }
            });
        });

        Packet request = new Packet("GetLibraryResources", "Request");
        request.role = UserSession.getRole();
        Client.getInstance().sendPacket(request);
    }


    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            new Views.SceneController().switchToEmployeeView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
