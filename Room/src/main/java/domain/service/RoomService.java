package domain.service;

import domain.model.Room;
import domain.model.Users;

import java.util.List;

public interface RoomService {

	String getWelcomeMessage();
	boolean isUserInRoom(int roomId, int userId);
	String getRoomUsers(int roomId);
	List<Room> getAll();
	Room get(int roomId);
	Long count();
	void update(Room room);
	boolean exists(int roomId);
	Users getRoomAdmin(int userId);
	boolean isRoomAdmin(int roomId, int userId);
	int createRoom(int userId);
	void joinRoom(int roomId, int userId);
	void deleteRoom(int roomId);
	boolean closeRoom(int roomId);
	boolean isUserIdInvalid(int userId);

}