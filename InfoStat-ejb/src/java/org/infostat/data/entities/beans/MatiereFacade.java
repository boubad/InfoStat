/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.infostat.data.entities.AffectationEnseignant;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Module;
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
        entity.setModules(new ArrayList<Module>());
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

    @Override
    public void maintains(List<Matiere> oList, boolean bDelete) {
        if (oList == null) {
            return;
        }
        HashMap<Long, Unite> oUnites = new HashMap<Long, Unite>();
        Query q = em.createNamedQuery("Matiere.findByUniteSigle", Matiere.class);
        for (Matiere entity : oList) {
            if (entity != null) {
                Matiere p = null;
                Long nId = entity.getId();
                if ((nId != null) && (nId.longValue() != 0)) {
                    p = em.find(Matiere.class, nId);
                    if (p != null) {
                        if (bDelete) {
                            super.remove(p);
                        } else {
                            this.edit(entity);
                        }
                        continue;
                    }
                }
                if (entity.getUnite() == null) {
                    continue;
                }
                Long nUniteId = entity.getUnite().getId();
                if (nUniteId == null) {
                    continue;
                }
                if (!oUnites.containsKey(nUniteId)) {
                    Unite px = em.find(Unite.class, nUniteId);
                    if (px == null) {
                        continue;
                    }
                    oUnites.put(nUniteId, px);
                }
                String sigle = entity.getSigle();
                if (sigle == null) {
                    continue;
                }
                q.setParameter("id", nUniteId);
                q.setParameter("sigle", sigle);
                List<Matiere> ylist = q.getResultList();
                if (!ylist.isEmpty()) {
                    p = ylist.get(0);
                    if (bDelete) {
                        this.remove(p);
                    } else {
                        entity.setId(p.getId());
                        this.edit(entity);
                    }
                } else if (!bDelete) {
                    Unite unite = oUnites.get(nUniteId);
                    entity.setUnite(unite);
                    entity.setId(null);
                    this.create(entity);
                }
            }// entity
        }// entity
    }
}
