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

    public Optional<song> getSongByTitle(String title) {
        EntityManager em = jpautil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM song s WHERE s.title = :title", song.class)
                     .setParameter("title", title)
                     .getResultStream()
                     .findFirst();
        } finally {
            em.close();
        }
    }

    public void deleteSong(Long id) {
        EntityManager em = jpautil.getEntityManager();
        try {
            em.getTransaction().begin();
            song songToDelete = em.find(song.class, id);
            if (songToDelete != null) {
                em.remove(songToDelete);
            } else {
                System.out.println("No song found with ID: " + id + " for deletion.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting song with ID: " + id, e);
        } finally {
            em.close();
        }
    }
}