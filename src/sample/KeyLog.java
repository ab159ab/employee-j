package sample;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class KeyLog implements NativeKeyListener {

    static boolean REACHED_LIMIT = false;
    static boolean CONNECTION_CANCELED = false;
    static boolean oldTimer = false;
    static int count = Config.INITIAL_VALUE;
    static Timer keyTimer;

    public void keyCheck(){
        if (oldTimer){
            keyTimer.cancel();
        }
        keyTimer = new Timer();
        oldTimer = true;
        keyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
             keyTask();
            }
        },0,1000);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if(count != 0){
            count = 0;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public static void keyTask(){
        if (MouseLog.CONNECTION_CANCELED){
            keyTimer.cancel();
        }
         if (count >= WebsocketClientEndpoint.inactivityInterval){
            count = 0;
            oldTimer = true;
            REACHED_LIMIT = true;
            if (MouseLog.REACHED_LIMIT){
                KeyLog.CONNECTION_CANCELED = true;
                REACHED_LIMIT = false;
                keyTimer.cancel();
                MouseLog.mouseTimer.cancel();
                WebsocketClientEndpoint.cancel();
            }
        }
        else {
            count ++;
            System.out.println("Key Counts: " + count);
        }
    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new KeyLog());
    }
}