package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public javafx.scene.control.Button btnLogin;
    public Button btnStop;
    public TextField txtName;
    public BorderPane borderPane;
    public Label lablStatus;
    static Main main;
    static String name;
    static Thread statusThread;
    static boolean isThreadRuninnig = false;
    boolean isRunning = true;
    boolean isClolored = true;
    private final StringProperty statusInput = new SimpleStringProperty("");
    public StringProperty statusInputProperty() {
        return statusInput;
    }
    public void setStatusInput(String statusInput) {
        this.statusInput.set(statusInput);
    }

    @FXML
    public void loginAction() {
        btnLogin.setDisable(true);
        main = new Main();
        name = txtName.getText();
        Main.connection(name);
        btnStop.setDisable(false);
        KeyLog.CONNECTION_CANCELED = false;
        MouseLog.CONNECTION_CANCELED = false;
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
        if (!isThreadRuninnig){
            statusThread = new Thread(this::ConnectionStatus);
            statusThread.setName("Status Thread Is Running");
            statusThread.start();
        }
    }

    @FXML
    public void stopAction(){
        WebsocketClientEndpoint.cancel();
        btnLogin.setDisable(false);
        btnStop.setDisable(true);
        if (WebsocketClientEndpoint.CONNECTION_STATUS.equals(Config.LOGIN)){
            MouseLog.mouseTimer.cancel();
            KeyLog.keyTimer.cancel();
        }
    }

    public void ConnectionStatus() {
       while (isRunning)
       {
           isThreadRuninnig = true;
           if (WebsocketClientEndpoint.CONNECTION_STATUS.equals(Config.EMPTY)) {
               Platform.runLater(() -> {
                   setStatusInput(Config.DISCONNECTED);
               });
           } else if (WebsocketClientEndpoint.CONNECTION_STATUS.equals(Config.LOGOUT)) {
               Platform.runLater(() -> {
                   setStatusInput(Config.LOGOUT);
                   btnStop.setDisable(true);
                   btnLogin.setDisable(false);
                   if (isClolored){
                       isClolored = false;
                       borderPane.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(0), Insets.EMPTY)));
                   }
                   else {
                       isClolored = true;
                       borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
                   }
               });

           } else if (WebsocketClientEndpoint.CONNECTION_STATUS.equals(Config.LOGIN)) {
               Platform.runLater(() -> {
                   setStatusInput(Config.LOGIN);
               });
           }
           try {
               Thread.sleep(500);
           } catch (InterruptedException iex) {
           }
       }
    }

    public void dispose(){
        isRunning = false;
    }

    @Override
    public  void initialize(URL url, ResourceBundle resourceBundle) {

        btnStop.setDisable(true);
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
        lablStatus.textProperty().bindBidirectional(statusInputProperty());
        btnStop.setDisable(false);

    }
}
