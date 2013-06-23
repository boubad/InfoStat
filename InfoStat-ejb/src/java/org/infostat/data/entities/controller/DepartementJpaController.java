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
import org.infostat.data.entities.Groupe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.infostat.data.entities.Unite;
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.controller.exceptions.IllegalOrphanException;
import org.infostat.data.entities.controller.exceptions.NonexistentEntityException;
import org.infostat.data.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author boubad
 */
public class DepartementJpaController implements Serializable {

    public DepartementJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departement departement) throws RollbackFailureException, Exception {
        if (departement.getGroupes() == null) {
            departement.setGroupes(new ArrayList<Groupe>());
        }
        if (departement.getUnites() == null) {
            departement.setUnites(new ArrayList<Unite>());
        }
        if (departement.getAnnees() == null) {
            departement.setAnnees(new ArrayList<Annee>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Groupe> attachedGroupes = new ArrayList<Groupe>();
            for (Groupe groupesGroupeToAttach : departement.getGroupes()) {
                groupesGroupeToAttach = em.getReference(groupesGroupeToAttach.getClass(), groupesGroupeToAttach.getId());
                attachedGroupes.add(groupesGroupeToAttach);
            }
            departement.setGroupes(attachedGroupes);
            Collection<Unite> attachedUnites = new ArrayList<Unite>();
            for (Unite unitesUniteToAttach : departement.getUnites()) {
                unitesUniteToAttach = em.getReference(unitesUniteToAttach.getClass(), unitesUniteToAttach.getId());
                attachedUnites.add(unitesUniteToAttach);
            }
            departement.setUnites(attachedUnites);
            Collection<Annee> attachedAnnees = new ArrayList<Annee>();
            for (Annee anneesAnneeToAttach : departement.getAnnees()) {
                anneesAnneeToAttach = em.getReference(anneesAnneeToAttach.getClass(), anneesAnneeToAttach.getId());
                attachedAnnees.add(anneesAnneeToAttach);
            }
            departement.setAnnees(attachedAnnees);
            em.persist(departement);
            for (Groupe groupesGroupe : departement.getGroupes()) {
                Departement oldDepartementOfGroupesGroupe = groupesGroupe.getDepartement();
                groupesGroupe.setDepartement(departement);
                groupesGroupe = em.merge(groupesGroupe);
                if (oldDepartementOfGroupesGroupe != null) {
                    oldDepartementOfGroupesGroupe.getGroupes().remove(groupesGroupe);
                    oldDepartementOfGroupesGroupe = em.merge(oldDepartementOfGroupesGroupe);
                }
            }
            for (Unite unitesUnite : departement.getUnites()) {
                Departement oldDepartementOfUnitesUnite = unitesUnite.getDepartement();
                unitesUnite.setDepartement(departement);
                unitesUnite = em.merge(unitesUnite);
                if (oldDepartementOfUnitesUnite != null) {
                    oldDepartementOfUnitesUnite.getUnites().remove(unitesUnite);
                    oldDepartementOfUnitesUnite = em.merge(oldDepartementOfUnitesUnite);
                }
            }
            for (Annee anneesAnnee : departement.getAnnees()) {
                Departement oldDepartementOfAnneesAnnee = anneesAnnee.getDepartement();
                anneesAnnee.setDepartement(departement);
                anneesAnnee = em.merge(anneesAnnee);
                if (oldDepartementOfAnneesAnnee != null) {
                    oldDepartementOfAnneesAnnee.getAnnees().remove(anneesAnnee);
                    oldDepartementOfAnneesAnnee = em.merge(oldDepartementOfAnneesAnnee);
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

    public void edit(Departement departement) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departement persistentDepartement = em.find(Departement.class, departement.getId());
            Collection<Groupe> groupesOld = persistentDepartement.getGroupes();
            Collection<Groupe> groupesNew = departement.getGroupes();
            Collection<Unite> unitesOld = persistentDepartement.getUnites();
            Collection<Unite> unitesNew = departement.getUnites();
            Collection<Annee> anneesOld = persistentDepartement.getAnnees();
            Collection<Annee> anneesNew = departement.getAnnees();
            List<String> illegalOrphanMessages = null;
            for (Groupe groupesOldGroupe : groupesOld) {
                if (!groupesNew.contains(groupesOldGroupe)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Groupe " + groupesOldGroupe + " since its departement field is not nullable.");
                }
            }
            for (Unite unitesOldUnite : unitesOld) {
                if (!unitesNew.contains(unitesOldUnite)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Unite " + unitesOldUnite + " since its departement field is not nullable.");
                }
            }
            for (Annee anneesOldAnnee : anneesOld) {
                if (!anneesNew.contains(anneesOldAnnee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Annee " + anneesOldAnnee + " since its departement field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Groupe> attachedGroupesNew = new ArrayList<Groupe>();
            for (Groupe groupesNewGroupeToAttach : groupesNew) {
                groupesNewGroupeToAttach = em.getReference(groupesNewGroupeToAttach.getClass(), groupesNewGroupeToAttach.getId());
                attachedGroupesNew.add(groupesNewGroupeToAttach);
            }
            groupesNew = attachedGroupesNew;
            departement.setGroupes(groupesNew);
            Collection<Unite> attachedUnitesNew = new ArrayList<Unite>();
            for (Unite unitesNewUniteToAttach : unitesNew) {
                unitesNewUniteToAttach = em.getReference(unitesNewUniteToAttach.getClass(), unitesNewUniteToAttach.getId());
                attachedUnitesNew.add(unitesNewUniteToAttach);
            }
            unitesNew = attachedUnitesNew;
            departement.setUnites(unitesNew);
            Collection<Annee> attachedAnneesNew = new ArrayList<Annee>();
            for (Annee anneesNewAnneeToAttach : anneesNew) {
                anneesNewAnneeToAttach = em.getReference(anneesNewAnneeToAttach.getClass(), anneesNewAnneeToAttach.getId());
                attachedAnneesNew.add(anneesNewAnneeToAttach);
            }
            anneesNew = attachedAnneesNew;
            departement.setAnnees(anneesNew);
            departement = em.merge(departement);
            for (Groupe groupesNewGroupe : groupesNew) {
                if (!groupesOld.contains(groupesNewGroupe)) {
                    Departement oldDepartementOfGroupesNewGroupe = groupesNewGroupe.getDepartement();
                    groupesNewGroupe.setDepartement(departement);
                    groupesNewGroupe = em.merge(groupesNewGroupe);
                    if (oldDepartementOfGroupesNewGroupe != null && !oldDepartementOfGroupesNewGroupe.equals(departement)) {
                        oldDepartementOfGroupesNewGroupe.getGroupes().remove(groupesNewGroupe);
                        oldDepartementOfGroupesNewGroupe = em.merge(oldDepartementOfGroupesNewGroupe);
                    }
                }
            }
            for (Unite unitesNewUnite : unitesNew) {
                if (!unitesOld.contains(unitesNewUnite)) {
                    Departement oldDepartementOfUnitesNewUnite = unitesNewUnite.getDepartement();
                    unitesNewUnite.setDepartement(departement);
                    unitesNewUnite = em.merge(unitesNewUnite);
                    if (oldDepartementOfUnitesNewUnite != null && !oldDepartementOfUnitesNewUnite.equals(departement)) {
                        oldDepartementOfUnitesNewUnite.getUnites().remove(unitesNewUnite);
                        oldDepartementOfUnitesNewUnite = em.merge(oldDepartementOfUnitesNewUnite);
                    }
                }
            }
            for (Annee anneesNewAnnee : anneesNew) {
                if (!anneesOld.contains(anneesNewAnnee)) {
                    Departement oldDepartementOfAnneesNewAnnee = anneesNewAnnee.getDepartement();
                    anneesNewAnnee.setDepartement(departement);
                    anneesNewAnnee = em.merge(anneesNewAnnee);
                    if (oldDepartementOfAnneesNewAnnee != null && !oldDepartementOfAnneesNewAnnee.equals(departement)) {
                        oldDepartementOfAnneesNewAnnee.getAnnees().remove(anneesNewAnnee);
                        oldDepartementOfAnneesNewAnnee = em.merge(oldDepartementOfAnneesNewAnnee);
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
                Long id = departement.getId();
                if (findDepartement(id) == null) {
                    throw new NonexistentEntityException("The departement with id " + id + " no longer exists.");
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
            Departement departement;
            try {
                departement = em.getReference(Departement.class, id);
                departement.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departement with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Groupe> groupesOrphanCheck = departement.getGroupes();
            for (Groupe groupesOrphanCheckGroupe : groupesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departement (" + departement + ") cannot be destroyed since the Groupe " + groupesOrphanCheckGroupe + " in its groupes field has a non-nullable departement field.");
            }
            Collection<Unite> unitesOrphanCheck = departement.getUnites();
            for (Unite unitesOrphanCheckUnite : unitesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departement (" + departement + ") cannot be destroyed since the Unite " + unitesOrphanCheckUnite + " in its unites field has a non-nullable departement field.");
            }
            Collection<Annee> anneesOrphanCheck = departement.getAnnees();
            for (Annee anneesOrphanCheckAnnee : anneesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departement (" + departement + ") cannot be destroyed since the Annee " + anneesOrphanCheckAnnee + " in its annees field has a non-nullable departement field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departement);
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

    public List<Departement> findDepartementEntities() {
        return findDepartementEntities(true, -1, -1);
    }

    public List<Departement> findDepartementEntities(int maxResults, int firstResult) {
        return findDepartementEntities(false, maxResults, firstResult);
    }

    private List<Departement> findDepartementEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departement.class));
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

    public Departement findDepartement(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departement.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartementCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departement> rt = cq.from(Departement.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
