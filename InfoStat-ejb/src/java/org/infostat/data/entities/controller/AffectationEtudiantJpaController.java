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
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.Semestre;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class AffectationEtudiantJpaController implements Serializable {

    public AffectationEtudiantJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AffectationEtudiant affectationEtudiant) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Semestre semestre = affectationEtudiant.getSemestre();
            if (semestre != null) {
                semestre = em.getReference(semestre.getClass(), semestre.getId());
                affectationEtudiant.setSemestre(semestre);
            }
            Groupe groupe = affectationEtudiant.getGroupe();
            if (groupe != null) {
                groupe = em.getReference(groupe.getClass(), groupe.getId());
                affectationEtudiant.setGroupe(groupe);
            }
            Etudiant etudiant = affectationEtudiant.getEtudiant();
            if (etudiant != null) {
                etudiant = em.getReference(etudiant.getClass(), etudiant.getId());
                affectationEtudiant.setEtudiant(etudiant);
            }
            em.persist(affectationEtudiant);
            if (semestre != null) {
                semestre.getAffectationetudiants().add(affectationEtudiant);
                semestre = em.merge(semestre);
            }
            if (groupe != null) {
                groupe.getAffectationEtudiants().add(affectationEtudiant);
                groupe = em.merge(groupe);
            }
            if (etudiant != null) {
                etudiant.getAffectationetudiants().add(affectationEtudiant);
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

    public void edit(AffectationEtudiant affectationEtudiant) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AffectationEtudiant persistentAffectationEtudiant = em.find(AffectationEtudiant.class, affectationEtudiant.getId());
            Semestre semestreOld = persistentAffectationEtudiant.getSemestre();
            Semestre semestreNew = affectationEtudiant.getSemestre();
            Groupe groupeOld = persistentAffectationEtudiant.getGroupe();
            Groupe groupeNew = affectationEtudiant.getGroupe();
            Etudiant etudiantOld = persistentAffectationEtudiant.getEtudiant();
            Etudiant etudiantNew = affectationEtudiant.getEtudiant();
            if (semestreNew != null) {
                semestreNew = em.getReference(semestreNew.getClass(), semestreNew.getId());
                affectationEtudiant.setSemestre(semestreNew);
            }
            if (groupeNew != null) {
                groupeNew = em.getReference(groupeNew.getClass(), groupeNew.getId());
                affectationEtudiant.setGroupe(groupeNew);
            }
            if (etudiantNew != null) {
                etudiantNew = em.getReference(etudiantNew.getClass(), etudiantNew.getId());
                affectationEtudiant.setEtudiant(etudiantNew);
            }
            affectationEtudiant = em.merge(affectationEtudiant);
            if (semestreOld != null && !semestreOld.equals(semestreNew)) {
                semestreOld.getAffectationetudiants().remove(affectationEtudiant);
                semestreOld = em.merge(semestreOld);
            }
            if (semestreNew != null && !semestreNew.equals(semestreOld)) {
                semestreNew.getAffectationetudiants().add(affectationEtudiant);
                semestreNew = em.merge(semestreNew);
            }
            if (groupeOld != null && !groupeOld.equals(groupeNew)) {
                groupeOld.getAffectationEtudiants().remove(affectationEtudiant);
                groupeOld = em.merge(groupeOld);
            }
            if (groupeNew != null && !groupeNew.equals(groupeOld)) {
                groupeNew.getAffectationEtudiants().add(affectationEtudiant);
                groupeNew = em.merge(groupeNew);
            }
            if (etudiantOld != null && !etudiantOld.equals(etudiantNew)) {
                etudiantOld.getAffectationetudiants().remove(affectationEtudiant);
                etudiantOld = em.merge(etudiantOld);
            }
            if (etudiantNew != null && !etudiantNew.equals(etudiantOld)) {
                etudiantNew.getAffectationetudiants().add(affectationEtudiant);
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
                Long id = affectationEtudiant.getId();
                if (findAffectationEtudiant(id) == null) {
                    throw new NonexistentEntityException("The affectationEtudiant with id " + id + " no longer exists.");
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
            AffectationEtudiant affectationEtudiant;
            try {
                affectationEtudiant = em.getReference(AffectationEtudiant.class, id);
                affectationEtudiant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The affectationEtudiant with id " + id + " no longer exists.", enfe);
            }
            Semestre semestre = affectationEtudiant.getSemestre();
            if (semestre != null) {
                semestre.getAffectationetudiants().remove(affectationEtudiant);
                semestre = em.merge(semestre);
            }
            Groupe groupe = affectationEtudiant.getGroupe();
            if (groupe != null) {
                groupe.getAffectationEtudiants().remove(affectationEtudiant);
                groupe = em.merge(groupe);
            }
            Etudiant etudiant = affectationEtudiant.getEtudiant();
            if (etudiant != null) {
                etudiant.getAffectationetudiants().remove(affectationEtudiant);
                etudiant = em.merge(etudiant);
            }
            em.remove(affectationEtudiant);
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

    public List<AffectationEtudiant> findAffectationEtudiantEntities() {
        return findAffectationEtudiantEntities(true, -1, -1);
    }

    public List<AffectationEtudiant> findAffectationEtudiantEntities(int maxResults, int firstResult) {
        return findAffectationEtudiantEntities(false, maxResults, firstResult);
    }

    private List<AffectationEtudiant> findAffectationEtudiantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AffectationEtudiant.class));
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

    public AffectationEtudiant findAffectationEtudiant(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AffectationEtudiant.class, id);
        } finally {
            em.close();
        }
    }

    public int getAffectationEtudiantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AffectationEtudiant> rt = cq.from(AffectationEtudiant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
