package Views.Reader;

import Classes.User.UserSession;
import Views.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ReaderViewController {


    @FXML
    public void initialize() {
        String username = UserSession.getUsername();
        welcomeLabel.setText("Witaj " + username + "!");
    }

    @FXML
        try {
            UserSession.clear();
            sceneController.switchToLoginView(event);
            e.printStackTrace();
        }
    }

}
