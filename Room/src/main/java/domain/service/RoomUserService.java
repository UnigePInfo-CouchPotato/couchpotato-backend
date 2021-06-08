package domain.service;

import domain.model.RoomUser;
import org.json.JSONArray;

import java.util.List;

public interface RoomUserService {

    RoomUser get(String roomId, String userNickname);
    boolean exists(String roomId, String userNickname);
    void create(RoomUser roomUser);
    void create(String roomId, String userNickname);
    List<RoomUser> getAll();
    List<RoomUser> getAllFromRoomId(String roomId);
    void setUserVotes(String roomId, String userNickname, JSONArray choice);

}
