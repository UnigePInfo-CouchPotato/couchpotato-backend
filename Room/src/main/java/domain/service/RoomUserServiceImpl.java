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
    public Room_User get(Room_User room_user) {
        log.info("Get a room_user");
        return em.find(Room_User.class, room_user);
    }

    @Override
    public Room_User get(int roomId, int userId) {
        log.info("Get a room_user");
        Room_User room_user = new Room_User();
        room_user.setRoomId(roomId);
        room_user.setUserId(userId);
        return em.find(Room_User.class, room_user);
    }

    @Override
    public boolean exists(Room_User room_user) {
        log.info("Check if a room_user exists");
        Room_User r = em.find(Room_User.class, room_user);
        return (r != null);
    }

    @Override
    public boolean exists(int roomId, int userId) {
        log.info("Check if a room_user exists");
        Room_User room_user = new Room_User();
        room_user.setRoomId(roomId);
        room_user.setUserId(userId);
        Room_User r = em.find(Room_User.class, room_user);
        return (r != null);
    }

    @Override
    @Transactional
    public void create(Room_User room_user) {
        log.info("Create a room_user");
        em.persist(room_user);
    }

    @Override
    @Transactional
    public void create(int roomId, int userId) {
        log.info("Create a room_user");
        Room_User room_user = new Room_User();
        room_user.setRoomId(roomId);
        room_user.setUserId(userId);
        em.persist(room_user);
    }

    @Override
    public List<Room_User> getAll() {
        log.info("Retrieve all room_users");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Room_User> criteria = builder.createQuery( Room_User.class );
        criteria.from(Room_User.class);
        return em.createQuery( criteria ).getResultList();
    }

    @Override
    @Transactional
    public void setUserGenres(int roomId, int userId, String genres) {
        log.info("Set user genres in a room");
        Room_User room_user = get(roomId, userId);
        room_user.setGenres(genres);
    }
}
