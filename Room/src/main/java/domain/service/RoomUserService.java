package domain.service;

import domain.model.Room_User;

import java.util.List;

public interface RoomUserService {

    Room_User get(Room_User room_user);
    Room_User get(int roomId, int userId);
    boolean exists(Room_User room_user);
    boolean exists(int roomId, int userId);
    void create(Room_User room_user);
    void create(int roomId, int userId);
    List<Room_User> getAll();
    void setUserGenres(int roomId, int userId, String genres);
}
