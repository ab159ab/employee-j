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
    static Timer timer;

    public void keyCheck(){
        if (oldTimer){
            timer.cancel();
        }
        timer = new Timer();
        oldTimer = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count <= WebsocketClientEndpoint.inactivityInterval && MouseLog.REACHED_LIMIT && MouseLog.CONNECTION_CANCELED){
                    timer.cancel();
                }
                else if (count >= WebsocketClientEndpoint.inactivityInterval){
                        count = 0;
                    oldTimer = false;
                    REACHED_LIMIT = true;
                    if (MouseLog.REACHED_LIMIT == true && MouseLog.CONNECTION_CANCELED == false){
                        CONNECTION_CANCELED = true;
                        timer.cancel();
                        WebsocketClientEndpoint.cancel();
                    }
                    System.out.println("Inactive Time Exceeded");
                }
                else {
                    count ++;
                    System.out.println("Key Counts: " + count);
                }
            }
        },0,1000);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if(count != 0){
            System.out.println("Count Reset");
            count = 0;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
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