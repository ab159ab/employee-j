package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    static String name;
    public javafx.scene.control.Button btnLogin;
    public Button btnStop;
    public TextField txtName;
    public Label lablStatus;
    Main main;

    @FXML
    public void loginAction(){
        btnLogin.setDisable(true);
        main = new Main();
        name = txtName.getText();
        main.connection(name);
    }

    @FXML
    public void stopAction(){
        btnLogin.setDisable(false);
        WebsocketClientEndpoint.cancel();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
