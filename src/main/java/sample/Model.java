package sample;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class Model {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  String array[];

    public Model(String[] array) {
        this.array = array;
    }

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    public String getMessage(){
        String []array1 = getArray();
        String message = array1[0];
        return  message;
    }

    public String getTime(){
        String []arr2 = getArray();
        String time = arr2[0];
        return  time;
    }

    public String toJson() {
        return GSON.toJson(this, Model.class);
    }
}
