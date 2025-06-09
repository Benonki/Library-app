package Views.Reader;

import Classes.Reader.Book;
import Classes.Reader.RezerwacjaDAO;
import Classes.Reader.BookDAO;
import Classes.User.UserSession;
import Classes.User.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ReservedBooksViewController {

    @FXML private TableView<Book> reservedTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, LocalDate> reservationDateColumn;
    @FXML private TableColumn<Book, LocalDate> pickupDateColumn;

    @FXML private Button cancelReservationButton;
    @FXML private Button closeButton;

    private int currentUserId;

    @FXML
    public void initialize() {
        currentUserId = UserDAO.getUserIdByUsername(UserSession.getUsername());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        pickupDateColumn.setCellValueFactory(new PropertyValueFactory<>("pickupDate"));

        loadReservedBooks();
        sprawdzCzyKsiazkiSaDostepne(reservedTable.getItems());
    }

    private void loadReservedBooks() {
        List<Book> reservedBooks = RezerwacjaDAO.getZarezerwowaneKsiazki(currentUserId);
        reservedTable.setItems(FXCollections.observableArrayList(reservedBooks));
    }

    private void sprawdzCzyKsiazkiSaDostepne(List<Book> reservedBooks) {
        for (Book book : reservedBooks) {
            boolean dostepna = BookDAO.czyKsiazkaDostepna(book.getId());
            if (dostepna) {
                showAlert("Powiadomienie", "Zarezerwowana książka \"" + book.getTitle() + "\" jest już dostępna do wypożyczenia.");
            }
        }
    }

    @FXML
    private void handleCancelReservation() {
        Book selected = reservedTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Błąd", "Nie wybrano książki do anulowania.");
            return;
        }

        boolean result = RezerwacjaDAO.anulujRezerwacje(currentUserId, selected.getId());
        if (result) {
            showAlert("Sukces", "Rezerwacja została anulowana.");
            loadReservedBooks();
        } else {
            showAlert("Błąd", "Nie udało się anulować rezerwacji.");
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
