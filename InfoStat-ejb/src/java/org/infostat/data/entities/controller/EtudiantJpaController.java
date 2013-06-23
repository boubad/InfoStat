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
import org.infostat.data.entities.AffectationEtudiant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.EtudiantEvent;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class EtudiantJpaController implements Serializable {

    public EtudiantJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Etudiant etudiant) throws RollbackFailureException, Exception {
        if (etudiant.getAffectationetudiants() == null) {
            etudiant.setAffectationetudiants(new ArrayList<AffectationEtudiant>());
        }
        if (etudiant.getEtudiantevents() == null) {
            etudiant.setEtudiantevents(new ArrayList<EtudiantEvent>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AffectationEtudiant> attachedAffectationetudiants = new ArrayList<AffectationEtudiant>();
            for (AffectationEtudiant affectationetudiantsAffectationEtudiantToAttach : etudiant.getAffectationetudiants()) {
                affectationetudiantsAffectationEtudiantToAttach = em.getReference(affectationetudiantsAffectationEtudiantToAttach.getClass(), affectationetudiantsAffectationEtudiantToAttach.getId());
                attachedAffectationetudiants.add(affectationetudiantsAffectationEtudiantToAttach);
            }
            etudiant.setAffectationetudiants(attachedAffectationetudiants);
            Collection<EtudiantEvent> attachedEtudiantevents = new ArrayList<EtudiantEvent>();
            for (EtudiantEvent etudianteventsEtudiantEventToAttach : etudiant.getEtudiantevents()) {
                etudianteventsEtudiantEventToAttach = em.getReference(etudianteventsEtudiantEventToAttach.getClass(), etudianteventsEtudiantEventToAttach.getId());
                attachedEtudiantevents.add(etudianteventsEtudiantEventToAttach);
            }
            etudiant.setEtudiantevents(attachedEtudiantevents);
            em.persist(etudiant);
            for (AffectationEtudiant affectationetudiantsAffectationEtudiant : etudiant.getAffectationetudiants()) {
                Etudiant oldEtudiantOfAffectationetudiantsAffectationEtudiant = affectationetudiantsAffectationEtudiant.getEtudiant();
                affectationetudiantsAffectationEtudiant.setEtudiant(etudiant);
                affectationetudiantsAffectationEtudiant = em.merge(affectationetudiantsAffectationEtudiant);
                if (oldEtudiantOfAffectationetudiantsAffectationEtudiant != null) {
                    oldEtudiantOfAffectationetudiantsAffectationEtudiant.getAffectationetudiants().remove(affectationetudiantsAffectationEtudiant);
                    oldEtudiantOfAffectationetudiantsAffectationEtudiant = em.merge(oldEtudiantOfAffectationetudiantsAffectationEtudiant);
                }
            }
            for (EtudiantEvent etudianteventsEtudiantEvent : etudiant.getEtudiantevents()) {
                Etudiant oldEtudiantOfEtudianteventsEtudiantEvent = etudianteventsEtudiantEvent.getEtudiant();
                etudianteventsEtudiantEvent.setEtudiant(etudiant);
                etudianteventsEtudiantEvent = em.merge(etudianteventsEtudiantEvent);
                if (oldEtudiantOfEtudianteventsEtudiantEvent != null) {
                    oldEtudiantOfEtudianteventsEtudiantEvent.getEtudiantevents().remove(etudianteventsEtudiantEvent);
                    oldEtudiantOfEtudianteventsEtudiantEvent = em.merge(oldEtudiantOfEtudianteventsEtudiantEvent);
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

    public void edit(Etudiant etudiant) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Etudiant persistentEtudiant = em.find(Etudiant.class, etudiant.getId());
            Collection<AffectationEtudiant> affectationetudiantsOld = persistentEtudiant.getAffectationetudiants();
            Collection<AffectationEtudiant> affectationetudiantsNew = etudiant.getAffectationetudiants();
            Collection<EtudiantEvent> etudianteventsOld = persistentEtudiant.getEtudiantevents();
            Collection<EtudiantEvent> etudianteventsNew = etudiant.getEtudiantevents();
            List<String> illegalOrphanMessages = null;
            for (AffectationEtudiant affectationetudiantsOldAffectationEtudiant : affectationetudiantsOld) {
                if (!affectationetudiantsNew.contains(affectationetudiantsOldAffectationEtudiant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AffectationEtudiant " + affectationetudiantsOldAffectationEtudiant + " since its etudiant field is not nullable.");
                }
            }
            for (EtudiantEvent etudianteventsOldEtudiantEvent : etudianteventsOld) {
                if (!etudianteventsNew.contains(etudianteventsOldEtudiantEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EtudiantEvent " + etudianteventsOldEtudiantEvent + " since its etudiant field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AffectationEtudiant> attachedAffectationetudiantsNew = new ArrayList<AffectationEtudiant>();
            for (AffectationEtudiant affectationetudiantsNewAffectationEtudiantToAttach : affectationetudiantsNew) {
                affectationetudiantsNewAffectationEtudiantToAttach = em.getReference(affectationetudiantsNewAffectationEtudiantToAttach.getClass(), affectationetudiantsNewAffectationEtudiantToAttach.getId());
                attachedAffectationetudiantsNew.add(affectationetudiantsNewAffectationEtudiantToAttach);
            }
            affectationetudiantsNew = attachedAffectationetudiantsNew;
            etudiant.setAffectationetudiants(affectationetudiantsNew);
            Collection<EtudiantEvent> attachedEtudianteventsNew = new ArrayList<EtudiantEvent>();
            for (EtudiantEvent etudianteventsNewEtudiantEventToAttach : etudianteventsNew) {
                etudianteventsNewEtudiantEventToAttach = em.getReference(etudianteventsNewEtudiantEventToAttach.getClass(), etudianteventsNewEtudiantEventToAttach.getId());
                attachedEtudianteventsNew.add(etudianteventsNewEtudiantEventToAttach);
            }
            etudianteventsNew = attachedEtudianteventsNew;
            etudiant.setEtudiantevents(etudianteventsNew);
            etudiant = em.merge(etudiant);
            for (AffectationEtudiant affectationetudiantsNewAffectationEtudiant : affectationetudiantsNew) {
                if (!affectationetudiantsOld.contains(affectationetudiantsNewAffectationEtudiant)) {
                    Etudiant oldEtudiantOfAffectationetudiantsNewAffectationEtudiant = affectationetudiantsNewAffectationEtudiant.getEtudiant();
                    affectationetudiantsNewAffectationEtudiant.setEtudiant(etudiant);
                    affectationetudiantsNewAffectationEtudiant = em.merge(affectationetudiantsNewAffectationEtudiant);
                    if (oldEtudiantOfAffectationetudiantsNewAffectationEtudiant != null && !oldEtudiantOfAffectationetudiantsNewAffectationEtudiant.equals(etudiant)) {
                        oldEtudiantOfAffectationetudiantsNewAffectationEtudiant.getAffectationetudiants().remove(affectationetudiantsNewAffectationEtudiant);
                        oldEtudiantOfAffectationetudiantsNewAffectationEtudiant = em.merge(oldEtudiantOfAffectationetudiantsNewAffectationEtudiant);
                    }
                }
            }
            for (EtudiantEvent etudianteventsNewEtudiantEvent : etudianteventsNew) {
                if (!etudianteventsOld.contains(etudianteventsNewEtudiantEvent)) {
                    Etudiant oldEtudiantOfEtudianteventsNewEtudiantEvent = etudianteventsNewEtudiantEvent.getEtudiant();
                    etudianteventsNewEtudiantEvent.setEtudiant(etudiant);
                    etudianteventsNewEtudiantEvent = em.merge(etudianteventsNewEtudiantEvent);
                    if (oldEtudiantOfEtudianteventsNewEtudiantEvent != null && !oldEtudiantOfEtudianteventsNewEtudiantEvent.equals(etudiant)) {
                        oldEtudiantOfEtudianteventsNewEtudiantEvent.getEtudiantevents().remove(etudianteventsNewEtudiantEvent);
                        oldEtudiantOfEtudianteventsNewEtudiantEvent = em.merge(oldEtudiantOfEtudianteventsNewEtudiantEvent);
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
                Long id = etudiant.getId();
                if (findEtudiant(id) == null) {
                    throw new NonexistentEntityException("The etudiant with id " + id + " no longer exists.");
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
            Etudiant etudiant;
            try {
                etudiant = em.getReference(Etudiant.class, id);
                etudiant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The etudiant with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AffectationEtudiant> affectationetudiantsOrphanCheck = etudiant.getAffectationetudiants();
            for (AffectationEtudiant affectationetudiantsOrphanCheckAffectationEtudiant : affectationetudiantsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Etudiant (" + etudiant + ") cannot be destroyed since the AffectationEtudiant " + affectationetudiantsOrphanCheckAffectationEtudiant + " in its affectationetudiants field has a non-nullable etudiant field.");
            }
            Collection<EtudiantEvent> etudianteventsOrphanCheck = etudiant.getEtudiantevents();
            for (EtudiantEvent etudianteventsOrphanCheckEtudiantEvent : etudianteventsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Etudiant (" + etudiant + ") cannot be destroyed since the EtudiantEvent " + etudianteventsOrphanCheckEtudiantEvent + " in its etudiantevents field has a non-nullable etudiant field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(etudiant);
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

    public List<Etudiant> findEtudiantEntities() {
        return findEtudiantEntities(true, -1, -1);
    }

    public List<Etudiant> findEtudiantEntities(int maxResults, int firstResult) {
        return findEtudiantEntities(false, maxResults, firstResult);
    }

    private List<Etudiant> findEtudiantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Etudiant.class));
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

    public Etudiant findEtudiant(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Etudiant.class, id);
        } finally {
            em.close();
        }
    }

    public int getEtudiantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Etudiant> rt = cq.from(Etudiant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
