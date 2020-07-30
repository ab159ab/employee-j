package sample;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class KeyLog implements NativeKeyListener {

    static boolean SIGNAL = false;
    static boolean SIGNAL2 = false;
    static int count = 0;
    static int temp = 0;
    static Timer timer;
    static WebsocketClientEndpoint handler;

    public void keyCheck(){
        if (temp == 1){
            timer.cancel();
        }
        timer = new Timer();
        temp = 1;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (MouseLog.SIGNAL == true && MouseLog.SIGNAL2 == true){
                    timer.cancel();
                }
                if (count >= WebsocketClientEndpoint.num){
                    count = 0;
                    temp = 0;
                    SIGNAL = true;
                    if (MouseLog.SIGNAL == true && MouseLog.SIGNAL2 == false){
                        SIGNAL2 = true;
                        timer.cancel();
                        handler.cancel();
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