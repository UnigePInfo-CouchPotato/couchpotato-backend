package domain.service;

import lombok.extern.java.Log;

import domain.model.Room_User;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
