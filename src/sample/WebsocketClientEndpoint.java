package sample;


import javax.websocket.*;
import javax.websocket.ClientEndpointConfig.Configurator;
import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;


public class WebsocketClientEndpoint extends Endpoint {

    Session session;
    String url;
    String string;


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                System.out.println("Received: " + message);
                string = message;
            }
        });
    }

    public void connect() throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new Configurator())
                .build();

        container.connectToServer(this, config, new URI(url));
        System.out.println("Connected");
    }

    public WebsocketClientEndpoint(String url) {
        super();
        this.url = url;
    }
    public void sendObject(Object object){
        session.getAsyncRemote().sendObject(object);
    }

    public int getValue(){
        return Integer.parseInt(string);
    }
}