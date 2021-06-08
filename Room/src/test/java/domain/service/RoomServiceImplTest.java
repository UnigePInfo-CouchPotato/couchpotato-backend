package domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.model.Room;
import eu.drus.jpa.unit.api.JpaUnit;

@ExtendWith(JpaUnit.class)
@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Spy
    @PersistenceContext(unitName = "RoomPUTest")
    EntityManager em;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    @Test
    void testGetAll() {
        int size = initDataStore();
        assertEquals(size, roomServiceImpl.getAll().size());
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

    private Room getRandomRoom() {
        Room r = new Room();
        r.setRoomClosed(new Random().nextBoolean());
        r.setRoomAdmin("Test administrator");
        r.setRoomId(UUID.randomUUID().toString().substring(24));
        return r;
    }

    private int initDataStore() {
        int size = roomServiceImpl.getAll().size();
        List<Room> rooms = getRooms();
        for (Room r : rooms) {
            em.persist(r);
        }
        return size + rooms.size();
    }
}
