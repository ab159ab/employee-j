package sample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message {

    String name;
    String image;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Message(String name, String image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String toJson() {
        return GSON.toJson(this, Message.class);
    }
}