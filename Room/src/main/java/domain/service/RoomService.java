package domain.service;

import domain.model.Room;
import org.json.JSONObject;

import java.util.List;

public interface RoomService {

	boolean isUserInRoom(String roomId, String token);
	String getRoomUsers(String roomId);
	List<Room> getAll();
	Room get(String roomId);
	Long count();
	boolean exists(String roomId);
	boolean canJoinRoom(String roomId);
	boolean canVote(String roomId);
	boolean isRoomAdmin(String roomId, String token);
	String createRoom(String token);
	void joinRoom(String roomId, String token);
	void deleteRoom(String roomId);
	boolean closeRoom(String roomId);
	boolean isTokenInvalid(String token);
	String getMovieWithMostVotes(String roomId);
	String getMovies(String roomId, String token);
	JSONObject getUserInfo(String token);
	void endJoinPeriod(String roomId);
	void endVotingPeriod(String roomId);
	void startVotingPeriod(String roomId);

}
