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
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.AffectationEnseignant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class GroupeJpaController implements Serializable {

    public GroupeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Groupe groupe) throws RollbackFailureException, Exception {
        if (groupe.getAffectationenseignants() == null) {
            groupe.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departement departement = groupe.getDepartement();
            if (departement != null) {
                departement = em.getReference(departement.getClass(), departement.getId());
                groupe.setDepartement(departement);
            }
            Collection<AffectationEnseignant> attachedAffectationenseignants = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsAffectationEnseignantToAttach : groupe.getAffectationenseignants()) {
                affectationenseignantsAffectationEnseignantToAttach = em.getReference(affectationenseignantsAffectationEnseignantToAttach.getClass(), affectationenseignantsAffectationEnseignantToAttach.getId());
                attachedAffectationenseignants.add(affectationenseignantsAffectationEnseignantToAttach);
            }
            groupe.setAffectationenseignants(attachedAffectationenseignants);
            em.persist(groupe);
            if (departement != null) {
                departement.getGroupes().add(groupe);
                departement = em.merge(departement);
            }
            for (AffectationEnseignant affectationenseignantsAffectationEnseignant : groupe.getAffectationenseignants()) {
                Groupe oldGroupeOfAffectationenseignantsAffectationEnseignant = affectationenseignantsAffectationEnseignant.getGroupe();
                affectationenseignantsAffectationEnseignant.setGroupe(groupe);
                affectationenseignantsAffectationEnseignant = em.merge(affectationenseignantsAffectationEnseignant);
                if (oldGroupeOfAffectationenseignantsAffectationEnseignant != null) {
                    oldGroupeOfAffectationenseignantsAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsAffectationEnseignant);
                    oldGroupeOfAffectationenseignantsAffectationEnseignant = em.merge(oldGroupeOfAffectationenseignantsAffectationEnseignant);
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

    public void edit(Groupe groupe) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Groupe persistentGroupe = em.find(Groupe.class, groupe.getId());
            Departement departementOld = persistentGroupe.getDepartement();
            Departement departementNew = groupe.getDepartement();
            Collection<AffectationEnseignant> affectationenseignantsOld = persistentGroupe.getAffectationenseignants();
            Collection<AffectationEnseignant> affectationenseignantsNew = groupe.getAffectationenseignants();
            List<String> illegalOrphanMessages = null;
            for (AffectationEnseignant affectationenseignantsOldAffectationEnseignant : affectationenseignantsOld) {
                if (!affectationenseignantsNew.contains(affectationenseignantsOldAffectationEnseignant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AffectationEnseignant " + affectationenseignantsOldAffectationEnseignant + " since its groupe field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departementNew != null) {
                departementNew = em.getReference(departementNew.getClass(), departementNew.getId());
                groupe.setDepartement(departementNew);
            }
            Collection<AffectationEnseignant> attachedAffectationenseignantsNew = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignantToAttach : affectationenseignantsNew) {
                affectationenseignantsNewAffectationEnseignantToAttach = em.getReference(affectationenseignantsNewAffectationEnseignantToAttach.getClass(), affectationenseignantsNewAffectationEnseignantToAttach.getId());
                attachedAffectationenseignantsNew.add(affectationenseignantsNewAffectationEnseignantToAttach);
            }
            affectationenseignantsNew = attachedAffectationenseignantsNew;
            groupe.setAffectationenseignants(affectationenseignantsNew);
            groupe = em.merge(groupe);
            if (departementOld != null && !departementOld.equals(departementNew)) {
                departementOld.getGroupes().remove(groupe);
                departementOld = em.merge(departementOld);
            }
            if (departementNew != null && !departementNew.equals(departementOld)) {
                departementNew.getGroupes().add(groupe);
                departementNew = em.merge(departementNew);
            }
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignant : affectationenseignantsNew) {
                if (!affectationenseignantsOld.contains(affectationenseignantsNewAffectationEnseignant)) {
                    Groupe oldGroupeOfAffectationenseignantsNewAffectationEnseignant = affectationenseignantsNewAffectationEnseignant.getGroupe();
                    affectationenseignantsNewAffectationEnseignant.setGroupe(groupe);
                    affectationenseignantsNewAffectationEnseignant = em.merge(affectationenseignantsNewAffectationEnseignant);
                    if (oldGroupeOfAffectationenseignantsNewAffectationEnseignant != null && !oldGroupeOfAffectationenseignantsNewAffectationEnseignant.equals(groupe)) {
                        oldGroupeOfAffectationenseignantsNewAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsNewAffectationEnseignant);
                        oldGroupeOfAffectationenseignantsNewAffectationEnseignant = em.merge(oldGroupeOfAffectationenseignantsNewAffectationEnseignant);
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
                Long id = groupe.getId();
                if (findGroupe(id) == null) {
                    throw new NonexistentEntityException("The groupe with id " + id + " no longer exists.");
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
            Groupe groupe;
            try {
                groupe = em.getReference(Groupe.class, id);
                groupe.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The groupe with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AffectationEnseignant> affectationenseignantsOrphanCheck = groupe.getAffectationenseignants();
            for (AffectationEnseignant affectationenseignantsOrphanCheckAffectationEnseignant : affectationenseignantsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Groupe (" + groupe + ") cannot be destroyed since the AffectationEnseignant " + affectationenseignantsOrphanCheckAffectationEnseignant + " in its affectationenseignants field has a non-nullable groupe field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departement departement = groupe.getDepartement();
            if (departement != null) {
                departement.getGroupes().remove(groupe);
                departement = em.merge(departement);
            }
            em.remove(groupe);
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

    public List<Groupe> findGroupeEntities() {
        return findGroupeEntities(true, -1, -1);
    }

    public List<Groupe> findGroupeEntities(int maxResults, int firstResult) {
        return findGroupeEntities(false, maxResults, firstResult);
    }

    private List<Groupe> findGroupeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Groupe.class));
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

    public Groupe findGroupe(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Groupe.class, id);
        } finally {
            em.close();
        }
    }

    public int getGroupeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Groupe> rt = cq.from(Groupe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
