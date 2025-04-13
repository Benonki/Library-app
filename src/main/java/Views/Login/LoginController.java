package Views.Login;

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
                try{
                    switch(role) {
                        case "Reader":
                            sceneController.switchToReaderView(new ActionEvent(loginButton,null));
                            break;
                        case "Employee":
                            sceneController.switchToEmployeeView(new ActionEvent(loginButton,null));
                            break;
                        case "Manager":
                            sceneController.switchToManagerView(new ActionEvent(loginButton,null));
                            break;
                        case "Coordinator":
                            sceneController.switchToCoordinatorView(new ActionEvent(loginButton,null));
                            break;
                        default:
                            sceneController.switchToMainView(new ActionEvent(loginButton,null));
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
