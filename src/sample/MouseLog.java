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
                if (count >= WebsocketClientEndpoint.inactivityInterval && KeyLog.REACHED_LIMIT && KeyLog.CONNECTION_CANCELED){
                    timer.cancel();
                }
                else if (count >= WebsocketClientEndpoint.inactivityInterval){
                    count = 0;
                    oldTimer = false;
                    REACHED_LIMIT = true;
                    if (KeyLog.REACHED_LIMIT == true && KeyLog.CONNECTION_CANCELED == false){
                        WebsocketClientEndpoint.cancel();
                        CONNECTION_CANCELED = true;
                        timer.cancel();
                    }
                    System.out.println("Inactive Time Exceeded");
                }
                else {
                    count ++;
                    System.out.println("Mouse Counts: " + count);
                }
            }
        },0,1000);
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
        System.out.println("Mouse Clicked: " + e.getClickCount());
    }

    public void nativeMousePressed(NativeMouseEvent e) {
        System.out.println("Mouse Pressed: " + e.getButton());
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
        System.out.println("Mouse Released: " + e.getButton());
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
        if(count != 0){
            System.out.println("Count Reset");
            count = 0;
        }
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
        System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
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
