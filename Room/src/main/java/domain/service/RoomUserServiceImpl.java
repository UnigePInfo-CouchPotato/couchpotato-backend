package domain.service;

import lombok.extern.java.Log;

import domain.model.Room_User;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Log
public class RoomUserServiceImpl implements RoomUserService {

    @PersistenceContext(unitName = "RoomPU")
    private EntityManager em;

    public RoomUserServiceImpl(){

    }

    public RoomUserServiceImpl(EntityManager em){
        this();
        this.em = em;
    }

    @Override
    public Room_User get(Room_User roomUser) {
        log.info("Get a roomUser");
        return em.find(Room_User.class, roomUser);
    }

    @Override
    public Room_User get(String roomId, int userId) {
        log.info("Get a roomUser");
        Room_User roomUser = new Room_User();
        roomUser.setRoomId(roomId);
        roomUser.setUserId(userId);
        return em.find(Room_User.class, roomUser);
    }

    @Override
    public boolean exists(Room_User roomUser) {
        log.info("Check if a roomUser exists");
        Room_User r = em.find(Room_User.class, roomUser);
        return (r != null);
    }

    @Override
    public boolean exists(String roomId, int userId) {
        log.info("Check if a roomUser exists");
        Room_User roomUser = new Room_User();
        roomUser.setRoomId(roomId);
        roomUser.setUserId(userId);
        Room_User r = em.find(Room_User.class, roomUser);
        return (r != null);
    }

    @Override
    @Transactional
    public void create(Room_User roomUser) {
        log.info("Create a roomUser");
        em.persist(roomUser);
    }

    @Override
    @Transactional
    public void create(String roomId, int userId) {
        log.info("Create a roomUser");
        Room_User roomUser = new Room_User();
        roomUser.setRoomId(roomId);
        roomUser.setUserId(userId);
        em.persist(roomUser);
    }

    @Override
    public List<Room_User> getAll() {
        log.info("Retrieve all roomUsers");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Room_User> criteria = builder.createQuery( Room_User.class );
        criteria.from(Room_User.class);
        return em.createQuery( criteria ).getResultList();
    }

    @Override
    @Transactional
    public void setUserGenres(String roomId, int userId, String genres) {
        log.info("Set user genres in a room");
        Room_User roomUser = get(roomId, userId);
        genres = genres.replace("\"", "").replaceAll("\\s", "");
        roomUser.setGenres(genres);
    }

    @Override
    @Transactional
    public void setUserVotes(String roomId, int userId, String votes) {
        // TODO Change votes representation in database
        log.info("Set user votes in a room");
        Room_User roomUser = get(roomId, userId);
        votes = votes.replaceAll("\\s", "");
        roomUser.setVotes(votes);
    }

    @Override
    public int countRoomUsers(String roomId) {
        log.info("Count number of roomUsers records associated to this room id");
        int counter = 0;
        List<Room_User> roomUsers = getAll();
        for (Room_User roomUser : roomUsers) {
            if (Objects.equals(roomUser.getRoomId(), roomId)) {
                counter++;
            }
        }
        return counter;
    }
}
