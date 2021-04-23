package domain.service;

import domain.model.User;
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
public class UserServiceImpl implements UserService {

    @PersistenceContext(unitName = "UserPU")
    private EntityManager em;
    
	@Override
	public List<User> getAll() {
		log.info("retrieve all users");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery( User.class );
		criteria.from(User.class);
		return em.createQuery( criteria ).getResultList();
	}
	
	@Override
	public User get(String username) {
		return em.find(User.class, username);
	}
	public User get(int id) {
		return em.find(User.class, id);
	}


	@Override
	public Long count() {
		log.info("Count the number of users");
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(User.class)));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	@Transactional
	public void update(User user) {
		User u = get(user.getId());
		if (u != null) {
			em.merge(user);
		} else {
			throw new IllegalArgumentException("User does not exist : " + user.getId());
		}
	}

	@Override
	public boolean exists(int id) {
		User u = em.find(User.class, id);
		return (u!=null);
	}

}
