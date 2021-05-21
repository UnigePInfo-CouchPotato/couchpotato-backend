package domain.service;

import domain.model.Room_User;

import java.util.List;

public interface RoomUserService {

    Room_User get(Room_User room_user);
    Room_User get(String roomId, int userId);
    boolean exists(Room_User room_user);
    boolean exists(String roomId, int userId);
    void create(Room_User room_user);
    void create(String roomId, int userId);
    List<Room_User> getAll();
    void setUserGenres(String roomId, int userId, String genres);
    void setUserVotes(String roomId, int userId, String votes);
    int countRoomUsers(String roomId);

}
