package domain.service;

import domain.model.Room;
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
        return em.find(Room_User.class, room_user);
    }

    @Override
    public boolean exists(Room_User room_user) {
        Room_User r = em.find(Room_User.class, room_user);
        return (r != null);
    }

    @Override
    public boolean exists(int roomId, int userId) {
        Room_User room_user = new Room_User();
        room_user.setRoomId(roomId);
        room_user.setUserId(userId);
        Room_User r = em.find(Room_User.class, room_user);
        return (r != null);
    }

    @Override
    @Transactional
    public void create(Room_User room_user) {
        em.persist(room_user);
    }

    @Override
    @Transactional
    public void create(int roomId, int userId) {
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
}
