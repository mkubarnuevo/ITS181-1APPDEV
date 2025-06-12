package finalproj.model;

import java.util.List;
import java.util.Optional;

import finalproj.util.jpautil;
import jakarta.persistence.EntityManager;

public class songservice {

    public void saveSong(song s) {
        EntityManager em = jpautil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<song> getAllSongs() {
        EntityManager em = jpautil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM song s", song.class).getResultList();
        } finally {
            em.close();
        }
    }

    // --- MODIFIED: Returns Optional<song> for safer handling of no results ---
    public Optional<song> getSongByTitle(String title) {
        EntityManager em = jpautil.getEntityManager();
        try {
            // Using getResultStream().findFirst() is generally safer as getSingleResult()
            // throws NoResultException if no entity is found, which you'd then have to catch.
            // findFirst() elegantly returns an empty Optional in that case.
            return em.createQuery("SELECT s FROM song s WHERE s.title = :title", song.class)
                     .setParameter("title", title)
                     .getResultStream() // Get a stream of results
                     .findFirst();      // Get the first result as an Optional, or empty if none
        } finally {
            em.close();
        }
    }
}