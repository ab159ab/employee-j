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
    static Timer mouseTimer;

    public void mouseCheck(){
        if (oldTimer){
            mouseTimer.cancel();
        }
        mouseTimer = new Timer();
        oldTimer = true;
        mouseTimer.scheduleAtFixedRate(new TimerTask() {
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

    public static void mouseTask(){
        if (KeyLog.CONNECTION_CANCELED){
            mouseTimer.cancel();
        }
         if (count >= WebsocketClientEndpoint.inactivityInterval){
            count = 0;
            oldTimer = true;
            REACHED_LIMIT = true;
            if (KeyLog.REACHED_LIMIT ){
                MouseLog.CONNECTION_CANCELED = true;
                REACHED_LIMIT = false;
                mouseTimer.cancel();
                KeyLog.keyTimer.cancel();
                WebsocketClientEndpoint.cancel();
            }
        }
        else {
            count ++;
            System.out.println("Mouse Counts: " + count);
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
