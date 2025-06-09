package Views.Login;

import Classes.User.UserSession;
import Server.Client;
import Server.Packet;
import Views.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginTxtField;
    @FXML
    private TextField passwordTxtField;

    private SceneController sceneController = new SceneController();

    @FXML
    public void initialize(){
        Client client = Client.getInstance();
        client.setCallBack((success, role) ->{
            if(success){
                String username = loginTxtField.getText();
                UserSession.setUser(username, role);
                try{
                    switch(role) {
                        case "Czytelnik":
                            sceneController.switchToReaderView(new ActionEvent(loginButton,null));
                            break;
                        case "Bibliotekarz":
                            sceneController.switchToEmployeeView(new ActionEvent(loginButton,null));
                            break;
                        case "Kierownik":
                            sceneController.switchToManagerView(new ActionEvent(loginButton,null));
                            break;
                        case "Koordynator":
                            sceneController.switchToCoordinatorView(new ActionEvent(loginButton,null));
                            break;
                        default:
                            System.out.println("Unknown role: " + role);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Login Failed");
            }
        });
    }

    @FXML
    protected void onLoginClick(){
            String username = loginTxtField.getText();
            String password = passwordTxtField.getText();

            Client client = Client.getInstance();
            client.sendPacket(new Packet("Login",username + ";" + password));
    }

}
