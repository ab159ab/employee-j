package sample;


import javax.websocket.*;
import javax.websocket.ClientEndpointConfig.Configurator;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import static javafx.application.Application.launch;

public class WebsocketClientEndpoint extends Endpoint {
    Session session;
    String url;
    Main main;
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {


            @Override
            public void onMessage(String message) {

                System.out.println("Received: " + message);
                if (!message.equals("404")){
                    main = new Main();
                    String image = main.captureScreens();
                    String name =  Main.text;
                    Controller controller = new Controller(name,image);
                    String object = controller.toJson();
                    sendObject(object);
                }

            }
        });
    }
    public void connect() throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new Configurator())
                .build();
        container.connectToServer(this, config, new URI(url));
    }
    public WebsocketClientEndpoint(String url) {
        super();
        this.url = url;
    }
    public void sendObject(String object){
        session.getAsyncRemote().sendObject(object);
        System.out.println("Object Sent");
    }
    public void cancel(){
        try{
            session.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}