package domain.model;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SingletonTest {

    @Test
    void testSingleton() {
        Singleton singleton = Singleton.getInstance();
        Singleton secondSingleton = Singleton.getInstance();

        assertEquals(singleton, secondSingleton);
    }

    @Test
    void testSingletonHashMap() {
        String roomId = UUID.randomUUID().toString().substring(24);
        Singleton singleton = Singleton.getInstance();
        Singleton secondSingleton = Singleton.getInstance();

        Map<String, JSONObject> singletonHashMap = new HashMap<>();
        singletonHashMap.put(roomId, initFakeUserInfo());
        singleton.setHashMap(singletonHashMap);

        assertEquals(singletonHashMap, singleton.getHashMap());
        assertEquals(singletonHashMap, secondSingleton.getHashMap());
    }

    private JSONObject initFakeUserInfo() {
        JSONObject metadata = new JSONObject();
        List<Integer> preferences = new ArrayList<>();
        preferences.add(878);
        preferences.add(18);
        preferences.add(53);
        metadata.put("preferences", preferences);
        JSONObject userInfo = new JSONObject();
        userInfo.put("email", "atomicnicos@gmail.com");
        userInfo.put("email_verified", true);
        userInfo.put("name", "atomicnicos@gmail.com");
        userInfo.put("nickname", "atomicnicos");
        userInfo.put("picture", "https://s.gravatar.com/avatar/7968ff23c5747b046f8ba74362db9b1e?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fat.png");
        userInfo.put("sub", "atomicnicos@gmail.com");
        userInfo.put("updated_at", "auth0|60ba51037634b50069305631");
        userInfo.put("https://pinfo2.unige.ch/metadata", metadata);
        return userInfo;
    }
}
