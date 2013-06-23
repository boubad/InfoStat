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
import org.infostat.data.entities.Matiere;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.Unite;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class UniteJpaController implements Serializable {

    public UniteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Unite unite) throws RollbackFailureException, Exception {
        if (unite.getMatieres() == null) {
            unite.setMatieres(new ArrayList<Matiere>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departement departement = unite.getDepartement();
            if (departement != null) {
                departement = em.getReference(departement.getClass(), departement.getId());
                unite.setDepartement(departement);
            }
            Collection<Matiere> attachedMatieres = new ArrayList<Matiere>();
            for (Matiere matieresMatiereToAttach : unite.getMatieres()) {
                matieresMatiereToAttach = em.getReference(matieresMatiereToAttach.getClass(), matieresMatiereToAttach.getId());
                attachedMatieres.add(matieresMatiereToAttach);
            }
            unite.setMatieres(attachedMatieres);
            em.persist(unite);
            if (departement != null) {
                departement.getUnites().add(unite);
                departement = em.merge(departement);
            }
            for (Matiere matieresMatiere : unite.getMatieres()) {
                Unite oldUniteOfMatieresMatiere = matieresMatiere.getUnite();
                matieresMatiere.setUnite(unite);
                matieresMatiere = em.merge(matieresMatiere);
                if (oldUniteOfMatieresMatiere != null) {
                    oldUniteOfMatieresMatiere.getMatieres().remove(matieresMatiere);
                    oldUniteOfMatieresMatiere = em.merge(oldUniteOfMatieresMatiere);
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

    public void edit(Unite unite) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Unite persistentUnite = em.find(Unite.class, unite.getId());
            Departement departementOld = persistentUnite.getDepartement();
            Departement departementNew = unite.getDepartement();
            Collection<Matiere> matieresOld = persistentUnite.getMatieres();
            Collection<Matiere> matieresNew = unite.getMatieres();
            List<String> illegalOrphanMessages = null;
            for (Matiere matieresOldMatiere : matieresOld) {
                if (!matieresNew.contains(matieresOldMatiere)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matiere " + matieresOldMatiere + " since its unite field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departementNew != null) {
                departementNew = em.getReference(departementNew.getClass(), departementNew.getId());
                unite.setDepartement(departementNew);
            }
            Collection<Matiere> attachedMatieresNew = new ArrayList<Matiere>();
            for (Matiere matieresNewMatiereToAttach : matieresNew) {
                matieresNewMatiereToAttach = em.getReference(matieresNewMatiereToAttach.getClass(), matieresNewMatiereToAttach.getId());
                attachedMatieresNew.add(matieresNewMatiereToAttach);
            }
            matieresNew = attachedMatieresNew;
            unite.setMatieres(matieresNew);
            unite = em.merge(unite);
            if (departementOld != null && !departementOld.equals(departementNew)) {
                departementOld.getUnites().remove(unite);
                departementOld = em.merge(departementOld);
            }
            if (departementNew != null && !departementNew.equals(departementOld)) {
                departementNew.getUnites().add(unite);
                departementNew = em.merge(departementNew);
            }
            for (Matiere matieresNewMatiere : matieresNew) {
                if (!matieresOld.contains(matieresNewMatiere)) {
                    Unite oldUniteOfMatieresNewMatiere = matieresNewMatiere.getUnite();
                    matieresNewMatiere.setUnite(unite);
                    matieresNewMatiere = em.merge(matieresNewMatiere);
                    if (oldUniteOfMatieresNewMatiere != null && !oldUniteOfMatieresNewMatiere.equals(unite)) {
                        oldUniteOfMatieresNewMatiere.getMatieres().remove(matieresNewMatiere);
                        oldUniteOfMatieresNewMatiere = em.merge(oldUniteOfMatieresNewMatiere);
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
                Long id = unite.getId();
                if (findUnite(id) == null) {
                    throw new NonexistentEntityException("The unite with id " + id + " no longer exists.");
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
            Unite unite;
            try {
                unite = em.getReference(Unite.class, id);
                unite.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The unite with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Matiere> matieresOrphanCheck = unite.getMatieres();
            for (Matiere matieresOrphanCheckMatiere : matieresOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Unite (" + unite + ") cannot be destroyed since the Matiere " + matieresOrphanCheckMatiere + " in its matieres field has a non-nullable unite field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departement departement = unite.getDepartement();
            if (departement != null) {
                departement.getUnites().remove(unite);
                departement = em.merge(departement);
            }
            em.remove(unite);
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

    public List<Unite> findUniteEntities() {
        return findUniteEntities(true, -1, -1);
    }

    public List<Unite> findUniteEntities(int maxResults, int firstResult) {
        return findUniteEntities(false, maxResults, firstResult);
    }

    private List<Unite> findUniteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Unite.class));
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

    public Unite findUnite(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Unite.class, id);
        } finally {
            em.close();
        }
    }

    public int getUniteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Unite> rt = cq.from(Unite.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
