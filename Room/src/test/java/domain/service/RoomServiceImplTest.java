package domain.service;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.model.RoomUser;
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
        assertTrue(roomServiceImpl.isInteger(integer));
        assertFalse(roomServiceImpl.isInteger(string));
    }

    @Test
    void testEndJoinPeriod() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        String token = "token";

        Room room = roomServiceImpl.get(roomId);
        assertTrue(room.isUsersCanJoin());
        roomServiceImpl.endJoinPeriod(roomId, token);
        assertFalse(room.isUsersCanJoin());
    }

    @Test
    void testEndVotingPeriod() {
        HashMap<Integer, List<Room>> hashMap = initDataStore();
        Set<Integer> keySet = hashMap.keySet();
        List<Room> rooms = hashMap.get(keySet.iterator().next());
        String roomId = rooms.get(0).getRoomId();
        String token = "token";

        Room room = roomServiceImpl.get(roomId);
        assertTrue(room.isUsersCanVote());
        roomServiceImpl.endVotingPeriod(roomId, token);
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
        for (int i = 0; i < 15; i++) {
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
}
