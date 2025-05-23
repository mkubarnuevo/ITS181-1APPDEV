package finalproj.model;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class songrepo {

    private EntityManager em;

    public songrepo(EntityManager em) {
        this.em = em;
    }

    public List<song> findAll() {
        TypedQuery<song> query = em.createQuery("SELECT s FROM song s", song.class);
        return query.getResultList();
    }

    public void save(song s) {
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
    }

    public void delete(song s) {
        em.getTransaction().begin();
        em.remove(em.contains(s) ? s : em.merge(s));
        em.getTransaction().commit();
    }

    // Add other useful DB operations if needed
}
