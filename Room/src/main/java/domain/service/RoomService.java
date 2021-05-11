package domain.service;

import domain.model.Room;

import java.util.List;

public interface RoomService {

	String getHelloJersey();
	List<Room> getAll();
	Room get(int roomId);
	Long count();
	void update(Room room);
	boolean exists(int roomId);

}