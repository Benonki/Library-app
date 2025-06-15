package Views.Employee;

import Classes.Employee.Util.NewBookData;
import Server.Client;
import Server.Packet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Classes.Employee.Util.LibraryItem;

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
    @FXML private TextField locationField;

    private boolean isEditMode = false;
    private int editingEgzemplarzId = -1;
    private static LibraryItem itemToEdit = null;

    @FXML
    public void initialize() {
        if (itemToEdit != null) {
            fillFormFromItem(itemToEdit);
            isEditMode = true;
            editingEgzemplarzId = itemToEdit.getEgzemplarzId();
        }
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
            String typOkladki = coverTypeField.getText();
            String lokalizacja = locationField.getText();

            if (lokalizacja == null || lokalizacja.trim().isEmpty()) {
                lokalizacja = "Brak";
            }

            NewBookData book = new NewBookData(tytul, imie, nazwisko, isbn, data, wydawnictwo, typOkladki);
            book.setLokalizacja(lokalizacja);

            Packet packet;

            if (isEditMode) {
                book.setEgzemplarzId(editingEgzemplarzId);
                if (itemToEdit != null) {
                    book.setStatus(itemToEdit.getStatus());
                } else {
                    System.err.println("Błąd: itemToEdit jest null w trybie edycji!");
                }
                packet = new Packet("EditBook", "Edytuj ksiazke");
            } else {
                packet = new Packet("AddNewBook", "Dodaj ksiazke");
            }

            packet.data = book;
            Client.getInstance().sendPacket(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            isEditMode = false;
            itemToEdit = null;
            new Views.SceneController().switchToLibraryResourcesView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillFormFromItem(LibraryItem item) {
        titleField.setText(item.getTytul());

        String[] autor = item.getAutor().split(" ", 2);
        authorFirstNameField.setText(autor[0]);
        authorLastNameField.setText(autor.length > 1 ? autor[1] : "");

        locationField.setText(item.getLokalizacja());
        publisherField.setText(item.getWydawnictwo());
        coverTypeField.setText(item.getTypOkladki());
        isbnField.setText(item.getIsbn());

        if (item.getDataWydania() != null) {
            releaseDatePicker.setValue(((java.sql.Date) item.getDataWydania()).toLocalDate());
        }
        isEditMode = true;
        editingEgzemplarzId = item.getEgzemplarzId();
    }

    public static void setItemToEdit(LibraryItem item) {
        itemToEdit = item;
    }
}
