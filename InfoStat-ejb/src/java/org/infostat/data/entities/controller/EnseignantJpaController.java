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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.Enseignant;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class EnseignantJpaController implements Serializable {

    public EnseignantJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Enseignant enseignant) throws RollbackFailureException, Exception {
        if (enseignant.getAffectationenseignants() == null) {
            enseignant.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<AffectationEnseignant> attachedAffectationenseignants = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsAffectationEnseignantToAttach : enseignant.getAffectationenseignants()) {
                affectationenseignantsAffectationEnseignantToAttach = em.getReference(affectationenseignantsAffectationEnseignantToAttach.getClass(), affectationenseignantsAffectationEnseignantToAttach.getId());
                attachedAffectationenseignants.add(affectationenseignantsAffectationEnseignantToAttach);
            }
            enseignant.setAffectationenseignants(attachedAffectationenseignants);
            em.persist(enseignant);
            for (AffectationEnseignant affectationenseignantsAffectationEnseignant : enseignant.getAffectationenseignants()) {
                Enseignant oldEnseignantOfAffectationenseignantsAffectationEnseignant = affectationenseignantsAffectationEnseignant.getEnseignant();
                affectationenseignantsAffectationEnseignant.setEnseignant(enseignant);
                affectationenseignantsAffectationEnseignant = em.merge(affectationenseignantsAffectationEnseignant);
                if (oldEnseignantOfAffectationenseignantsAffectationEnseignant != null) {
                    oldEnseignantOfAffectationenseignantsAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsAffectationEnseignant);
                    oldEnseignantOfAffectationenseignantsAffectationEnseignant = em.merge(oldEnseignantOfAffectationenseignantsAffectationEnseignant);
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

    public void edit(Enseignant enseignant) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Enseignant persistentEnseignant = em.find(Enseignant.class, enseignant.getId());
            Collection<AffectationEnseignant> affectationenseignantsOld = persistentEnseignant.getAffectationenseignants();
            Collection<AffectationEnseignant> affectationenseignantsNew = enseignant.getAffectationenseignants();
            List<String> illegalOrphanMessages = null;
            for (AffectationEnseignant affectationenseignantsOldAffectationEnseignant : affectationenseignantsOld) {
                if (!affectationenseignantsNew.contains(affectationenseignantsOldAffectationEnseignant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AffectationEnseignant " + affectationenseignantsOldAffectationEnseignant + " since its enseignant field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AffectationEnseignant> attachedAffectationenseignantsNew = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignantToAttach : affectationenseignantsNew) {
                affectationenseignantsNewAffectationEnseignantToAttach = em.getReference(affectationenseignantsNewAffectationEnseignantToAttach.getClass(), affectationenseignantsNewAffectationEnseignantToAttach.getId());
                attachedAffectationenseignantsNew.add(affectationenseignantsNewAffectationEnseignantToAttach);
            }
            affectationenseignantsNew = attachedAffectationenseignantsNew;
            enseignant.setAffectationenseignants(affectationenseignantsNew);
            enseignant = em.merge(enseignant);
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignant : affectationenseignantsNew) {
                if (!affectationenseignantsOld.contains(affectationenseignantsNewAffectationEnseignant)) {
                    Enseignant oldEnseignantOfAffectationenseignantsNewAffectationEnseignant = affectationenseignantsNewAffectationEnseignant.getEnseignant();
                    affectationenseignantsNewAffectationEnseignant.setEnseignant(enseignant);
                    affectationenseignantsNewAffectationEnseignant = em.merge(affectationenseignantsNewAffectationEnseignant);
                    if (oldEnseignantOfAffectationenseignantsNewAffectationEnseignant != null && !oldEnseignantOfAffectationenseignantsNewAffectationEnseignant.equals(enseignant)) {
                        oldEnseignantOfAffectationenseignantsNewAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsNewAffectationEnseignant);
                        oldEnseignantOfAffectationenseignantsNewAffectationEnseignant = em.merge(oldEnseignantOfAffectationenseignantsNewAffectationEnseignant);
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
                Long id = enseignant.getId();
                if (findEnseignant(id) == null) {
                    throw new NonexistentEntityException("The enseignant with id " + id + " no longer exists.");
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
            Enseignant enseignant;
            try {
                enseignant = em.getReference(Enseignant.class, id);
                enseignant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enseignant with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AffectationEnseignant> affectationenseignantsOrphanCheck = enseignant.getAffectationenseignants();
            for (AffectationEnseignant affectationenseignantsOrphanCheckAffectationEnseignant : affectationenseignantsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Enseignant (" + enseignant + ") cannot be destroyed since the AffectationEnseignant " + affectationenseignantsOrphanCheckAffectationEnseignant + " in its affectationenseignants field has a non-nullable enseignant field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(enseignant);
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

    public List<Enseignant> findEnseignantEntities() {
        return findEnseignantEntities(true, -1, -1);
    }

    public List<Enseignant> findEnseignantEntities(int maxResults, int firstResult) {
        return findEnseignantEntities(false, maxResults, firstResult);
    }

    private List<Enseignant> findEnseignantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Enseignant.class));
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

    public Enseignant findEnseignant(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Enseignant.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnseignantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Enseignant> rt = cq.from(Enseignant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
