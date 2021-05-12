package domain.service;

import domain.model.Users;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Log
public class UserServiceImpl implements UserService {

    @PersistenceContext(unitName = "UserManagementPU")
    private EntityManager em;

    public UserServiceImpl(){ }

	public UserServiceImpl(EntityManager em){
    	this();
    	this.em = em;
	}
    
	@Override
	public List<Users> getAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> cq = cb.createQuery(Users.class);
		Root<Users> rootEntry = cq.from(Users.class);
		CriteriaQuery<Users> all = cq.select(rootEntry);

		TypedQuery<Users> allQuery = em.createQuery(all);
		return allQuery.getResultList();

//		CriteriaBuilder qb = em.getCriteriaBuilder();
//		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
//		cq.select(qb.count(cq.from(Users.class)));
//		return em.createQuery(cq).getSingleResult();
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


	@Override
	@Transactional
	public void create(Users user) {
    	em.persist(user);
	}


}
