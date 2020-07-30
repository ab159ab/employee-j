package sample;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import java.util.Timer;
import java.util.TimerTask;

public class MouseLog implements NativeMouseInputListener {

    static boolean REACHED_LIMIT = false;
    static boolean CONNECTION_CANCELED = false;
    static boolean oldTimer = false;
    static int count = Config.INITIAL_VALUE;
    static Timer timer;

    public void mouseCheck(){
        if (oldTimer){
            timer.cancel();
        }
        timer = new Timer();
        oldTimer = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
             mouseTask();
            }
        },0,1000);
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
    }

    public void nativeMousePressed(NativeMouseEvent e) {
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        if(count != 0){
            count = 0;
        }
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
    }

    public void mouseTask(){
        if (count >= WebsocketClientEndpoint.inactivityInterval && KeyLog.REACHED_LIMIT && KeyLog.CONNECTION_CANCELED){
            timer.cancel();
        }
        else if (count >= WebsocketClientEndpoint.inactivityInterval){
            count = 0;
            oldTimer = false;
            REACHED_LIMIT = true;
            if (KeyLog.REACHED_LIMIT && !KeyLog.CONNECTION_CANCELED){
                WebsocketClientEndpoint.cancel();
                CONNECTION_CANCELED = true;
                timer.cancel();
            }
        }
        else {
            count ++;
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
        MouseLog mouseLog = new MouseLog();
        GlobalScreen.addNativeMouseListener(mouseLog);
        GlobalScreen.addNativeMouseMotionListener(mouseLog);
    }
}
