package domain.service;

import domain.model.Room;
import domain.model.RoomUser;
import domain.model.Singleton;
import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONException;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.*;
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
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String BAD_REQUEST = "Bad request";
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String AUTH0_URL = "https://couchpotato.eu.auth0.com/v2/userinfo";
	private static final String RECOMMENDATION_SERVICE_URL = "http://recommendation-service:28080/recommendation/";


	/*USEFUL METHODS*/
	public String createID() { return UUID.randomUUID().toString().substring(24); }

	public String makeRequest(String url, String token) {
		Client client = ClientBuilder.newClient();
		try {
			WebTarget webTarget = client.target(url);
			Response response = webTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).get();

			if (response.getStatusInfo() == Response.Status.UNAUTHORIZED) {
				return UNAUTHORIZED;
			}

			if (response.getStatus() != 200) {
				return BAD_REQUEST;
			}

			return response.readEntity(String.class);
		} catch (Exception e) {
			return "[]";
		}
	}

	@Override
	public JSONObject getUserInfo(String token) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(AUTH0_URL);
		Response response = webTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).get();
		String str = response.readEntity(String.class);
		if (Objects.equals(str, UNAUTHORIZED))
			return new JSONObject();

		JSONObject jsonObject = new JSONObject(str);
		if (jsonObject.keySet().contains(ERROR))
			return new JSONObject();

		return jsonObject;
	}

	public boolean isInteger(Object object) {
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

	public <K, V> Stream<K> keys(Map<K, V> map, V value) {
		return map
				.entrySet()
				.stream()
				.filter(entry -> value.equals(entry.getValue()))
				.map(Map.Entry::getKey);
	}

	public String getUserPreferences(JSONObject userInfo) {
		try {
			JSONObject metadata = userInfo.getJSONObject("https://pinfo2.unige.ch/metadata");
			JSONArray preferences = metadata.getJSONArray("preferences");
			List<Integer> integerList = new ArrayList<>();
			preferences.forEach(o -> integerList.add((Integer) o));
			return integerList.stream().map(Object::toString).collect(Collectors.joining(","));
		} catch (JSONException e) {
			return "";
		}
	}

	public String getUserNickname(JSONObject userInfo) {
		try {
			return userInfo.getString("nickname");
		} catch (JSONException e) {
			return "";
		}
	}

	@Override
	@Transactional
	public void endJoinPeriod(String roomId) {
		Room room = get(roomId);
		room.setUsersCanJoin(false);
	}

	@Override
	@Transactional
	public void endVotingPeriod(String roomId) {
		Room room = get(roomId);
		room.setUsersCanVote(false);
	}

	@Override
	@Transactional
	public void startVotingPeriod(String roomId) {
		Room room = get(roomId);
		room.setUsersCanVote(true);
	}

	@Override
	public boolean isRoomAdmin(String roomId, String token) {
    	log.info("Check if user is the administrator of a room");
		Room room = get(roomId);
		JSONObject userInfo = getUserInfo(token);
		return Objects.equals(room.getRoomAdmin(), userInfo.toString());
	}

	@Override
	@Transactional
	public String createRoom(JSONObject user) {
    	log.info("Create a room, set the administrator and add the user");
		Room room = new Room();
		room.setRoomAdmin(user.toString());
		room.setRoomId(createID());
		em.persist(room);
		em.flush();
		String createdRoomId = room.getRoomId();
		if (roomUserService != null)
			roomUserService.create(createdRoomId, getUserNickname(user));

		String str = getUserPreferences(user);
		room.setUserPreferences(str);
		return createdRoomId;
	}

	@Override
	@Transactional
	public void joinRoom(String roomId, JSONObject userInfo) {
    	log.info("Add user to a room");
    	Room room = get(roomId);
		String newPreferences = getUserPreferences(userInfo);
		String userPreferences = room.getUserPreferences();
		String updatedPreferences = userPreferences + "," + newPreferences;
		room.setUserPreferences(updatedPreferences);

		if (roomUserService != null)
			roomUserService.create(roomId, getUserNickname(userInfo));
	}

	@Override
	@Transactional
	public void deleteRoom(String roomId) {
    	log.info("Delete a room");
    	//Delete room
    	Room room = get(roomId);
		em.remove(room);

		//Delete all roomUser records associated
		if (roomUserService != null) {
			List<RoomUser> roomUsers = roomUserService.getAllFromRoomId(roomId);
			roomUsers.forEach(roomUser -> em.remove(roomUser));
		}
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
	public Map<String, JSONObject> checkTokenValidity(String token) {
    	log.info("Check if the token is valid");
		String response = makeRequest(AUTH0_URL, token);

		Map<String, JSONObject> map = new HashMap<>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", response);
		jsonObject.put("valid", !Objects.equals(response, UNAUTHORIZED) && !Objects.equals(response, "[]"));

		if (response.equals("[]") || response.equals(UNAUTHORIZED) || response.equals(BAD_REQUEST) || response.isEmpty()) {
			jsonObject.put("userInfo", new JSONObject());
			map.put("info", jsonObject);
			return map;
		}
		System.out.println(token);
		System.out.println(response);

		try {
			if (response.charAt(0) != '{') {
				String newResponse = "{" + response + "}";
				jsonObject.put("userInfo", new JSONObject(newResponse));
			}
			else {
				jsonObject.put("userInfo", new JSONObject(response));
			}
		} catch (Exception e) {
			jsonObject.put("userInfo", new JSONObject());
		}

		map.put("info", jsonObject);

		return map;
	}

	@Override
	public String getMovieWithMostVotes(String roomId) {
    	log.info("Get the movie with the most votes");
		Singleton singleton = Singleton.getInstance();
		Map<String, JSONObject> roomMoviesData = singleton.getHashMap();
		JSONObject jsonObject = roomMoviesData.get(roomId);

		if (jsonObject == null) {
			JSONObject message = new JSONObject();
			message.put(MESSAGE, String.format("No data for room %s", roomId));
			return message.toString();
		}

		if (roomUserService == null) {
			JSONObject message = new JSONObject();
			message.put(ERROR, "No movie");
			return message.toString();
		}

		List<RoomUser> roomUsers = roomUserService.getAllFromRoomId(roomId);
    	HashMap<String, Integer> index = new HashMap<>();

		for (RoomUser roomUser : roomUsers) {
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
		JSONObject movie = jsonObject.getJSONObject(movieIdWithMostVotes);

		if (movie == null || movie.isEmpty()) {
			JSONObject message = new JSONObject();
			message.put(MESSAGE, String.format("No data for movie %s", movieIdWithMostVotes));
			return message.toString();
		}

		return movie.toString();
	}

	@Override
	@Transactional
	public String getMovies(String roomId, String token) {
		log.info("Get movies from recommendation service");

		Room room = get(roomId);
		JSONObject message = new JSONObject();
		message.put(ERROR, "Please set some preferences for this room");

		//Check if movies have been set already
		String movies = room.getMovies();
		if (!movies.isEmpty())
			return movies;

		String preferences = room.getUserPreferences();
		if (preferences.isEmpty())
			return message.toString();

		LinkedHashMap<Integer, Integer> index = new LinkedHashMap<>();
		List<String> genres = Arrays.asList(preferences.split(","));
		boolean areNumbers = genres.stream().allMatch(this::isInteger);
		if (!areNumbers)
			return message.toString();

		List<Integer> integers = Arrays.stream(preferences.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		for (Integer id : integers) {
			Integer value = index.get(id);
			index.put(id, (value == null) ? 1 : ++value);
		}

		if (index.size() < 3) {
			message.clear();
			message.put(MESSAGE, "Please set at least 3 genres");
			return message.toString();
		}

		List<Integer> indexValues = new ArrayList<>(index.values());
		indexValues.sort(Collections.reverseOrder());

		int count = 0;
		Integer[] genresIdsWithTheMostOccurrences = {0, 0, 0};

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
				for (int i = 0; i < (3 - count); i++, count++) {
					Collections.shuffle(keys);
					int randomInt = new SecureRandom().nextInt(keys.size());
					Integer value = keys.get(randomInt);
					genresIdsWithTheMostOccurrences[count] = value;
					index.remove(keys.get(randomInt));
					keys.remove(value);
				}
			}
		}

		String idGenres = Arrays.stream(genresIdsWithTheMostOccurrences).map(String::valueOf).collect(Collectors.joining(","));
		final String url = RECOMMENDATION_SERVICE_URL + "selectGenres/" + idGenres;
		String response = makeRequest(url, token);

		Singleton singleton = Singleton.getInstance();
		JSONArray jsonArray = new JSONArray(response);
		JSONObject data = new JSONObject();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject movie = jsonArray.getJSONObject(i);
			String movieId = String.valueOf(movie.getInt("id"));
			data.put(movieId, movie);
		}

		Map<String, JSONObject> roomMoviesData = singleton.getHashMap();
		roomMoviesData.put(roomId, data);
		singleton.setHashMap(roomMoviesData);

		room.setNumberOfMovies(jsonArray.length());
		room.setMovies(response);

		return response;
	}

	@Override
	public boolean isUserInRoom(String roomId, JSONObject userInfo) {
		log.info("Check if user is already in a room");
		String userNickname = getUserNickname(userInfo);
		if (userNickname.isEmpty())
			return false;

		return roomUserService.exists(roomId, userNickname);
	}

	@Override
	public String getRoomUsers(String roomId) {
		log.info("Get all users in a room");
		if (roomUserService == null)
			return "";

		List<RoomUser> roomUsers = roomUserService.getAllFromRoomId(roomId);
		JSONArray users = new JSONArray();

		for (RoomUser roomUser : roomUsers) {
			users.put(roomUser.getUserNickname());
		}

		return users.toString();
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

	@Override
	public boolean canJoinRoom(String roomId) {
		Room room = get(roomId);
		return room.isUsersCanJoin();
	}

	@Override
	public boolean canVote(String roomId) {
		Room room = get(roomId);
		return room.isUsersCanVote();
	}
}
