package Views.Employee;

import Classes.Employee.Util.NewBookData;
import Server.Client;
import Server.Packet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.ZoneId;
import java.util.Date;

public class AddNewBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorFirstNameField;
    @FXML private TextField authorLastNameField;
    @FXML private TextField isbnField;
    @FXML private DatePicker releaseDatePicker;
    @FXML private TextField publisherField;
    @FXML private TextField coverTypeField;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private TextField locationField;

    @FXML
    public void initialize() {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1));
    }

    @FXML
    public void handleAddBook(ActionEvent event) {
        try {
            String tytul = titleField.getText();
            String imie = authorFirstNameField.getText();
            String nazwisko = authorLastNameField.getText();
            String isbn = isbnField.getText();
            Date data = Date.from(releaseDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            String wydawnictwo = publisherField.getText();
            int typOkladki = Integer.parseInt(coverTypeField.getText());
            int ilosc = quantitySpinner.getValue();
            String lokalizacja = locationField.getText();

            if (lokalizacja == null || lokalizacja.trim().isEmpty()) {
                lokalizacja = "Brak";
            }

            NewBookData book = new NewBookData(tytul, imie, nazwisko, isbn, data, wydawnictwo, typOkladki, ilosc);
            book.setLokalizacja(lokalizacja);

            Packet packet = new Packet("AddNewBook", "Dodaj ksiazke");
            packet.data = book;
            Client.getInstance().sendPacket(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            new Views.SceneController().switchToLibraryResourcesView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
