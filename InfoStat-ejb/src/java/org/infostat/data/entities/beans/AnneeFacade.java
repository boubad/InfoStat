/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.Departement;

/**
 *
 * @author boubad
 */
@Stateless
public class AnneeFacade extends AbstractFacade<Annee> implements AnneeFacadeLocal {
    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnneeFacade() {
        super(Annee.class);
    }

    @Override
    public void create(Annee entity) {
        EntityManager em = getEntityManager();
        Departement dep =em.getReference(Departement.class, entity.getDepartement().getId());
        entity.setDepartement(dep);
        em.persist(entity);
    }
    
    public List<Annee> findAnneesByDepartement(Long depId){
        EntityManager e = getEntityManager();
        Query q = e.createNamedQuery("Annee.findByDepartement");
        q.setParameter("id", depId);
        return q.getResultList();
    }// findAnneesByDepartement
}
