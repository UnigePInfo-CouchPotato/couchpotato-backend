package domain.service;

import domain.model.Users;
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

    @PersistenceContext(unitName = "UserManagementPU")
    private EntityManager em;

    public UserServiceImpl(){

	}

	public UserServiceImpl(EntityManager em){
    	this();
    	this.em = em;
	}
    
	@Override
	public List<Users> getAll() {
		log.info("retrieve all users");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = builder.createQuery( Users.class );
		criteria.from(Users.class);
		return em.createQuery( criteria ).getResultList();
	}
	
	@Override
	public Users get(String username) {
		return em.find(Users.class, username);
	}
	public Users get(int id) {
		return em.find(Users.class, id);
	}


	@Override
	public Long count() {
		log.info("Count the number of users");
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(Users.class)));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	@Transactional
	public void update(Users users) {
		Users u = get(users.getId());
		if (u != null) {
			em.merge(users);
		} else {
			throw new IllegalArgumentException("User does not exist : " + users.getId());
		}
	}

	@Override
	public boolean exists(int id) {
		Users u = em.find(Users.class, id);
		return (u!=null);
	}

}
