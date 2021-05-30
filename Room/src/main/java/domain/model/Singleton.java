package domain.model;

import org.json.JSONObject;
import java.util.HashMap;

public final class Singleton {

    private static volatile Singleton instance = null;

    private HashMap<String, JSONObject> hashMap = new HashMap<>();

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public HashMap<String, JSONObject> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, JSONObject> hashMap) {
        this.hashMap = hashMap;
    }
}
