package sample;


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
import javax.imageio.ImageIO;


public class Main extends Application {

    private BorderPane layout;
    private Scene scene;
    TextField textField;
    static String text = "A";
    static BufferedImage screenFullImage;
    static ByteBuffer byteBuffer;
    Timer timer;
    static String image;
    WebsocketClientEndpoint handler = new WebsocketClientEndpoint("ws://localhost:8080/echo");

    @Override
    public void start(Stage primaryStage) throws Exception {
        layout = new BorderPane();
        scene = new Scene(layout, 280, 333);
        Button button2 = new Button();
        button2.setText("Stop");
        layout.setRight(button2);
        button2.setOnAction(e -> {
        handler.cancel();
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

    public String captureScreens() {
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

        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
        return image;
    }

    public void connection(String name) {

        try {
            handler.connect();
            Controller controller = new Controller(name, "image");
            String object = controller.toJson();
            handler.sendObject(object);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}

//    public void sendImages(long number) {
//        try{
//            timer = new Timer();
//            timer.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    captureScreens();
//                    System.out.println("In timer");
//                }
//            }, 2000, number * 1);
//        }
//        catch (Exception e){
//            System.out.println(e);
//        }
//    }
