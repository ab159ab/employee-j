package sample;


import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.websocket.*;
import javax.websocket.ClientEndpointConfig.Configurator;
import java.io.IOException;
import java.net.*;
import static javafx.application.Application.launch;

public class WebsocketClientEndpoint extends Endpoint {
    static Session session;
    static int num = 10;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String message) {
                if (!message.equals("404")){
                    Main main = new Main();
                    String image = main.captureScreens();
                    String name =  Main.text;
                    Message message1 = new Message(name,image);
                    String object = message1.toJson();
                    sendObject(object);
                    num = getInternalTime(message);
                }
            }
        });
    }

    public void connect() throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new Configurator())
                .build();
        container.connectToServer(this, config, new URI("ws://localhost:8080/echo"));
        checkActivity();
    }

    public WebsocketClientEndpoint() {
        super();
    }

    public void checkActivity(){
        KeyLog keyLog = new KeyLog();
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(keyLog);
        MouseLog log2 = new MouseLog();
        GlobalScreen.addNativeMouseListener(log2);
        GlobalScreen.addNativeMouseMotionListener(log2);
        MouseLog.check();
    }

    public static void sendObject(String object) {
        session.getAsyncRemote().sendObject(object);
    }

    public int getInternalTime(String string){
        int iTime = Integer.parseInt(string);
        return iTime;
    }

    public static void cancel(){
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