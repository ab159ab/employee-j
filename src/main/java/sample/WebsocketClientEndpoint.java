package sample;


import javax.websocket.*;
import javax.websocket.ClientEndpointConfig.Configurator;
import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import static javafx.application.Application.launch;


public class WebsocketClientEndpoint extends Endpoint {

    Session session;
    String url;
    public static String msg;


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                System.out.println("Received: " + message);
               WebsocketClientEndpoint.msg = message;
//               setValue(message);
            }
        });
    }

    public void connect() throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new Configurator())
                .build();

        container.connectToServer(this, config, new URI(url));
        System.out.println("Connected In WS");
    }

    public WebsocketClientEndpoint(String url) {
        super();
        this.url = url;
    }

    public void sendMessage(Object object) {
        session.getAsyncRemote().sendObject(object);
    }

    public void sendObject(Object object){
        session.getAsyncRemote().sendObject(object);
    }

    public int getValue(){
        System.out.println("GotValue: " + msg);
//        numberedMessage = Integer.parseInt(string);
        return 2000;
    }

    public void setValue(String s){
//        WebsocketClientEndpoint.msg =
        this.msg = s;
        System.out.println(s);
//        return s;
    }
    public static void main(String[] args) {
        launch(args);
    }
}