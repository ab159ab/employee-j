package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Main extends Application {

    static BufferedImage screenFullImage;
    static String image;
    static WebsocketClientEndpoint handler = new WebsocketClientEndpoint();
    BorderPane layout;
    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));
        layout = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(layout);
        primaryStage.setTitle("Java Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event->{
            controller.dispose();
            Platform.exit();
            System.exit(0);
        });
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

    public static void connection(String name) {
        try {
            handler.connect();
            Message message = new Message(name, "image");
            String object = message.toJson();
            handler.sendObject(object);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
