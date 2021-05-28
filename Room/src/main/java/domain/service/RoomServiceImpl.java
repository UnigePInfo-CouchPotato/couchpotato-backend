package domain.service;

import domain.model.Room;
import domain.model.Room_User;
import domain.model.Singleton;
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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	/*CONSTANTS*/
	private static final String USER_MANAGEMENT_SERVICE_URL = "http://usermanagement-service:28080/users/";
	private static final String RECOMMENDATION_SERVICE_URL = "http://recommendation-service:28080/recommendation/";

	/*USEFUL METHODS*/
	private String createID() { return UUID.randomUUID().toString().substring(24); }

	private String makeRequest(String url) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(url);
		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

		if (response.getStatus() != 200) {
			return "Failed : HTTP error code : " + response.getStatus();
		}

		return response.readEntity(String.class);
	}

	private boolean isInteger(Object object) {
		if (object instanceof Integer) {
			return true;
		}
		else {
			String string = object.toString();

			try {
				Integer.parseInt(string);
			} catch(Exception e) {
				return false;
			}
		}

		return true;
	}

	private <K, V> Stream<K> keys(Map<K, V> map, V value) {
		return map
				.entrySet()
				.stream()
				.filter(entry -> value.equals(entry.getValue()))
				.map(Map.Entry::getKey);
	}

	@Override
	public Users getRoomAdmin(int userId) {
		log.info("Get information on room administrator from user management");
		final String url = USER_MANAGEMENT_SERVICE_URL + userId;
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(url);
		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
		return response.readEntity(Users.class);
	}

	@Override
	public boolean isRoomAdmin(String roomId, int userId) {
    	log.info("Check if user is the administrator of a room");
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
    	log.info("Add user to a room");
		roomUserService.create(roomId, userId);
	}

	@Override
	@Transactional
	public void deleteRoom(String roomId) {
    	log.info("Delete a room");
    	//Delete room
    	Room room = get(roomId);
    	if (room != null)
    		em.remove(room);

    	//Delete all roomUser records associated
		List<Room_User> roomUsers = roomUserService.getAllFromRoomId(roomId);
		roomUsers.forEach(roomUser -> em.remove(roomUser));
	}

	@Override
	@Transactional
	public boolean closeRoom(String roomId) {
    	log.info("Close a room");
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
		final String url = USER_MANAGEMENT_SERVICE_URL + userId + "/exists";
		String response = makeRequest(url);
		return !Boolean.parseBoolean(response);
	}

	@Override
	public boolean isRoomClosed(String roomId) {
		log.info("Check if room is closed");
		Room room = get(roomId);
		return room.isRoomClosed();
	}

	@Override
	public String getMovieWithMostVotes(String roomId) {
    	log.info("Get the index of the movie with the most votes");
		Singleton singleton = Singleton.getInstance();
		HashMap<String, JSONObject> roomMoviesData = singleton.getHashMap();
		JSONObject jsonObject = roomMoviesData.get(roomId);

		if (jsonObject == null)
			return "{" + String.format("\"error\":\"No data for room %s\"", roomId) + "}";

		List<Room_User> roomUsers = roomUserService.getAllFromRoomId(roomId);
    	HashMap<String, Integer> index = new HashMap<>();

		for (Room_User roomUser : roomUsers) {
			String userVote = roomUser.getVotes();
			JSONArray votes = new JSONArray(userVote);
			JSONObject movieData = votes.getJSONObject(0);
			for (String movieId : movieData.keySet()) {
				Integer movieScore = movieData.getInt(movieId);
				if (!index.containsKey(movieId)) {
					index.put(movieId, movieScore);
				}
				else {
					Integer updatedScore = index.get(movieId) + movieScore;
					index.put(movieId, updatedScore);
				}
			}
		}

		String movieIdWithMostVotes = Collections.max(index.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

		return (jsonObject.getJSONObject(movieIdWithMostVotes) != null) ? jsonObject.getJSONObject(movieIdWithMostVotes).toString() : "{" + String.format("\"error\":\"No data for movie %s\"", movieIdWithMostVotes) + "}";
	}

	@Override
	@Transactional
	public String getMovies(String roomId, int userId) {
		log.info("Get movies from recommendation service");
		LinkedHashMap<Integer, Integer> index = new LinkedHashMap<>();
		List<Room_User> roomUsers = roomUserService.getAllFromRoomId(roomId);
		Integer[] genresIdsWithTheMostOccurrences = {0, 0, 0};
		for (Room_User roomUser : roomUsers) {
			String userGenres = roomUser.getGenres();
			List<String> genres = Arrays.asList(userGenres.split(","));
			boolean areNumbers = genres.stream().allMatch(this::isInteger);
			if (!areNumbers)
				continue;

			List<Integer> integers = Arrays.stream(userGenres.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			for (Integer id : integers) {
				Integer value = index.get(id);
				index.put(id, (value == null) ? 1 : ++value);
			}
		}

		// TODO Check if hashmap index is empty

		List<Integer> indexValues = new ArrayList<>(index.values());
		indexValues.sort(Collections.reverseOrder());
		int count = 0;

		// TODO Make sure there are at least 3 index values (i.e. 3 genres ids)

		while (count < 3) {
			Stream<Integer> keyStream = keys(index, indexValues.get(count));
			List<Integer> keys = keyStream.collect(Collectors.toList());
			int size = keys.size();

			if (size == 1) {
				genresIdsWithTheMostOccurrences[count] = keys.get(0);
				index.remove(keys.get(0));
				count++;
			}
			else {
				for (int i = 0; i < (3 - count); i++) {
					Collections.shuffle(keys);
					int randomInt = ThreadLocalRandom.current().nextInt(keys.size());
					Integer value = keys.get(randomInt);
					genresIdsWithTheMostOccurrences[count] = value;
					index.remove(keys.get(randomInt));
					keys.remove(value);
					count++;
				}
			}
		}

		String idGenres = Arrays.stream(genresIdsWithTheMostOccurrences).map(String::valueOf).collect(Collectors.joining(","));
		final String url = RECOMMENDATION_SERVICE_URL + "selectGenres=" + idGenres;
		String response = makeRequest(url);

		Singleton singleton = Singleton.getInstance();
		JSONArray jsonArray = new JSONArray(response);
		JSONObject data = new JSONObject();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject movie = jsonArray.getJSONObject(i);
			String movieId = String.valueOf(movie.getInt("id"));
			data.put(movieId, movie);
		}

		HashMap<String, JSONObject> roomMoviesData = singleton.getHashMap();
		roomMoviesData.put(roomId, data);
		singleton.setHashMap(roomMoviesData);

		Room r = get(roomId);
		r.setNumberOfMovies(jsonArray.length());

		return response;
	}

	@Override
	public boolean isUserInRoom(String roomId, int userId) {
		log.info("Check if user is already in a room");
		return roomUserService.exists(roomId, userId);
	}

	@Override
	public String getRoomUsers(String roomId) {
		log.info("Get all users in a room");
		ArrayList<Integer> validUsersIds = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		List<Room_User> roomUsers = roomUserService.getAllFromRoomId(roomId);

		for (Room_User roomUser : roomUsers) {
			validUsersIds.add(roomUser.getUserId());
		}

		stringBuilder.append("[");
		int count = 0;
		for (Integer validUserId : validUsersIds) {
			final String url = USER_MANAGEMENT_SERVICE_URL + validUserId;
			String str = makeRequest(url);
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
		log.info("Get a specific room");
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
	public boolean exists(String roomId) {
		log.info("Check if a room exists");
		Room r = em.find(Room.class, roomId);
		return (r != null);
	}
}
