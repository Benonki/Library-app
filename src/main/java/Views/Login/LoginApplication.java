package Views.Login;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Library App");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(e ->{
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}
