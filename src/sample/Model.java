package sample;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class Model {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    String name;

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toJson() {
        return GSON.toJson(this, Model.class);
    }
}
