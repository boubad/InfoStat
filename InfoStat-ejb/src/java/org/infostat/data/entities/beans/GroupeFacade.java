/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.infostat.data.entities.AffectationEnseignant;
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.Groupe;

/**
 *
 * @author boubad
 */
@Stateless
public class GroupeFacade extends AbstractFacade<Groupe> implements GroupeFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupeFacade() {
        super(Groupe.class);
    }

    @Override
    public void create(Groupe entity) {
        Departement dep = em.find(Departement.class, 
                new Long(entity.getDepartement().getId().longValue()));
        entity.setAffectationEtudiants(new ArrayList<AffectationEtudiant>());
        entity.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        entity.setDepartement(dep);
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Groupe entity) {
        Groupe p = em.find(Groupe.class, new Long(entity.getId().longValue()));
        p.setDescription(entity.getDescription());
        p.setNom(entity.getNom());
        p.setSigle(entity.getSigle());
        p.setStatus(entity.getStatus());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Groupe entity) {
        Groupe p = em.find(Groupe.class, new Long(entity.getId().longValue()));
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Groupe> findByDepartementId(Integer depId) {
        Query q = em.createNamedQuery("Groupe.findByDepartement", Groupe.class);
        q.setParameter("id", depId);
        return q.getResultList();
    }

    @Override
    public List<Groupe> findByDepartementSigle(Integer depId, String sigle) {
        Query q = em.createNamedQuery("Groupe.findByDepartementSigle", Groupe.class);
        q.setParameter("id", depId);
        q.setParameter("sigle", sigle);
        return q.getResultList();
    }
}
