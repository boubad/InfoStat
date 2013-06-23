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
import org.infostat.data.entities.Semestre;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class AnneeJpaController implements Serializable {

    public AnneeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Annee annee) throws RollbackFailureException, Exception {
        if (annee.getSemestres() == null) {
            annee.setSemestres(new ArrayList<Semestre>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departement departement = annee.getDepartement();
            if (departement != null) {
                departement = em.getReference(departement.getClass(), departement.getId());
                annee.setDepartement(departement);
            }
            Collection<Semestre> attachedSemestres = new ArrayList<Semestre>();
            for (Semestre semestresSemestreToAttach : annee.getSemestres()) {
                semestresSemestreToAttach = em.getReference(semestresSemestreToAttach.getClass(), semestresSemestreToAttach.getId());
                attachedSemestres.add(semestresSemestreToAttach);
            }
            annee.setSemestres(attachedSemestres);
            em.persist(annee);
            if (departement != null) {
                departement.getAnnees().add(annee);
                departement = em.merge(departement);
            }
            for (Semestre semestresSemestre : annee.getSemestres()) {
                Annee oldAnneeOfSemestresSemestre = semestresSemestre.getAnnee();
                semestresSemestre.setAnnee(annee);
                semestresSemestre = em.merge(semestresSemestre);
                if (oldAnneeOfSemestresSemestre != null) {
                    oldAnneeOfSemestresSemestre.getSemestres().remove(semestresSemestre);
                    oldAnneeOfSemestresSemestre = em.merge(oldAnneeOfSemestresSemestre);
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

    public void edit(Annee annee) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Annee persistentAnnee = em.find(Annee.class, annee.getId());
            Departement departementOld = persistentAnnee.getDepartement();
            Departement departementNew = annee.getDepartement();
            Collection<Semestre> semestresOld = persistentAnnee.getSemestres();
            Collection<Semestre> semestresNew = annee.getSemestres();
            List<String> illegalOrphanMessages = null;
            for (Semestre semestresOldSemestre : semestresOld) {
                if (!semestresNew.contains(semestresOldSemestre)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Semestre " + semestresOldSemestre + " since its annee field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departementNew != null) {
                departementNew = em.getReference(departementNew.getClass(), departementNew.getId());
                annee.setDepartement(departementNew);
            }
            Collection<Semestre> attachedSemestresNew = new ArrayList<Semestre>();
            for (Semestre semestresNewSemestreToAttach : semestresNew) {
                semestresNewSemestreToAttach = em.getReference(semestresNewSemestreToAttach.getClass(), semestresNewSemestreToAttach.getId());
                attachedSemestresNew.add(semestresNewSemestreToAttach);
            }
            semestresNew = attachedSemestresNew;
            annee.setSemestres(semestresNew);
            annee = em.merge(annee);
            if (departementOld != null && !departementOld.equals(departementNew)) {
                departementOld.getAnnees().remove(annee);
                departementOld = em.merge(departementOld);
            }
            if (departementNew != null && !departementNew.equals(departementOld)) {
                departementNew.getAnnees().add(annee);
                departementNew = em.merge(departementNew);
            }
            for (Semestre semestresNewSemestre : semestresNew) {
                if (!semestresOld.contains(semestresNewSemestre)) {
                    Annee oldAnneeOfSemestresNewSemestre = semestresNewSemestre.getAnnee();
                    semestresNewSemestre.setAnnee(annee);
                    semestresNewSemestre = em.merge(semestresNewSemestre);
                    if (oldAnneeOfSemestresNewSemestre != null && !oldAnneeOfSemestresNewSemestre.equals(annee)) {
                        oldAnneeOfSemestresNewSemestre.getSemestres().remove(semestresNewSemestre);
                        oldAnneeOfSemestresNewSemestre = em.merge(oldAnneeOfSemestresNewSemestre);
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
                Long id = annee.getId();
                if (findAnnee(id) == null) {
                    throw new NonexistentEntityException("The annee with id " + id + " no longer exists.");
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
            Annee annee;
            try {
                annee = em.getReference(Annee.class, id);
                annee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The annee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Semestre> semestresOrphanCheck = annee.getSemestres();
            for (Semestre semestresOrphanCheckSemestre : semestresOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Annee (" + annee + ") cannot be destroyed since the Semestre " + semestresOrphanCheckSemestre + " in its semestres field has a non-nullable annee field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departement departement = annee.getDepartement();
            if (departement != null) {
                departement.getAnnees().remove(annee);
                departement = em.merge(departement);
            }
            em.remove(annee);
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

    public List<Annee> findAnneeEntities() {
        return findAnneeEntities(true, -1, -1);
    }

    public List<Annee> findAnneeEntities(int maxResults, int firstResult) {
        return findAnneeEntities(false, maxResults, firstResult);
    }

    private List<Annee> findAnneeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Annee.class));
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

    public Annee findAnnee(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Annee.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnneeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Annee> rt = cq.from(Annee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
