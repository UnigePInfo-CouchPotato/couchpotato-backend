package domain.service;

import domain.model.Room;
import domain.model.Room_User;
import domain.model.Users;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@ApplicationScoped
@Log
public class RoomServiceImpl implements RoomService {

    @PersistenceContext(unitName = "RoomPU")
    private EntityManager em;

    @Inject
	private RoomUserService roomUserService;

    public RoomServiceImpl(){

	}

	public RoomServiceImpl(EntityManager em){
    	this();
    	this.em = em;
	}

	private String createID() { return UUID.randomUUID().toString().substring(24); }

	private String makeRequest(String url, String mediaType) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(url);
		Response response = webTarget.request(mediaType).get();

		if (response.getStatus() != 200) {
			return "Failed : HTTP error code : " + response.getStatus();
		}

		return response.readEntity(String.class);
	}

	@Override
	public String getWelcomeMessage() {
    	log.info("Get welcome message from user management");
    	final String url = "http://usermanagement-service:28080/users";
    	return makeRequest(url, MediaType.TEXT_PLAIN);
	}

	@Override
	public Users getRoomAdmin(int userId) {
		log.info("Get information on room administrator from user management");
		final String url = "http://usermanagement-service:28080/users/" + userId;
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(url);
		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
		return response.readEntity(Users.class);
	}

	@Override
	public boolean isRoomAdmin(String roomId, int userId) {
    	log.info("Check if user is the administrator of the room");
		Room room = get(roomId);
		return room.getRoomAdminId() == userId;
	}

	@Override
	@Transactional
	public String createRoom(int userId) {
    	log.info("Create a room, set the administrator and add the user");
		Room room = new Room();
		room.setRoomAdminId(userId);
		room.setRoomId(createID());
		em.persist(room);
		em.flush();
		String createdRoomId = room.getRoomId();
		roomUserService.create(createdRoomId, userId);
		return createdRoomId;
	}

	@Override
	public void joinRoom(String roomId, int userId) {
    	log.info("Add user to room");
		roomUserService.create(roomId, userId);
	}

	@Override
	@Transactional
	public void deleteRoom(String roomId) {
    	log.info("Delete the room");
    	//Delete room
    	Room room = get(roomId);
    	if (room != null)
    		em.remove(room);

    	//Delete all room_user records associated
		List<Room_User> room_users = roomUserService.getAll();
    	for (Room_User room_user : room_users) {
    		if (Objects.equals(room_user.getRoomId(), roomId))
    			em.remove(room_user);
		}
	}

	@Override
	@Transactional
	public boolean closeRoom(String roomId) {
    	log.info("Close the room");
    	//If room is already closed, return false else return true
    	Room room = get(roomId);
    	if (room.isRoomClosed())
    		return false;
    	else
    		room.setRoomClosed(true);

    	return true;
	}

	@Override
	public boolean isUserIdInvalid(int userId) {
    	log.info("Check if the user id is valid");
		final String url = "http://usermanagement-service:28080/users/" + userId + "/exists";
		String response = makeRequest(url, MediaType.APPLICATION_JSON);
		return !Boolean.parseBoolean(response);
	}

	@Override
	public boolean isRoomClosed(String roomId) {
		log.info("Check if room is closed");
		Room room = get(roomId);
		return room.isRoomClosed();
	}

	@Override
	public int getMovieWithMostVotes(String roomId) {
    	log.info("Get the index of the movie with the most votes");
		List<Room_User> room_users = roomUserService.getAll();
    	final int numberOfUsers = roomUserService.countRoomUsers(roomId);
    	int counter = 0;

		int[][] scores = new int[numberOfUsers][];
		for (Room_User room_user : room_users) {
			String userVote = room_user.getVotes();
			int[] array = Arrays.stream(userVote.substring(1, userVote.length()-1).split(", ")).mapToInt(Integer::parseInt).toArray();
			scores[counter] = array;
			counter++;
		}

		int MAX_MOVIES = 5;
		Integer[] index = new Integer[MAX_MOVIES];
		for (int[] score : scores) {
			for (int j = 0; j < MAX_MOVIES; j++) {
				index[j] += score[j];
			}
		}
		final int max = Collections.max(Arrays.asList(index));

		return Arrays.asList(index).indexOf(max);
	}

	@Override
	public boolean isUserInRoom(String roomId, int userId) {
		log.info("Check if user is already in the room");
		return roomUserService.exists(roomId, userId);
	}

	@Override
	public String getRoomUsers(String roomId) {
		log.info("Get all users in a room");
		ArrayList<Integer> validUsersIds = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		List<Room_User> room_users = roomUserService.getAll();

		for (Room_User room_user : room_users) {
			if (!(Objects.equals(room_user.getRoomId(), roomId)))
				continue;

			validUsersIds.add(room_user.getUserId());
		}

		stringBuilder.append("[");
		int count = 0;
		for (Integer validUserId : validUsersIds) {
			final String url = "http://usermanagement-service:28080/users/" + validUserId;
			String str = makeRequest(url, MediaType.APPLICATION_JSON);
			stringBuilder.append(str);
			count++;
			if (count < validUsersIds.size())
				stringBuilder.append(",");
		}
		stringBuilder.append("]");

		return stringBuilder.toString();
	}

	@Override
	public List<Room> getAll() {
		log.info("Retrieve all rooms");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Room> criteria = builder.createQuery( Room.class );
		criteria.from(Room.class);
		return em.createQuery( criteria ).getResultList();
	}

	@Override
	public Room get(String roomId) {
		return em.find(Room.class, roomId);
	}


	@Override
	public Long count() {
		log.info("Count the number of rooms");
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(Room.class)));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	@Transactional
	public void update(Room room) {
		Room r = get(room.getRoomId());
		if (r != null) {
			em.merge(room);
		} else {
			throw new IllegalArgumentException("Room does not exist : " + room.getRoomId());
		}
	}

	@Override
	public boolean exists(String roomId) {
		Room r = em.find(Room.class, roomId);
		return (r != null);
	}
}
