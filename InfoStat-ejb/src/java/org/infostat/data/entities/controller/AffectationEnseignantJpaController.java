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
import org.infostat.data.entities.Semestre;
import org.infostat.data.entities.Enseignant;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.GroupeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.AffectationEnseignant;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class AffectationEnseignantJpaController implements Serializable {

    public AffectationEnseignantJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AffectationEnseignant affectationEnseignant) throws RollbackFailureException, Exception {
        if (affectationEnseignant.getGroupeevents() == null) {
            affectationEnseignant.setGroupeevents(new ArrayList<GroupeEvent>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Semestre semestre = affectationEnseignant.getSemestre();
            if (semestre != null) {
                semestre = em.getReference(semestre.getClass(), semestre.getId());
                affectationEnseignant.setSemestre(semestre);
            }
            Enseignant enseignant = affectationEnseignant.getEnseignant();
            if (enseignant != null) {
                enseignant = em.getReference(enseignant.getClass(), enseignant.getId());
                affectationEnseignant.setEnseignant(enseignant);
            }
            Matiere matiere = affectationEnseignant.getMatiere();
            if (matiere != null) {
                matiere = em.getReference(matiere.getClass(), matiere.getId());
                affectationEnseignant.setMatiere(matiere);
            }
            Groupe groupe = affectationEnseignant.getGroupe();
            if (groupe != null) {
                groupe = em.getReference(groupe.getClass(), groupe.getId());
                affectationEnseignant.setGroupe(groupe);
            }
            Collection<GroupeEvent> attachedGroupeevents = new ArrayList<GroupeEvent>();
            for (GroupeEvent groupeeventsGroupeEventToAttach : affectationEnseignant.getGroupeevents()) {
                groupeeventsGroupeEventToAttach = em.getReference(groupeeventsGroupeEventToAttach.getClass(), groupeeventsGroupeEventToAttach.getId());
                attachedGroupeevents.add(groupeeventsGroupeEventToAttach);
            }
            affectationEnseignant.setGroupeevents(attachedGroupeevents);
            em.persist(affectationEnseignant);
            if (semestre != null) {
                semestre.getAffectationenseignants().add(affectationEnseignant);
                semestre = em.merge(semestre);
            }
            if (enseignant != null) {
                enseignant.getAffectationenseignants().add(affectationEnseignant);
                enseignant = em.merge(enseignant);
            }
            if (matiere != null) {
                matiere.getAffectationenseignants().add(affectationEnseignant);
                matiere = em.merge(matiere);
            }
            if (groupe != null) {
                groupe.getAffectationenseignants().add(affectationEnseignant);
                groupe = em.merge(groupe);
            }
            for (GroupeEvent groupeeventsGroupeEvent : affectationEnseignant.getGroupeevents()) {
                AffectationEnseignant oldAffectationenseignantOfGroupeeventsGroupeEvent = groupeeventsGroupeEvent.getAffectationenseignant();
                groupeeventsGroupeEvent.setAffectationenseignant(affectationEnseignant);
                groupeeventsGroupeEvent = em.merge(groupeeventsGroupeEvent);
                if (oldAffectationenseignantOfGroupeeventsGroupeEvent != null) {
                    oldAffectationenseignantOfGroupeeventsGroupeEvent.getGroupeevents().remove(groupeeventsGroupeEvent);
                    oldAffectationenseignantOfGroupeeventsGroupeEvent = em.merge(oldAffectationenseignantOfGroupeeventsGroupeEvent);
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

    public void edit(AffectationEnseignant affectationEnseignant) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AffectationEnseignant persistentAffectationEnseignant = em.find(AffectationEnseignant.class, affectationEnseignant.getId());
            Semestre semestreOld = persistentAffectationEnseignant.getSemestre();
            Semestre semestreNew = affectationEnseignant.getSemestre();
            Enseignant enseignantOld = persistentAffectationEnseignant.getEnseignant();
            Enseignant enseignantNew = affectationEnseignant.getEnseignant();
            Matiere matiereOld = persistentAffectationEnseignant.getMatiere();
            Matiere matiereNew = affectationEnseignant.getMatiere();
            Groupe groupeOld = persistentAffectationEnseignant.getGroupe();
            Groupe groupeNew = affectationEnseignant.getGroupe();
            Collection<GroupeEvent> groupeeventsOld = persistentAffectationEnseignant.getGroupeevents();
            Collection<GroupeEvent> groupeeventsNew = affectationEnseignant.getGroupeevents();
            List<String> illegalOrphanMessages = null;
            for (GroupeEvent groupeeventsOldGroupeEvent : groupeeventsOld) {
                if (!groupeeventsNew.contains(groupeeventsOldGroupeEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GroupeEvent " + groupeeventsOldGroupeEvent + " since its affectationenseignant field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (semestreNew != null) {
                semestreNew = em.getReference(semestreNew.getClass(), semestreNew.getId());
                affectationEnseignant.setSemestre(semestreNew);
            }
            if (enseignantNew != null) {
                enseignantNew = em.getReference(enseignantNew.getClass(), enseignantNew.getId());
                affectationEnseignant.setEnseignant(enseignantNew);
            }
            if (matiereNew != null) {
                matiereNew = em.getReference(matiereNew.getClass(), matiereNew.getId());
                affectationEnseignant.setMatiere(matiereNew);
            }
            if (groupeNew != null) {
                groupeNew = em.getReference(groupeNew.getClass(), groupeNew.getId());
                affectationEnseignant.setGroupe(groupeNew);
            }
            Collection<GroupeEvent> attachedGroupeeventsNew = new ArrayList<GroupeEvent>();
            for (GroupeEvent groupeeventsNewGroupeEventToAttach : groupeeventsNew) {
                groupeeventsNewGroupeEventToAttach = em.getReference(groupeeventsNewGroupeEventToAttach.getClass(), groupeeventsNewGroupeEventToAttach.getId());
                attachedGroupeeventsNew.add(groupeeventsNewGroupeEventToAttach);
            }
            groupeeventsNew = attachedGroupeeventsNew;
            affectationEnseignant.setGroupeevents(groupeeventsNew);
            affectationEnseignant = em.merge(affectationEnseignant);
            if (semestreOld != null && !semestreOld.equals(semestreNew)) {
                semestreOld.getAffectationenseignants().remove(affectationEnseignant);
                semestreOld = em.merge(semestreOld);
            }
            if (semestreNew != null && !semestreNew.equals(semestreOld)) {
                semestreNew.getAffectationenseignants().add(affectationEnseignant);
                semestreNew = em.merge(semestreNew);
            }
            if (enseignantOld != null && !enseignantOld.equals(enseignantNew)) {
                enseignantOld.getAffectationenseignants().remove(affectationEnseignant);
                enseignantOld = em.merge(enseignantOld);
            }
            if (enseignantNew != null && !enseignantNew.equals(enseignantOld)) {
                enseignantNew.getAffectationenseignants().add(affectationEnseignant);
                enseignantNew = em.merge(enseignantNew);
            }
            if (matiereOld != null && !matiereOld.equals(matiereNew)) {
                matiereOld.getAffectationenseignants().remove(affectationEnseignant);
                matiereOld = em.merge(matiereOld);
            }
            if (matiereNew != null && !matiereNew.equals(matiereOld)) {
                matiereNew.getAffectationenseignants().add(affectationEnseignant);
                matiereNew = em.merge(matiereNew);
            }
            if (groupeOld != null && !groupeOld.equals(groupeNew)) {
                groupeOld.getAffectationenseignants().remove(affectationEnseignant);
                groupeOld = em.merge(groupeOld);
            }
            if (groupeNew != null && !groupeNew.equals(groupeOld)) {
                groupeNew.getAffectationenseignants().add(affectationEnseignant);
                groupeNew = em.merge(groupeNew);
            }
            for (GroupeEvent groupeeventsNewGroupeEvent : groupeeventsNew) {
                if (!groupeeventsOld.contains(groupeeventsNewGroupeEvent)) {
                    AffectationEnseignant oldAffectationenseignantOfGroupeeventsNewGroupeEvent = groupeeventsNewGroupeEvent.getAffectationenseignant();
                    groupeeventsNewGroupeEvent.setAffectationenseignant(affectationEnseignant);
                    groupeeventsNewGroupeEvent = em.merge(groupeeventsNewGroupeEvent);
                    if (oldAffectationenseignantOfGroupeeventsNewGroupeEvent != null && !oldAffectationenseignantOfGroupeeventsNewGroupeEvent.equals(affectationEnseignant)) {
                        oldAffectationenseignantOfGroupeeventsNewGroupeEvent.getGroupeevents().remove(groupeeventsNewGroupeEvent);
                        oldAffectationenseignantOfGroupeeventsNewGroupeEvent = em.merge(oldAffectationenseignantOfGroupeeventsNewGroupeEvent);
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
                Long id = affectationEnseignant.getId();
                if (findAffectationEnseignant(id) == null) {
                    throw new NonexistentEntityException("The affectationEnseignant with id " + id + " no longer exists.");
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
            AffectationEnseignant affectationEnseignant;
            try {
                affectationEnseignant = em.getReference(AffectationEnseignant.class, id);
                affectationEnseignant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The affectationEnseignant with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<GroupeEvent> groupeeventsOrphanCheck = affectationEnseignant.getGroupeevents();
            for (GroupeEvent groupeeventsOrphanCheckGroupeEvent : groupeeventsOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AffectationEnseignant (" + affectationEnseignant + ") cannot be destroyed since the GroupeEvent " + groupeeventsOrphanCheckGroupeEvent + " in its groupeevents field has a non-nullable affectationenseignant field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Semestre semestre = affectationEnseignant.getSemestre();
            if (semestre != null) {
                semestre.getAffectationenseignants().remove(affectationEnseignant);
                semestre = em.merge(semestre);
            }
            Enseignant enseignant = affectationEnseignant.getEnseignant();
            if (enseignant != null) {
                enseignant.getAffectationenseignants().remove(affectationEnseignant);
                enseignant = em.merge(enseignant);
            }
            Matiere matiere = affectationEnseignant.getMatiere();
            if (matiere != null) {
                matiere.getAffectationenseignants().remove(affectationEnseignant);
                matiere = em.merge(matiere);
            }
            Groupe groupe = affectationEnseignant.getGroupe();
            if (groupe != null) {
                groupe.getAffectationenseignants().remove(affectationEnseignant);
                groupe = em.merge(groupe);
            }
            em.remove(affectationEnseignant);
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

    public List<AffectationEnseignant> findAffectationEnseignantEntities() {
        return findAffectationEnseignantEntities(true, -1, -1);
    }

    public List<AffectationEnseignant> findAffectationEnseignantEntities(int maxResults, int firstResult) {
        return findAffectationEnseignantEntities(false, maxResults, firstResult);
    }

    private List<AffectationEnseignant> findAffectationEnseignantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AffectationEnseignant.class));
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

    public AffectationEnseignant findAffectationEnseignant(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AffectationEnseignant.class, id);
        } finally {
            em.close();
        }
    }

    public int getAffectationEnseignantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AffectationEnseignant> rt = cq.from(AffectationEnseignant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
