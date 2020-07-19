package sample;


import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;



public class Main extends Application {

    private BorderPane layout;
    private Scene scene;
    TextField textField;
    static String text;
    static BufferedImage screenFullImage;
    static ByteBuffer byteBuffer;
    Controller controller;
    static Timer timer ;
    static String image;
    int time = 5000;

    @Override
    public void start(Stage primaryStage) throws Exception{
        layout = new BorderPane();
        scene = new Scene(layout,280,333);
        Button button2 = new Button();
        button2.setText("Stop");
        layout.setRight(button2);
        button2.setOnAction(e -> {
            timer.cancel();
        });
        Button button = new Button();
        button.setText("Start");
        layout.setLeft(button);
        button.setOnAction(e -> {
             text = textField.getText().toString();
            timer = new Timer();
            if (temp == 1) {
                timer.cancel();
            }
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    captureScreens();
                    connection();
                }
            }, 0, time);
        });
        textField = new TextField();
        textField.setPromptText("User Name");
        layout.setTop(textField);


        primaryStage.setTitle("Desktop Client");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private static void captureScreens() {
        try {
            Robot robot = new Robot();
            String format = "png";
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
             screenFullImage = robot.createScreenCapture(captureRect);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(screenFullImage, format, arrayOutputStream);
            image =  Base64.getEncoder().encodeToString(arrayOutputStream.toByteArray());
            System.out.println("Screen Captured");
        }
        catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }

    public void connection() {
        try {
            Controller controller = new Controller(text,image);
            String object = controller.toJson();
            WebsocketClientEndpoint handler = new WebsocketClientEndpoint("ws://localhost:8080/echo");
            handler.connect();
            handler.sendObject(object);
            time2 = handler.getValue();

        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
