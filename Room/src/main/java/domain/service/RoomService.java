package domain.service;

import domain.model.Room;
import domain.model.Users;

import java.util.List;

public interface RoomService {

	boolean isUserInRoom(String roomId, int userId);
	String getRoomUsers(String roomId);
	List<Room> getAll();
	Room get(String roomId);
	Long count();
	boolean exists(String roomId);
	Users getRoomAdmin(int userId);
	boolean isRoomAdmin(String roomId, int userId);
	String createRoom(int userId);
	void joinRoom(String roomId, int userId);
	void deleteRoom(String roomId);
	boolean closeRoom(String roomId);
	boolean isUserIdInvalid(int userId);
	boolean isRoomClosed(String roomId);
	String getMovieWithMostVotes(String roomId);
	String getMovies(String roomId, int userId);

}
