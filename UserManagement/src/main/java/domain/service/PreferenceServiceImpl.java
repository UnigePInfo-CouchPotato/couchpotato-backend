package domain.service;
import domain.model.Preference;
import domain.model.Users;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Log
public class PreferenceServiceImpl implements PreferenceService {

    @PersistenceContext(unitName = "UserManagementPU")
    private EntityManager em;

    @Inject private PreferenceService prefService;

    public PreferenceServiceImpl(){}
    public PreferenceServiceImpl(EntityManager em){
        this();
        this.em = em;
    }

    @Override
    @Transactional
    public void createPreference(int userId, String genreIdsString) {
        log.info("Create a Preference instance for user with id userId");
        Preference pref = new Preference();
        pref.setUserId(userId);
        pref.setGenreIds(genreIdsString);
        em.persist(pref);
    }

    @Override
    @Transactional
    public void updatePreference(Preference pref) {
        Preference p = get(pref.getUserId());
        if (p != null) {
            em.merge(pref);
        } else {
            throw new IllegalArgumentException("User does not exist : " + pref.getUserId());
        }
    }

    @Override
    public Preference get(int userId) {
        return em.find(Preference.class, userId);
    }

    @Override
    public List<Preference> getAll() {
        log.info("Retrieve all preference instances");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Preference> criteria = builder.createQuery( Preference.class );
        criteria.from(Preference.class);
        return em.createQuery( criteria ).getResultList();
    }

    @Override
    public Long count() {
        log.info("Count the number of users that have preferences");
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(Preference.class)));
        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public boolean exists(int userId) {
        Preference p = em.find(Preference.class, userId);
        return (p != null);
    }

    @Override
    @Transactional
    public void create(Preference preference) {
        em.persist(preference);
    }
}
