package domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void testRoom() {
        Room room = new Room();

        assertNull(room.getRoomId());
        assertNull(room.getRoomAdmin());
        assertFalse(room.isRoomClosed());
        assertFalse(room.isUsersCanVote());
        assertTrue(room.isUsersCanJoin());
        assertEquals("", room.getMovies());
        assertEquals("", room.getUserPreferences());
        assertEquals(0, room.getNumberOfMovies());
    }
}
