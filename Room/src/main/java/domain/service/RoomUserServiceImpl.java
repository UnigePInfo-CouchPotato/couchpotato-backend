package domain.service;

import lombok.extern.java.Log;

import domain.model.Room_User;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
}
