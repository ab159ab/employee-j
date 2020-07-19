package sample;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.ByteBuffer;

import static sample.Main.byteBuffer;

public class Controller {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

     String name;
     String image;

    public Controller(String string1, String string2){
        this.name = string1;
        this.image = string2;
    }

    public String getString1() {
        return name;
    }

    public String getString2() {
        return image;
    }

    public String toJson() {
        return GSON.toJson(this, Controller.class);
    }
}
