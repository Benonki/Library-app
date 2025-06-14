package Views.Reader;

import Classes.User.UserSession;
import Classes.Reader.WypozyczenieDAO;
import Classes.Reader.RezerwacjaDAO;
import Classes.Reader.BookDAO;
import Classes.Reader.Book;
import Classes.User.UserDAO;
import Views.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReaderViewController {

    private final SceneController sceneController = new SceneController();
    private int currentUserId;

    @FXML private Label welcomeLabel;
    @FXML private Button backButton;
    @FXML private Button borrowButton;
    @FXML private Button reserveButton;
    @FXML private Button reservedBooksButton;
    @FXML private Button logoutButton;

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;

    @FXML private TableView<Book> borrowedTable;
    @FXML private TableColumn<Book, Integer> borrowedIdColumn;
    @FXML private TableColumn<Book, String> borrowedTitleColumn;
    @FXML private TableColumn<Book, String> borrowedAuthorColumn;

    @FXML
    public void initialize() {
        String username = UserSession.getUsername();
        welcomeLabel.setText("Witaj " + username + "!");

        // Pobierz userId z bazy danych na podstawie username
        currentUserId = UserDAO.getUserIdByUsername(username);

        setupTables();
        loadBooks();
        loadBorrowedBooks();
    }

    private void setupTables() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        borrowedIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        borrowedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowedAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    }

    private void loadBooks() {
        List<Book> books = BookDAO.getAllBooks();
        ObservableList<Book> bookList = FXCollections.observableArrayList(books);
        bookTable.setItems(bookList);
    }

    private void loadBorrowedBooks() {
        List<Book> borrowed = WypozyczenieDAO.getWypozyczoneKsiazki(currentUserId);
        ObservableList<Book> borrowedList = FXCollections.observableArrayList(borrowed);
        borrowedTable.setItems(borrowedList);
    }

    @FXML
    private void handleBorrowClick() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Błąd", "Nie wybrano książki do wypożyczenia.");
            return;
        }

        boolean result = WypozyczenieDAO.wypozyczKsiazke(currentUserId, selected.getId());
        if (result) {
            showAlert("Sukces", "Książka została wypożyczona.");
            loadBooks();
            loadBorrowedBooks();
        } else {
            showAlert("Błąd", "Brak dostępnych egzemplarzy.");
        }
    }

    @FXML
    private void handleReserveClick() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Błąd", "Nie wybrano książki do rezerwacji.");
            return;
        }

        boolean result = RezerwacjaDAO.zarezerwujKsiazke(currentUserId, selected.getId());
        if (result) {
            showAlert("Sukces", "Książka została zarezerwowana.");
            loadBooks();
        } else {
            showAlert("Błąd", "Nie można zarezerwować książki – egzemplarze są dostępne lub wystąpił błąd.");
        }
    }

    @FXML
    private void openReservedBooksWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Reader/ReservedBooksView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Zarezerwowane książki");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Błąd", "Nie udało się otworzyć okna zarezerwowanych książek.");
        }
    }

    @FXML
    public void switchToLoginView(ActionEvent event) {
        UserSession.clear();
        try {
            sceneController.switchToLoginView(event);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Błąd", "Nie udało się przełączyć widoku logowania.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
