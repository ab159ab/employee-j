package sample;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import java.util.Timer;
import java.util.TimerTask;

public class MouseLog implements NativeMouseInputListener {

    static int count = 0;
    static int temp = 0;
    static Timer timer;
    static boolean SIGNAL = false;
    static boolean SIGNAL2 = false;
    static WebsocketClientEndpoint handler;

    public void mouseCheck(){
        if (temp == 1){
            timer.cancel();
        }
        timer = new Timer();
        temp = 1;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (KeyLog.SIGNAL == true && KeyLog.SIGNAL2 == true){
                    timer.cancel();
                }
                if (count >= WebsocketClientEndpoint.num){
                    count = 0;
                    temp = 0;
                    SIGNAL = true;
                    if (KeyLog.SIGNAL == true && KeyLog.SIGNAL2 == false){
                    handler.cancel();
                    SIGNAL2 = true;
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
