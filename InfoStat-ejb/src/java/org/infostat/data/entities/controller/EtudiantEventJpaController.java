/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.GroupeEvent;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.EtudiantEvent;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class EtudiantEventJpaController implements Serializable {

    public EtudiantEventJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EtudiantEvent etudiantEvent) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GroupeEvent groupevent = etudiantEvent.getGroupevent();
            if (groupevent != null) {
                groupevent = em.getReference(groupevent.getClass(), groupevent.getId());
                etudiantEvent.setGroupevent(groupevent);
            }
            Etudiant etudiant = etudiantEvent.getEtudiant();
            if (etudiant != null) {
                etudiant = em.getReference(etudiant.getClass(), etudiant.getId());
                etudiantEvent.setEtudiant(etudiant);
            }
            em.persist(etudiantEvent);
            if (groupevent != null) {
                groupevent.getEtudiantevents().add(etudiantEvent);
                groupevent = em.merge(groupevent);
            }
            if (etudiant != null) {
                etudiant.getEtudiantevents().add(etudiantEvent);
                etudiant = em.merge(etudiant);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EtudiantEvent etudiantEvent) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EtudiantEvent persistentEtudiantEvent = em.find(EtudiantEvent.class, etudiantEvent.getId());
            GroupeEvent groupeventOld = persistentEtudiantEvent.getGroupevent();
            GroupeEvent groupeventNew = etudiantEvent.getGroupevent();
            Etudiant etudiantOld = persistentEtudiantEvent.getEtudiant();
            Etudiant etudiantNew = etudiantEvent.getEtudiant();
            if (groupeventNew != null) {
                groupeventNew = em.getReference(groupeventNew.getClass(), groupeventNew.getId());
                etudiantEvent.setGroupevent(groupeventNew);
            }
            if (etudiantNew != null) {
                etudiantNew = em.getReference(etudiantNew.getClass(), etudiantNew.getId());
                etudiantEvent.setEtudiant(etudiantNew);
            }
            etudiantEvent = em.merge(etudiantEvent);
            if (groupeventOld != null && !groupeventOld.equals(groupeventNew)) {
                groupeventOld.getEtudiantevents().remove(etudiantEvent);
                groupeventOld = em.merge(groupeventOld);
            }
            if (groupeventNew != null && !groupeventNew.equals(groupeventOld)) {
                groupeventNew.getEtudiantevents().add(etudiantEvent);
                groupeventNew = em.merge(groupeventNew);
            }
            if (etudiantOld != null && !etudiantOld.equals(etudiantNew)) {
                etudiantOld.getEtudiantevents().remove(etudiantEvent);
                etudiantOld = em.merge(etudiantOld);
            }
            if (etudiantNew != null && !etudiantNew.equals(etudiantOld)) {
                etudiantNew.getEtudiantevents().add(etudiantEvent);
                etudiantNew = em.merge(etudiantNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = etudiantEvent.getId();
                if (findEtudiantEvent(id) == null) {
                    throw new NonexistentEntityException("The etudiantEvent with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EtudiantEvent etudiantEvent;
            try {
                etudiantEvent = em.getReference(EtudiantEvent.class, id);
                etudiantEvent.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The etudiantEvent with id " + id + " no longer exists.", enfe);
            }
            GroupeEvent groupevent = etudiantEvent.getGroupevent();
            if (groupevent != null) {
                groupevent.getEtudiantevents().remove(etudiantEvent);
                groupevent = em.merge(groupevent);
            }
            Etudiant etudiant = etudiantEvent.getEtudiant();
            if (etudiant != null) {
                etudiant.getEtudiantevents().remove(etudiantEvent);
                etudiant = em.merge(etudiant);
            }
            em.remove(etudiantEvent);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EtudiantEvent> findEtudiantEventEntities() {
        return findEtudiantEventEntities(true, -1, -1);
    }

    public List<EtudiantEvent> findEtudiantEventEntities(int maxResults, int firstResult) {
        return findEtudiantEventEntities(false, maxResults, firstResult);
    }

    private List<EtudiantEvent> findEtudiantEventEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EtudiantEvent.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public EtudiantEvent findEtudiantEvent(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EtudiantEvent.class, id);
        } finally {
            em.close();
        }
    }

    public int getEtudiantEventCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EtudiantEvent> rt = cq.from(EtudiantEvent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
