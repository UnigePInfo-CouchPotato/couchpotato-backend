package domain.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.model.RoomUser;
import domain.model.Singleton;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.model.Room;
import eu.drus.jpa.unit.api.JpaUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JpaUnit.class)
@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Spy
    @PersistenceContext(unitName = "RoomPUTest")
    EntityManager em;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    @InjectMocks
    private RoomUserServiceImpl roomUserServiceImpl;

    @Test
    void testGetAll() {
        Set<Integer> keySet = initDataStore().keySet();
        assertEquals(keySet.iterator().next(), roomServiceImpl.getAll().size());
    }

    @Test
    void testIsInteger() {
        Integer integer = 5;
        String string = "Hello!";
        JSONObject jsonObject = new JSONObject();

        assertTrue(roomServiceImpl.isInteger(integer));
        assertFalse(roomServiceImpl.isInteger(string));
        assertFalse(roomServiceImpl.isInteger(jsonObject));
    }

    @Test
    void testEndJoinPeriod() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        Room room = roomServiceImpl.get(roomId);

        assertTrue(room.isUsersCanJoin());
        roomServiceImpl.endJoinPeriod(roomId);
        assertFalse(room.isUsersCanJoin());
    }

    @Test
    void testVotingPeriod() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        Room room = roomServiceImpl.get(roomId);

        assertFalse(room.isUsersCanVote());
        roomServiceImpl.startVotingPeriod(roomId);
        assertTrue(room.isUsersCanVote());
        roomServiceImpl.endVotingPeriod(roomId);
        assertFalse(room.isUsersCanVote());
    }

    @Test
    void testDeleteRoom() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        roomServiceImpl.deleteRoom(roomId);
        roomUserServiceImpl.delete(roomId);

        assertNull(roomServiceImpl.get(roomId));
        assertEquals(new ArrayList<>() ,roomUserServiceImpl.getAllFromRoomId(roomId));
    }

    @Test
    void testGetUserPreferences() {
        JSONObject userInfo = initFakeUserInfo();
        String preferences = roomServiceImpl.getUserPreferences(userInfo);
        String str = "878,18,53";

        assertEquals(str, preferences);
        assertEquals(str.length(), preferences.length());
    }

    @Test
    void testGetUserNickname() {
        JSONObject userInfo = initFakeUserInfo();
        String userNickname = roomServiceImpl.getUserNickname(userInfo);

        assertEquals("atomicnicos", userNickname);
    }

    @Test
    void testCloseRoom() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();

        assertTrue(roomServiceImpl.closeRoom(roomId));
        roomServiceImpl.closeRoom(roomId);
        assertFalse(roomServiceImpl.closeRoom(roomId));
    }

    @Test
    void testCheckTokenValidity() {
        String token = UUID.randomUUID().toString();
        Map<String, JSONObject> map = roomServiceImpl.checkTokenValidity(token);
        JSONObject info = map.get("info");
        boolean isValid = info.getBoolean("valid");
        assertFalse(isValid);
    }

    @Test
    void testMakeRequest() {
        String token = UUID.randomUUID().toString();
        String url = "https://couchpotato.eu.auth0.com/v2/userinfo";
        String response = roomServiceImpl.makeRequest(url, token);

        assertEquals("Unauthorized", response);
    }

    @Test
    void testGetUserInfo() {
        String token = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        JSONObject userInfo = roomServiceImpl.getUserInfo(token);

        assertEquals(jsonObject.toString(), userInfo.toString());
    }

    @Test
    void testKeys() {
        LinkedHashMap<Integer, Integer> index = new LinkedHashMap<>();
        index.put(878, 1);
        index.put(18, 1);
        index.put(53, 2);
        List<Integer> integerList = new ArrayList<>();
        integerList.add(878);
        integerList.add(18);
        Stream<Integer> stream = integerList.stream();

        assertEquals(stream.count(), roomServiceImpl.keys(index, 1).count());
        stream = integerList.stream();
        assertEquals(stream.collect(Collectors.toList()), roomServiceImpl.keys(index, 1).collect(Collectors.toList()));
    }

    @Test
    void testCreateID() {
        String str = UUID.randomUUID().toString().substring(24);
        String id = roomServiceImpl.createID();

        assertFalse(id.isEmpty());
        assertEquals(str.length(), id.length());
    }

    @Test
    void testIsRoomAdmin() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        String token = UUID.randomUUID().toString();

        assertFalse(roomServiceImpl.isRoomAdmin(roomId, token));
    }

    @Test
    void testExists() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        String fakeRoomId = UUID.randomUUID().toString().substring(24);

        assertTrue(roomServiceImpl.exists(roomId));
        assertFalse(roomServiceImpl.exists(fakeRoomId));
    }

    @Test
    void testCanJoinRoom() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();

        assertTrue(roomServiceImpl.canJoinRoom(roomId));
        roomServiceImpl.endJoinPeriod(roomId);
        assertFalse(roomServiceImpl.canJoinRoom(roomId));
    }

    @Test
    void testCanVote() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();

        assertFalse(roomServiceImpl.canVote(roomId));
        roomServiceImpl.endVotingPeriod(roomId);
        assertFalse(roomServiceImpl.canVote(roomId));
    }

    @Test
    void testIsUserInRoom() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        JSONObject jsonObject = new JSONObject();

        assertFalse(roomServiceImpl.isUserInRoom(roomId, jsonObject));
    }

    @Test
    void testGetRoomUsers() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();

        assertEquals("", roomServiceImpl.getRoomUsers(roomId));
        assertTrue(roomServiceImpl.getRoomUsers(roomId).isEmpty());
    }

    @Test
    void testCount() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());

        assertNotNull(rooms);
        assertEquals(rooms, roomServiceImpl.getAll());
        assertEquals(rooms.size(), roomServiceImpl.getAll().size());
        assertEquals(rooms.size(), roomServiceImpl.count());
    }

    @Test
    void testCreateRoom() {
        String roomId = roomServiceImpl.createRoom(initFakeUserInfo());
        assertTrue(roomServiceImpl.exists(roomId));
    }

    @Test
    void testJoinRoom() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        String nickname = "Admin";
        roomServiceImpl.joinRoom(roomId, initFakeUserInfo());
        roomUserServiceImpl.create(roomId, nickname);

        assertTrue(roomUserServiceImpl.exists(roomId, nickname));
    }

    @Test
    void testGetMovieWithMostVotes() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        JSONObject message = new JSONObject();
        message.put("message", String.format("No data for room %s", roomId));

        assertEquals(message.toString(), roomServiceImpl.getMovieWithMostVotes(roomId));

        Singleton singleton = Singleton.getInstance();
        Map<String, JSONObject> singletonHashMap = new HashMap<>();
        singletonHashMap.put(roomId, initFakeUserInfo());
        singleton.setHashMap(singletonHashMap);

        message.clear();
        message.put("error", "No movie");
        assertEquals(message.toString(), roomServiceImpl.getMovieWithMostVotes(roomId));
    }

    @Test
    void testGetMovies() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        String token = UUID.randomUUID().toString().substring(24);
        JSONObject message = new JSONObject();
        message.put("error", "Please set some preferences for this room");

        assertEquals(message.toString(), roomServiceImpl.getMovies(roomId, token));

        Room room1 = new Room();
        String id = UUID.randomUUID().toString().substring(24);
        room1.setRoomId(id);
        room1.setRoomAdmin("Test administrator");
        room1.setUserPreferences("878,18");
        em.persist(room1);

        message.clear();
        message.put("message", "Please set at least 3 genres");
        assertEquals(message.toString(), roomServiceImpl.getMovies(id, token));

        Room room2 = new Room();
        String newId = UUID.randomUUID().toString().substring(24);
        room2.setRoomId(newId);
        room2.setRoomAdmin("Test administrator");
        room2.setUserPreferences("878,18,54,20");
        em.persist(room2);

        JSONArray jsonArray = new JSONArray();
        assertEquals(jsonArray.toString(), roomServiceImpl.getMovies(newId, token));

        Room room3 = new Room();
        String thirdId = UUID.randomUUID().toString().substring(24);
        room3.setRoomId(thirdId);
        room3.setRoomAdmin("Test administrator");
        room3.setUserPreferences("preferences");
        em.persist(room3);

        message.clear();
        message.put("error", "Please set some preferences for this room");
        assertEquals(message.toString(), roomServiceImpl.getMovies(thirdId, token));

        Room room4 = new Room();
        String fourthId = UUID.randomUUID().toString().substring(24);
        room4.setRoomId(fourthId);
        room4.setRoomAdmin("Test administrator");
        room4.setUserPreferences("10,10,1054,35,16,10");
        em.persist(room4);

        assertEquals(jsonArray.toString(), roomServiceImpl.getMovies(fourthId, token));

        Room room5 = new Room();
        String fifthId = UUID.randomUUID().toString().substring(24);
        room5.setRoomId(fifthId);
        room5.setRoomAdmin("Test administrator");
        room5.setMovies("Movies");
        room5.setUserPreferences("10,10,1054,35,16,10");
        em.persist(room5);

        assertEquals("Movies", roomServiceImpl.getMovies(fifthId, token));
    }

    @Test
    void testGet() {
        initDataStore();
        List<Room> rooms = roomServiceImpl.getAll();
        String roomId = rooms.get(0).getRoomId();
        Room room = roomServiceImpl.get(roomId);

        assertEquals(rooms.get(0).getRoomId(), room.getRoomId());
        assertEquals(rooms.get(0).getRoomAdmin(), room.getRoomAdmin());
    }

    private List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rooms.add(getRandomRoom());
        }
        return rooms;
    }

    private void setRandomRoomUsers(String roomId, String nickname) {
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(roomId);
        roomUser.setUserNickname(nickname);
        em.persist(roomUser);

        for (int i = 0; i < 5; i++) {
            roomUserServiceImpl.create(roomId, UUID.randomUUID().toString().substring(24));
        }
    }

    private Room getRandomRoom() {
        Room r = new Room();
        r.setRoomClosed(false);
        r.setRoomAdmin("Test administrator");
        r.setRoomId(UUID.randomUUID().toString().substring(24));
        setRandomRoomUsers(r.getRoomId(), r.getRoomAdmin());
        return r;
    }

    private HashMap<Integer, List<Room>> initDataStore() {
        int size = roomServiceImpl.getAll().size();
        HashMap<Integer, List<Room>> hashMap = new HashMap<>();
        List<Room> rooms = getRooms();
        for (Room r : rooms) {
            em.persist(r);
        }
        hashMap.put(size + rooms.size(), rooms);
        return hashMap;
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
