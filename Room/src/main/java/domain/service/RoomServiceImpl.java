package domain.service;

import domain.model.Room;
import domain.model.Room_User;
import domain.model.Users;
import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Log
public class RoomServiceImpl implements RoomService {

    @PersistenceContext(unitName = "RoomPU")
    private EntityManager em;

    private static int counter = 0;

    @Inject
	private RoomUserService roomUserService;

    public RoomServiceImpl(){

	}

	public RoomServiceImpl(EntityManager em){
    	this();
    	this.em = em;
	}

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
	public boolean isRoomAdmin(int roomId, int userId) {
		Room room = get(roomId);
		return room.getRoomAdminId() == userId;
	}

	@Override
	@Transactional
	public int createRoom(int userId) {
    	log.info("Create a room, set the administrator and add the user");
		Room room = new Room();
		room.setRoomAdminId(userId);
		room.setRoomId(counter);
		em.persist(room);
		em.flush();
		counter++;
		int createdRoomId = room.getRoomId();
		roomUserService.create(createdRoomId, userId);
		return createdRoomId;
	}

	private ArrayList<Integer> getAllUsersIds() {
		log.info("Get information on room administrator from user management");
		final String url = "http://usermanagement-service:28080/users/all";
		String users = makeRequest(url, MediaType.APPLICATION_JSON);
		ArrayList<Integer> usersIds = new ArrayList<>();

		final JSONArray jsonArray = new JSONArray(users);
		final int length = jsonArray.length();

		for (int i = 0; i < length; i++) {
			final JSONObject jsonObject = jsonArray.getJSONObject(i);
			final int id = jsonObject.getInt("id");
			usersIds.add(id);
		}

		return usersIds;
	}

	@Override
	public String getRoomUsers(int roomId) {
		log.info("Get all users in a room");
		final ArrayList<Integer> usersIds = getAllUsersIds();
		ArrayList<Integer> validUsersIds = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();

		for (Integer usersId : usersIds) {
			Room_User roomUser = new Room_User();
			roomUser.setUserId(usersId);
			roomUser.setRoomId(roomId);
			if (roomUserService.exists(roomUser)) {
				validUsersIds.add(usersId);
			}
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
	public Room get(int roomId) {
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
	public boolean exists(int roomId) {
		Room r = em.find(Room.class, roomId);
		return (r != null);
	}
}
