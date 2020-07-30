package sample;

import com.google.gson.Gson;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import javax.websocket.*;
import javax.websocket.ClientEndpointConfig.Configurator;
import java.io.IOException;
import java.net.*;
import static javafx.application.Application.launch;

public class WebsocketClientEndpoint extends Endpoint {

    static Session session;
    static int inactivityInterval = Config.INITIAL_VALUE;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String jsonMessage) {
                Gson gson = new Gson();
                IncommingMessage message = gson.fromJson(jsonMessage,IncommingMessage.class);
                String imageStatus = message.getimageStatus();
                String internalTime = message.getInternalTime();
                System.out.println("INTERNAL TIME:  " + internalTime);
                int newInactivityInterval = Config.INITIAL_VALUE;
                newInactivityInterval = Integer.parseInt(internalTime);
                if (imageStatus.equals(Config.IMAGE_STATUS)){
                    Main main = new Main();
                    String image = main.captureScreens();
                    String name =  Controller.name;
                    Message message1 = new Message(name,image);
                    String object = message1.toJson();
                    sendObject(object);
                }
                if (newInactivityInterval != inactivityInterval ){
                    inactivityInterval = newInactivityInterval;
                    checkActivity();
                }
            }
        });
    }

    public void connect() throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                .configurator(new Configurator())
                .build();
        container.connectToServer(this, config, new URI(Config.CONNECTION_STRING));
    }

    public WebsocketClientEndpoint() {
        super();
    }

    public void checkActivity(){
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        KeyLog keyLog = new KeyLog();
        GlobalScreen.addNativeKeyListener(keyLog);
        keyLog.keyCheck();
        MouseLog mouseLog = new MouseLog();
        GlobalScreen.addNativeMouseListener(mouseLog);
        GlobalScreen.addNativeMouseMotionListener(mouseLog);
        mouseLog.mouseCheck();
    }

    public static void sendObject(String object) {
        session.getAsyncRemote().sendObject(object);
    }

    public static void cancel(){
        try{
            System.out.println("Disconnected");
            session.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}