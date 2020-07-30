package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public javafx.scene.control.Button btnLogin;
    public Button btnStop;
    public TextField txtName;
    public Label lablStatus;
    static Main main;
    static String name;
    public BorderPane borderPane;

    @FXML
    public void loginAction(){
        btnLogin.setDisable(true);
        main = new Main();
        name = txtName.getText();
        Main.connection(name);
        lablStatus.setText("Connected");
        btnStop.setDisable(false);
    }

    @FXML
    public void stopAction(){
        btnLogin.setDisable(false);
        WebsocketClientEndpoint.cancel();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStop.setDisable(true);

    }
}
