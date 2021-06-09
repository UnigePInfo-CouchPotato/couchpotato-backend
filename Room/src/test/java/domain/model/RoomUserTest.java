package domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomUserTest {

    @Test
    void testRoomUser() {
        RoomUser roomUser = new RoomUser();

        assertNull(roomUser.getRoomId());
        assertNull(roomUser.getUserNickname());
        assertEquals("[]", roomUser.getVotes());
    }
}
