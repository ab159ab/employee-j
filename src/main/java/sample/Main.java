package sample;


import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.glassfish.tyrus.core.cluster.BroadcastListener;

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
    Timer timer;
    static String image;
    int time = 5000;
    int time2 = 10;
    int temp = 0;
    WebsocketClientEndpoint handler = new WebsocketClientEndpoint("ws://localhost:8080/echo");

    @Override
    public void start(Stage primaryStage) throws Exception {
        layout = new BorderPane();
        scene = new Scene(layout, 280, 333);
        Button button2 = new Button();
        button2.setText("Stop");
        layout.setRight(button2);
        button2.setOnAction(e -> {

            timer.cancel();
            temp=0;
        });
        Button button = new Button();
        button.setText("Start");
        layout.setLeft(button);
        button.setOnAction(e -> {
            text = textField.getText().toString();
            System.out.println(text);
             connection(text);

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
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
            screenFullImage = robot.createScreenCapture(captureRect);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(screenFullImage, format, arrayOutputStream);
            image = Base64.getEncoder().encodeToString(arrayOutputStream.toByteArray());
            System.out.println("Screen Captured");
            System.out.println("Real Value1: "+WebsocketClientEndpoint.msg);
             int num = Integer.parseInt(WebsocketClientEndpoint.msg);
            Controller controller2 = new Controller(text, image);
            String object2 = controller2.toJson();
            handler.sendObject(object2);
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }

    public void connection(String name) {

        try {
            int valll = handler.connect();
            Controller controller = new Controller(text, "image");
            String object = controller.toJson();
            handler.sendMessage(object);
            System.out.println("Connected in Main");
//            timer.wait();
        } catch (Exception e) {
            System.out.println(e);
        }
//        String sss =  WebsocketClientEndpoint.msg;
//        int nob = Integer.parseInt(sss);
//        System.out.println("???: "+sss);
        sendImages(4000);
    }

    public void sendImages(long number) {
        try{
             timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    captureScreens();
                    temp = 1;
                    System.out.println("In timer");
                }
            }, 2000, number * 1);
        }
        catch (Exception e){
            System.out.println("MyTimer Exception: "+e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
