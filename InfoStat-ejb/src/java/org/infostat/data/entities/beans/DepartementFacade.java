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
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.Unite;

/**
 *
 * @author boubad
 */
@Stateless
public class DepartementFacade extends AbstractFacade<Departement> implements DepartementFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartementFacade() {
        super(Departement.class);
    }

    @Override
    public List<Departement> findBySigle(String sigle) {
        EntityManager e = getEntityManager();
        Query q = e.createNamedQuery("Departement.findBySigle");
        q.setParameter("sigle", sigle);
        return q.getResultList();
    }

    @Override
    public void create(Departement entity) {
        entity.setGroupes(new ArrayList<Groupe>());
        entity.setUnites(new ArrayList<Unite>());
        entity.setAnnees(new ArrayList<Annee>());
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Departement entity) {
        Departement p = em.find(Departement.class, new Long(entity.getId().longValue()));
        p.setSigle(entity.getSigle());
        p.setNom(entity.getNom());
        p.setStatus(entity.getStatus());
        p.setDescription(entity.getDescription());
        p.setLogodata(entity.getLogodata());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Departement entity) {
        Departement p = em.find(Departement.class, new Long(entity.getId().longValue()));
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }
}
