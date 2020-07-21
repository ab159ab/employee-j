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
    static String text = "A";
    static BufferedImage screenFullImage;
    static ByteBuffer byteBuffer;
    Controller controller;
    static Timer timer;
    static String image;
    int time = 5000;
    int time2 = 10;
    int temp = 0;
    WebsocketClientEndpoint handler = new WebsocketClientEndpoint("ws://localhost:8080/echo");

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        layout = new BorderPane();
        scene = new Scene(layout, 280, 333);
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
            System.out.println(text);
            int con = connection(text);
            System.out.println(con);
            if (con == 1){
                sendImages();
            }


        });
        textField = new TextField();
        textField.setPromptText("User Name");
        layout.setTop(textField);


        primaryStage.setTitle("Desktop Client");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void captureScreens() {
        try {
            Robot robot = new Robot();
            String format = "png";
//            String fileName = "C://Users/Marhaba/Pictures/Saved Pictures/screenshot." + format;
//            ImageIO.write(screenFullImage, format, new File(fileName));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
            screenFullImage = robot.createScreenCapture(captureRect);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(screenFullImage, format, arrayOutputStream);
            image = Base64.getEncoder().encodeToString(arrayOutputStream.toByteArray());
            System.out.println("Screen Captured");

            Controller controller = new Controller(text, image);
            String object = controller.toJson();
            handler.sendObject(object);

        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }

    public int connection(String name) {
        try {
            handler.connect();
//            Model model = new Model(text);
//            String object = model.toJson();
            Controller controller = new Controller(text, image);
            String object = controller.toJson();
            handler.sendObject(object);
//            time2 = handler.getValue();
//            String ghgh =  String.valueOf(time2);
//            System.out.println("Time:"+ghgh);
//            System.out.println("Connected in Main");

        } catch (Exception e) {
            System.out.println(e);
        }
        return 1;
    }

    public void sendImages() {
        timer = new Timer();
        if (temp == 1) {
            timer.cancel();
        }
        else if (time2==10){
            time2 = handler.getValue();
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                    captureScreens();

                temp = 1;

                System.out.println(time2);
            }
        }, 0, time2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
