package domain.model;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public final class Singleton {

    private static Singleton instance = null;

    private Map<String, JSONObject> hashMap = new HashMap<>();

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null)
            instance = new Singleton();

        return instance;
    }

    public Map<String, JSONObject> getHashMap() {
        return hashMap;
    }

    public void setHashMap(Map<String, JSONObject> newHashMap) {
        this.hashMap = newHashMap;
    }
}
