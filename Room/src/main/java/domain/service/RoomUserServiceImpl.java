package domain.service;

import lombok.extern.java.Log;

import domain.model.RoomUser;
import org.json.JSONArray;

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
    public RoomUser get(String roomId, String userNickname) {
        log.info("Get a roomUser");
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(roomId);
        roomUser.setUserNickname(userNickname);
        return em.find(RoomUser.class, roomUser);
    }

    @Override
    public boolean exists(String roomId, String userNickname) {
        log.info("Check if a roomUser exists");
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(roomId);
        roomUser.setUserNickname(userNickname);
        RoomUser r = em.find(RoomUser.class, roomUser);
        return (r != null);
    }

    @Override
    @Transactional
    public void create(RoomUser roomUser) {
        log.info("Create a roomUser");
        em.persist(roomUser);
    }

    @Override
    @Transactional
    public void create(String roomId, String userNickname) {
        log.info("Create a roomUser");
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(roomId);
        roomUser.setUserNickname(userNickname);
        em.persist(roomUser);
    }

    @Override
    public List<RoomUser> getAll() {
        log.info("Retrieve all roomUsers");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<RoomUser> criteria = builder.createQuery( RoomUser.class );
        criteria.from(RoomUser.class);
        return em.createQuery( criteria ).getResultList();
    }

    @Override
    @Transactional
    public void setUserVotes(String roomId, String userNickname, JSONArray choice) {
        log.info("Set user votes in a room");
        RoomUser roomUser = get(roomId, userNickname);
        String votes = choice.toString();
        roomUser.setVotes(votes);
    }

    @Override
    public List<RoomUser> getAllFromRoomId(String roomId) {
        log.info("Retrieve all roomUsers associated to a room");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<RoomUser> criteria = builder.createQuery( RoomUser.class );
        criteria.from(RoomUser.class);
        List<RoomUser> roomUsers = em.createQuery( criteria ).getResultList();
        roomUsers.removeIf(roomUser -> !Objects.equals(roomUser.getRoomId(), roomId));
        return roomUsers;
    }
}
