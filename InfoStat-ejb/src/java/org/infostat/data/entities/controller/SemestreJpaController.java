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
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.AffectationEnseignant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.Semestre;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class SemestreJpaController implements Serializable {

    public SemestreJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Semestre semestre) throws RollbackFailureException, Exception {
        if (semestre.getAffectationenseignants() == null) {
            semestre.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        }
        if (semestre.getAffectationetudiants() == null) {
            semestre.setAffectationetudiants(new ArrayList<AffectationEtudiant>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Annee annee = semestre.getAnnee();
            if (annee != null) {
                annee = em.getReference(annee.getClass(), annee.getId());
                semestre.setAnnee(annee);
            }
            Collection<AffectationEnseignant> attachedAffectationenseignants = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsAffectationEnseignantToAttach : semestre.getAffectationenseignants()) {
                affectationenseignantsAffectationEnseignantToAttach = em.getReference(affectationenseignantsAffectationEnseignantToAttach.getClass(), affectationenseignantsAffectationEnseignantToAttach.getId());
                attachedAffectationenseignants.add(affectationenseignantsAffectationEnseignantToAttach);
            }
            semestre.setAffectationenseignants(attachedAffectationenseignants);
            Collection<AffectationEtudiant> attachedAffectationetudiants = new ArrayList<AffectationEtudiant>();
            for (AffectationEtudiant affectationetudiantsAffectationEtudiantToAttach : semestre.getAffectationetudiants()) {
                affectationetudiantsAffectationEtudiantToAttach = em.getReference(affectationetudiantsAffectationEtudiantToAttach.getClass(), affectationetudiantsAffectationEtudiantToAttach.getId());
                attachedAffectationetudiants.add(affectationetudiantsAffectationEtudiantToAttach);
            }
            semestre.setAffectationetudiants(attachedAffectationetudiants);
            em.persist(semestre);
            if (annee != null) {
                annee.getSemestres().add(semestre);
                annee = em.merge(annee);
            }
            for (AffectationEnseignant affectationenseignantsAffectationEnseignant : semestre.getAffectationenseignants()) {
                Semestre oldSemestreOfAffectationenseignantsAffectationEnseignant = affectationenseignantsAffectationEnseignant.getSemestre();
                affectationenseignantsAffectationEnseignant.setSemestre(semestre);
                affectationenseignantsAffectationEnseignant = em.merge(affectationenseignantsAffectationEnseignant);
                if (oldSemestreOfAffectationenseignantsAffectationEnseignant != null) {
                    oldSemestreOfAffectationenseignantsAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsAffectationEnseignant);
                    oldSemestreOfAffectationenseignantsAffectationEnseignant = em.merge(oldSemestreOfAffectationenseignantsAffectationEnseignant);
                }
            }
            for (AffectationEtudiant affectationetudiantsAffectationEtudiant : semestre.getAffectationetudiants()) {
                Semestre oldSemestreOfAffectationetudiantsAffectationEtudiant = affectationetudiantsAffectationEtudiant.getSemestre();
                affectationetudiantsAffectationEtudiant.setSemestre(semestre);
                affectationetudiantsAffectationEtudiant = em.merge(affectationetudiantsAffectationEtudiant);
                if (oldSemestreOfAffectationetudiantsAffectationEtudiant != null) {
                    oldSemestreOfAffectationetudiantsAffectationEtudiant.getAffectationetudiants().remove(affectationetudiantsAffectationEtudiant);
                    oldSemestreOfAffectationetudiantsAffectationEtudiant = em.merge(oldSemestreOfAffectationetudiantsAffectationEtudiant);
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

    public void edit(Semestre semestre) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Semestre persistentSemestre = em.find(Semestre.class, semestre.getId());
            Annee anneeOld = persistentSemestre.getAnnee();
            Annee anneeNew = semestre.getAnnee();
            Collection<AffectationEnseignant> affectationenseignantsOld = persistentSemestre.getAffectationenseignants();
            Collection<AffectationEnseignant> affectationenseignantsNew = semestre.getAffectationenseignants();
            Collection<AffectationEtudiant> affectationetudiantsOld = persistentSemestre.getAffectationetudiants();
            Collection<AffectationEtudiant> affectationetudiantsNew = semestre.getAffectationetudiants();
            List<String> illegalOrphanMessages = null;
            for (AffectationEnseignant affectationenseignantsOldAffectationEnseignant : affectationenseignantsOld) {
                if (!affectationenseignantsNew.contains(affectationenseignantsOldAffectationEnseignant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AffectationEnseignant " + affectationenseignantsOldAffectationEnseignant + " since its semestre field is not nullable.");
                }
            }
            for (AffectationEtudiant affectationetudiantsOldAffectationEtudiant : affectationetudiantsOld) {
                if (!affectationetudiantsNew.contains(affectationetudiantsOldAffectationEtudiant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AffectationEtudiant " + affectationetudiantsOldAffectationEtudiant + " since its semestre field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (anneeNew != null) {
                anneeNew = em.getReference(anneeNew.getClass(), anneeNew.getId());
                semestre.setAnnee(anneeNew);
            }
            Collection<AffectationEnseignant> attachedAffectationenseignantsNew = new ArrayList<AffectationEnseignant>();
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignantToAttach : affectationenseignantsNew) {
                affectationenseignantsNewAffectationEnseignantToAttach = em.getReference(affectationenseignantsNewAffectationEnseignantToAttach.getClass(), affectationenseignantsNewAffectationEnseignantToAttach.getId());
                attachedAffectationenseignantsNew.add(affectationenseignantsNewAffectationEnseignantToAttach);
            }
            affectationenseignantsNew = attachedAffectationenseignantsNew;
            semestre.setAffectationenseignants(affectationenseignantsNew);
            Collection<AffectationEtudiant> attachedAffectationetudiantsNew = new ArrayList<AffectationEtudiant>();
            for (AffectationEtudiant affectationetudiantsNewAffectationEtudiantToAttach : affectationetudiantsNew) {
                affectationetudiantsNewAffectationEtudiantToAttach = em.getReference(affectationetudiantsNewAffectationEtudiantToAttach.getClass(), affectationetudiantsNewAffectationEtudiantToAttach.getId());
                attachedAffectationetudiantsNew.add(affectationetudiantsNewAffectationEtudiantToAttach);
            }
            affectationetudiantsNew = attachedAffectationetudiantsNew;
            semestre.setAffectationetudiants(affectationetudiantsNew);
            semestre = em.merge(semestre);
            if (anneeOld != null && !anneeOld.equals(anneeNew)) {
                anneeOld.getSemestres().remove(semestre);
                anneeOld = em.merge(anneeOld);
            }
            if (anneeNew != null && !anneeNew.equals(anneeOld)) {
                anneeNew.getSemestres().add(semestre);
                anneeNew = em.merge(anneeNew);
            }
            for (AffectationEnseignant affectationenseignantsNewAffectationEnseignant : affectationenseignantsNew) {
                if (!affectationenseignantsOld.contains(affectationenseignantsNewAffectationEnseignant)) {
                    Semestre oldSemestreOfAffectationenseignantsNewAffectationEnseignant = affectationenseignantsNewAffectationEnseignant.getSemestre();
                    affectationenseignantsNewAffectationEnseignant.setSemestre(semestre);
                    affectationenseignantsNewAffectationEnseignant = em.merge(affectationenseignantsNewAffectationEnseignant);
                    if (oldSemestreOfAffectationenseignantsNewAffectationEnseignant != null && !oldSemestreOfAffectationenseignantsNewAffectationEnseignant.equals(semestre)) {
                        oldSemestreOfAffectationenseignantsNewAffectationEnseignant.getAffectationenseignants().remove(affectationenseignantsNewAffectationEnseignant);
                        oldSemestreOfAffectationenseignantsNewAffectationEnseignant = em.merge(oldSemestreOfAffectationenseignantsNewAffectationEnseignant);
                    }
                }
            }
            for (AffectationEtudiant affectationetudiantsNewAffectationEtudiant : affectationetudiantsNew) {
                if (!affectationetudiantsOld.contains(affectationetudiantsNewAffectationEtudiant)) {
                    Semestre oldSemestreOfAffectationetudiantsNewAffectationEtudiant = affectationetudiantsNewAffectationEtudiant.getSemestre();
                    affectationetudiantsNewAffectationEtudiant.setSemestre(semestre);
                    affectationetudiantsNewAffectationEtudiant = em.merge(affectationetudiantsNewAffectationEtudiant);
                    if (oldSemestreOfAffectationetudiantsNewAffectationEtudiant != null && !oldSemestreOfAffectationetudiantsNewAffectationEtudiant.equals(semestre)) {
                        oldSemestreOfAffectationetudiantsNewAffectationEtudiant.getAffectationetudiants().remove(affectationetudiantsNewAffectationEtudiant);
                        oldSemestreOfAffectationetudiantsNewAffectationEtudiant = em.merge(oldSemestreOfAffectationetudiantsNewAffectationEtudiant);
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
                Long id = semestre.getId();
                if (findSemestre(id) == null) {
                    throw new NonexistentEntityException("The semestre with id " + id + " no longer exists.");
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
            Semestre semestre;
            try {
                semestre = em.getReference(Semestre.class, id);
                semestre.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The semestre with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AffectationEnseignant> affectationenseignantsOrphanCheck = semestre.getAffectationenseignants();
            for (AffectationEnseignant affectationenseignantsOrphanCheckAffectationEnseignant : affectationenseignantsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semestre (" + semestre + ") cannot be destroyed since the AffectationEnseignant " + affectationenseignantsOrphanCheckAffectationEnseignant + " in its affectationenseignants field has a non-nullable semestre field.");
            }
            Collection<AffectationEtudiant> affectationetudiantsOrphanCheck = semestre.getAffectationetudiants();
            for (AffectationEtudiant affectationetudiantsOrphanCheckAffectationEtudiant : affectationetudiantsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semestre (" + semestre + ") cannot be destroyed since the AffectationEtudiant " + affectationetudiantsOrphanCheckAffectationEtudiant + " in its affectationetudiants field has a non-nullable semestre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Annee annee = semestre.getAnnee();
            if (annee != null) {
                annee.getSemestres().remove(semestre);
                annee = em.merge(annee);
            }
            em.remove(semestre);
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

    public List<Semestre> findSemestreEntities() {
        return findSemestreEntities(true, -1, -1);
    }

    public List<Semestre> findSemestreEntities(int maxResults, int firstResult) {
        return findSemestreEntities(false, maxResults, firstResult);
    }

    private List<Semestre> findSemestreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Semestre.class));
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

    public Semestre findSemestre(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Semestre.class, id);
        } finally {
            em.close();
        }
    }

    public int getSemestreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Semestre> rt = cq.from(Semestre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
