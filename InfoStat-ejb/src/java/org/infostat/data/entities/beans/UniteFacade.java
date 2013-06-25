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
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Unite;

/**
 *
 * @author boubad
 */
@Stateless
public class UniteFacade extends AbstractFacade<Unite> implements UniteFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UniteFacade() {
        super(Unite.class);
    }

    @Override
    public void create(Unite entity) {
        Departement dep = em.find(Departement.class, new Long(entity.getDepartement().getId().longValue()));
        entity.setDepartement(dep);
        entity.setMatieres(new ArrayList<Matiere>());
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Unite entity) {
        Unite p = em.find(Unite.class, new Long(entity.getId().longValue()));
        p.setDescription(entity.getDescription());
        p.setNom(entity.getNom());
        p.setOrder(entity.getOrder());
        p.setSigle(entity.getSigle());
        p.setStatus(entity.getStatus());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Unite entity) {
        Unite p = em.find(Unite.class, new Long(entity.getId().longValue()));
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Unite> findByDepartementId(Integer depId) {
        Query q = em.createNamedQuery("Unite.findByDepartement", Unite.class);
        q.setParameter("id", depId);
        return q.getResultList();
    }

    @Override
    public List<Unite> findByDepartementSigle(Integer depId, String sigle) {
        Query q = em.createNamedQuery("Unite.findByDepartementSigle", Unite.class);
        q.setParameter("id", depId);
        q.setParameter("sigle", sigle);
        return q.getResultList();
    }
}
