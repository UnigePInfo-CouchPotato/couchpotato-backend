package domain.service;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.model.RoomUser;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import eu.drus.jpa.unit.api.JpaUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JpaUnit.class)
@ExtendWith(MockitoExtension.class)
class RoomUserServiceImplTest {

    @Spy
    @PersistenceContext(unitName = "RoomPUTest")
    EntityManager em;

    @InjectMocks
    private RoomUserServiceImpl roomUserServiceImpl;

    @Test
    void testSetUserVotes() {
        HashMap<Integer, List<RoomUser>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<RoomUser> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(878);
        String nickname = "Test administrator";
        roomUserServiceImpl.setUserVotes(roomId, nickname, jsonArray);

        assertEquals(jsonArray.toString(), roomUserServiceImpl.get(roomId, nickname).getVotes());
    }

    @Test
    void testGetAll() {
        Set<Integer> keySet = initDataStore().keySet();
        assertEquals(keySet.iterator().next(), roomUserServiceImpl.getAll().size());
    }

    @Test
    void testExists() {
        HashMap<Integer, List<RoomUser>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<RoomUser> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();

        assertFalse(roomUserServiceImpl.exists(roomId, "atomicnicos"));
        assertTrue(roomUserServiceImpl.exists(roomId, "Test administrator"));
    }

    @Test
    void testCreate() {
        RoomUser roomUser = new RoomUser();
        String roomId = UUID.randomUUID().toString().substring(24);
        String nickname = "Test administrator";
        roomUser.setRoomId(roomId);
        roomUser.setUserNickname(nickname);
        roomUserServiceImpl.create(roomUser);

        assertNotNull(roomUserServiceImpl.get(roomId, nickname));
        assertTrue(roomUserServiceImpl.exists(roomId, nickname));
    }

    @Test
    void testGet() {
        initDataStore();
        List<RoomUser> roomUsers = roomUserServiceImpl.getAll();
        String roomId = roomUsers.get(0).getRoomId();
        RoomUser roomUser = roomUserServiceImpl.get(roomId, "{}");

        assertEquals(roomId, roomUser.getRoomId());
        assertEquals(roomUser.getUserNickname(), roomUser.getUserNickname());
    }

    private List<RoomUser> getRoomUsers() {
        List<RoomUser> roomUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            roomUsers.add(getRandomRoomUser());
        }
        return roomUsers;
    }

    private RoomUser getRandomRoomUser() {
        RoomUser r = new RoomUser();
        r.setUserNickname("Test administrator");
        r.setRoomId(UUID.randomUUID().toString().substring(24));
        return r;
    }

    private HashMap<Integer, List<RoomUser>> initDataStore() {
        int size = roomUserServiceImpl.getAll().size();
        HashMap<Integer, List<RoomUser>> hashMap = new HashMap<>();
        List<RoomUser> roomUsers = getRoomUsers();
        for (RoomUser r : roomUsers) {
            em.persist(r);
        }
        hashMap.put(size + roomUsers.size(), roomUsers);
        return hashMap;
    }
}
