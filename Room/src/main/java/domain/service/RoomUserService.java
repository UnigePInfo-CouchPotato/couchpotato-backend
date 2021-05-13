package domain.service;

import domain.model.Room_User;

public interface RoomUserService {

    Room_User get(Room_User room_user);
    boolean exists(Room_User room_user);

}
