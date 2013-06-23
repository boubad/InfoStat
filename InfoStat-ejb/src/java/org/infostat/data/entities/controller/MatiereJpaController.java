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
import org.infostat.data.entities.Unite;
import org.infostat.data.entities.AffectationEnseignant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class MatiereJpaController implements Serializable {

    public MatiereJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Matiere matiere) throws RollbackFailureException, Exception {
        if (matiere.getAffectationenseignants() == null) {
            matiere.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Unite unite = matiere.getUnite();
            if (unite != null) {
                unite = em.getReference(unite.getClass(), unite.getId());
                matiere.setUnite(unite);
            }
            Collection<AffectationEnseignant> attachedAffectationenseignants = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsAffectationEnseignantToAttach : matiere.getAffectationenseignants()) {
                affectationenseignantsAffectationEnseignantToAttach = em.getReference(affectationenseignantsAffectationEnseignantToAttach.getClass(), affectationenseignantsAffectationEnseignantToAttach.getId());
                attachedAffectationenseignants.add(affectationenseignantsAffectationEnseignantToAttach);
            }
            matiere.setAffectationenseignants(attachedAffectationenseignants);
            em.persist(matiere);
            if (unite != null) {
                unite.getMatieres().add(matiere);
                unite = em.merge(unite);
            }
            for (AffectationEnseignant affectationenseignantsAffectationEnseignant : matiere.getAffectationenseignants()) {
                Matiere oldMatiereOfAffectationenseignantsAffectationEnseignant = affectationenseignantsAffectationEnseignant.getMatiere();
                affectationenseignantsAffectationEnseignant.setMatiere(matiere);
                affectationenseignantsAffectationEnseignant = em.merge(affectationenseignantsAffectationEnseignant);
                if (oldMatiereOfAffectationenseignantsAffectationEnseignant != null) {
                    oldMatiereOfAffectationenseignantsAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsAffectationEnseignant);
                    oldMatiereOfAffectationenseignantsAffectationEnseignant = em.merge(oldMatiereOfAffectationenseignantsAffectationEnseignant);
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

    public void edit(Matiere matiere) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Matiere persistentMatiere = em.find(Matiere.class, matiere.getId());
            Unite uniteOld = persistentMatiere.getUnite();
            Unite uniteNew = matiere.getUnite();
            Collection<AffectationEnseignant> affectationenseignantsOld = persistentMatiere.getAffectationenseignants();
            Collection<AffectationEnseignant> affectationenseignantsNew = matiere.getAffectationenseignants();
            List<String> illegalOrphanMessages = null;
            for (AffectationEnseignant affectationenseignantsOldAffectationEnseignant : affectationenseignantsOld) {
                if (!affectationenseignantsNew.contains(affectationenseignantsOldAffectationEnseignant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AffectationEnseignant " + affectationenseignantsOldAffectationEnseignant + " since its matiere field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (uniteNew != null) {
                uniteNew = em.getReference(uniteNew.getClass(), uniteNew.getId());
                matiere.setUnite(uniteNew);
            }
            Collection<AffectationEnseignant> attachedAffectationenseignantsNew = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignantToAttach : affectationenseignantsNew) {
                affectationenseignantsNewAffectationEnseignantToAttach = em.getReference(affectationenseignantsNewAffectationEnseignantToAttach.getClass(), affectationenseignantsNewAffectationEnseignantToAttach.getId());
                attachedAffectationenseignantsNew.add(affectationenseignantsNewAffectationEnseignantToAttach);
            }
            affectationenseignantsNew = attachedAffectationenseignantsNew;
            matiere.setAffectationenseignants(affectationenseignantsNew);
            matiere = em.merge(matiere);
            if (uniteOld != null && !uniteOld.equals(uniteNew)) {
                uniteOld.getMatieres().remove(matiere);
                uniteOld = em.merge(uniteOld);
            }
            if (uniteNew != null && !uniteNew.equals(uniteOld)) {
                uniteNew.getMatieres().add(matiere);
                uniteNew = em.merge(uniteNew);
            }
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignant : affectationenseignantsNew) {
                if (!affectationenseignantsOld.contains(affectationenseignantsNewAffectationEnseignant)) {
                    Matiere oldMatiereOfAffectationenseignantsNewAffectationEnseignant = affectationenseignantsNewAffectationEnseignant.getMatiere();
                    affectationenseignantsNewAffectationEnseignant.setMatiere(matiere);
                    affectationenseignantsNewAffectationEnseignant = em.merge(affectationenseignantsNewAffectationEnseignant);
                    if (oldMatiereOfAffectationenseignantsNewAffectationEnseignant != null && !oldMatiereOfAffectationenseignantsNewAffectationEnseignant.equals(matiere)) {
                        oldMatiereOfAffectationenseignantsNewAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsNewAffectationEnseignant);
                        oldMatiereOfAffectationenseignantsNewAffectationEnseignant = em.merge(oldMatiereOfAffectationenseignantsNewAffectationEnseignant);
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
                Long id = matiere.getId();
                if (findMatiere(id) == null) {
                    throw new NonexistentEntityException("The matiere with id " + id + " no longer exists.");
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
            Matiere matiere;
            try {
                matiere = em.getReference(Matiere.class, id);
                matiere.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matiere with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AffectationEnseignant> affectationenseignantsOrphanCheck = matiere.getAffectationenseignants();
            for (AffectationEnseignant affectationenseignantsOrphanCheckAffectationEnseignant : affectationenseignantsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Matiere (" + matiere + ") cannot be destroyed since the AffectationEnseignant " + affectationenseignantsOrphanCheckAffectationEnseignant + " in its affectationenseignants field has a non-nullable matiere field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Unite unite = matiere.getUnite();
            if (unite != null) {
                unite.getMatieres().remove(matiere);
                unite = em.merge(unite);
            }
            em.remove(matiere);
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

    public List<Matiere> findMatiereEntities() {
        return findMatiereEntities(true, -1, -1);
    }

    public List<Matiere> findMatiereEntities(int maxResults, int firstResult) {
        return findMatiereEntities(false, maxResults, firstResult);
    }

    private List<Matiere> findMatiereEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matiere.class));
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

    public Matiere findMatiere(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Matiere.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatiereCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matiere> rt = cq.from(Matiere.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
