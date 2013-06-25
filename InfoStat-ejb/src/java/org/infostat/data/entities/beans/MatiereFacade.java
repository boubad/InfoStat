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
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Unite;

/**
 *
 * @author boubad
 */
@Stateless
public class MatiereFacade extends AbstractFacade<Matiere> implements MatiereFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MatiereFacade() {
        super(Matiere.class);
    }

    @Override
    public void create(Matiere entity) {
        Unite un = em.find(Unite.class, entity.getUnite().getId());
        entity.setUnite(un);
        entity.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        super.create(entity);
    }

    @Override
    public void edit(Matiere entity) {
        Matiere p = em.find(Matiere.class, new Long(entity.getId().longValue()));
        p.setDescription(entity.getDescription());
        p.setNom(entity.getNom());
        p.setOrder(entity.getOrder());
        p.setSigle(entity.getSigle());
        p.setStatus(entity.getStatus());
        p.setCoefficient(entity.getCoefficient());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Matiere entity) {
        Matiere p = em.find(Matiere.class, new Long(entity.getId().longValue()));
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Matiere> findByDepartementId(Integer depId) {
        Query q = em.createNamedQuery("Matiere.findByDepartement", Matiere.class);
        q.setParameter("id", depId);
        return q.getResultList();
    }

    @Override
    public List<Matiere> findByUniteSigle(Integer uniteId, String sigle) {
        Query q = em.createNamedQuery("Matiere.findByUniteSigle", Matiere.class);
        q.setParameter("id", uniteId);
        q.setParameter("sigle", sigle);
        return q.getResultList();
    }

    @Override
    public List<Matiere> findByUniteId(Integer uniteId) {
        Query q = em.createNamedQuery("Matiere.findByUnite", Matiere.class);
        q.setParameter("id", uniteId);
        return q.getResultList();
    }
}
