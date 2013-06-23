/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.infostat.data.entities.AffectationEnseignant;
import org.infostat.data.entities.EtudiantEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.GroupeEvent;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class GroupeEventJpaController implements Serializable {

    public GroupeEventJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GroupeEvent groupeEvent) throws RollbackFailureException, Exception {
        if (groupeEvent.getEtudiantevents() == null) {
            groupeEvent.setEtudiantevents(new ArrayList<EtudiantEvent>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AffectationEnseignant affectationenseignant = groupeEvent.getAffectationenseignant();
            if (affectationenseignant != null) {
                affectationenseignant = em.getReference(affectationenseignant.getClass(), affectationenseignant.getId());
                groupeEvent.setAffectationenseignant(affectationenseignant);
            }
            Collection<EtudiantEvent> attachedEtudiantevents = new ArrayList<EtudiantEvent>();
            for (EtudiantEvent etudianteventsEtudiantEventToAttach : groupeEvent.getEtudiantevents()) {
                etudianteventsEtudiantEventToAttach = em.getReference(etudianteventsEtudiantEventToAttach.getClass(), etudianteventsEtudiantEventToAttach.getId());
                attachedEtudiantevents.add(etudianteventsEtudiantEventToAttach);
            }
            groupeEvent.setEtudiantevents(attachedEtudiantevents);
            em.persist(groupeEvent);
            if (affectationenseignant != null) {
                affectationenseignant.getGroupeevents().add(groupeEvent);
                affectationenseignant = em.merge(affectationenseignant);
            }
            for (EtudiantEvent etudianteventsEtudiantEvent : groupeEvent.getEtudiantevents()) {
                GroupeEvent oldGroupeventOfEtudianteventsEtudiantEvent = etudianteventsEtudiantEvent.getGroupevent();
                etudianteventsEtudiantEvent.setGroupevent(groupeEvent);
                etudianteventsEtudiantEvent = em.merge(etudianteventsEtudiantEvent);
                if (oldGroupeventOfEtudianteventsEtudiantEvent != null) {
                    oldGroupeventOfEtudianteventsEtudiantEvent.getEtudiantevents().remove(etudianteventsEtudiantEvent);
                    oldGroupeventOfEtudianteventsEtudiantEvent = em.merge(oldGroupeventOfEtudianteventsEtudiantEvent);
                }
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

    public void edit(GroupeEvent groupeEvent) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GroupeEvent persistentGroupeEvent = em.find(GroupeEvent.class, groupeEvent.getId());
            AffectationEnseignant affectationenseignantOld = persistentGroupeEvent.getAffectationenseignant();
            AffectationEnseignant affectationenseignantNew = groupeEvent.getAffectationenseignant();
            Collection<EtudiantEvent> etudianteventsOld = persistentGroupeEvent.getEtudiantevents();
            Collection<EtudiantEvent> etudianteventsNew = groupeEvent.getEtudiantevents();
            List<String> illegalOrphanMessages = null;
            for (EtudiantEvent etudianteventsOldEtudiantEvent : etudianteventsOld) {
                if (!etudianteventsNew.contains(etudianteventsOldEtudiantEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EtudiantEvent " + etudianteventsOldEtudiantEvent + " since its groupevent field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (affectationenseignantNew != null) {
                affectationenseignantNew = em.getReference(affectationenseignantNew.getClass(), affectationenseignantNew.getId());
                groupeEvent.setAffectationenseignant(affectationenseignantNew);
            }
            Collection<EtudiantEvent> attachedEtudianteventsNew = new ArrayList<EtudiantEvent>();
            for (EtudiantEvent etudianteventsNewEtudiantEventToAttach : etudianteventsNew) {
                etudianteventsNewEtudiantEventToAttach = em.getReference(etudianteventsNewEtudiantEventToAttach.getClass(), etudianteventsNewEtudiantEventToAttach.getId());
                attachedEtudianteventsNew.add(etudianteventsNewEtudiantEventToAttach);
            }
            etudianteventsNew = attachedEtudianteventsNew;
            groupeEvent.setEtudiantevents(etudianteventsNew);
            groupeEvent = em.merge(groupeEvent);
            if (affectationenseignantOld != null && !affectationenseignantOld.equals(affectationenseignantNew)) {
                affectationenseignantOld.getGroupeevents().remove(groupeEvent);
                affectationenseignantOld = em.merge(affectationenseignantOld);
            }
            if (affectationenseignantNew != null && !affectationenseignantNew.equals(affectationenseignantOld)) {
                affectationenseignantNew.getGroupeevents().add(groupeEvent);
                affectationenseignantNew = em.merge(affectationenseignantNew);
            }
            for (EtudiantEvent etudianteventsNewEtudiantEvent : etudianteventsNew) {
                if (!etudianteventsOld.contains(etudianteventsNewEtudiantEvent)) {
                    GroupeEvent oldGroupeventOfEtudianteventsNewEtudiantEvent = etudianteventsNewEtudiantEvent.getGroupevent();
                    etudianteventsNewEtudiantEvent.setGroupevent(groupeEvent);
                    etudianteventsNewEtudiantEvent = em.merge(etudianteventsNewEtudiantEvent);
                    if (oldGroupeventOfEtudianteventsNewEtudiantEvent != null && !oldGroupeventOfEtudianteventsNewEtudiantEvent.equals(groupeEvent)) {
                        oldGroupeventOfEtudianteventsNewEtudiantEvent.getEtudiantevents().remove(etudianteventsNewEtudiantEvent);
                        oldGroupeventOfEtudianteventsNewEtudiantEvent = em.merge(oldGroupeventOfEtudianteventsNewEtudiantEvent);
                    }
                }
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
                Long id = groupeEvent.getId();
                if (findGroupeEvent(id) == null) {
                    throw new NonexistentEntityException("The groupeEvent with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GroupeEvent groupeEvent;
            try {
                groupeEvent = em.getReference(GroupeEvent.class, id);
                groupeEvent.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The groupeEvent with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EtudiantEvent> etudianteventsOrphanCheck = groupeEvent.getEtudiantevents();
            for (EtudiantEvent etudianteventsOrphanCheckEtudiantEvent : etudianteventsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GroupeEvent (" + groupeEvent + ") cannot be destroyed since the EtudiantEvent " + etudianteventsOrphanCheckEtudiantEvent + " in its etudiantevents field has a non-nullable groupevent field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AffectationEnseignant affectationenseignant = groupeEvent.getAffectationenseignant();
            if (affectationenseignant != null) {
                affectationenseignant.getGroupeevents().remove(groupeEvent);
                affectationenseignant = em.merge(affectationenseignant);
            }
            em.remove(groupeEvent);
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

    public List<GroupeEvent> findGroupeEventEntities() {
        return findGroupeEventEntities(true, -1, -1);
    }

    public List<GroupeEvent> findGroupeEventEntities(int maxResults, int firstResult) {
        return findGroupeEventEntities(false, maxResults, firstResult);
    }

    private List<GroupeEvent> findGroupeEventEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GroupeEvent.class));
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

    public GroupeEvent findGroupeEvent(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GroupeEvent.class, id);
        } finally {
            em.close();
        }
    }

    public int getGroupeEventCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GroupeEvent> rt = cq.from(GroupeEvent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
