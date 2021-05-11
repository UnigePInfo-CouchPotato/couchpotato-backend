package domain.service;

import domain.model.Room;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Log
public class RoomServiceImpl implements RoomService {

    @PersistenceContext(unitName = "RoomPU")
    private EntityManager em;

    public RoomServiceImpl(){

	}

	public RoomServiceImpl(EntityManager em){
    	this();
    	this.em = em;
	}

	@Override
	public List<Room> getAll() {
		log.info("retrieve all rooms");
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
