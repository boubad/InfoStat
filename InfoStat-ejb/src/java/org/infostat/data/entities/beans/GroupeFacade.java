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

    @Override
    public void maintains(List<Groupe> oList, boolean bDelete) {
        if (oList == null) {
            return;
        }
        HashMap<Long, Departement> oDeps = new HashMap<Long, Departement>();
        Query q = em.createNamedQuery("Groupe.findByDepartementSigle", Groupe.class);
        for (Groupe entity : oList) {
            if (entity != null) {
                Groupe p = null;
                Long nId = entity.getId();
                if ((nId != null) && (nId.longValue() != 0)) {
                    p = em.find(Groupe.class, nId);
                    if (p != null) {
                        if (bDelete) {
                            super.remove(p);
                        } else {
                            this.edit(entity);
                        }
                        continue;
                    }
                }
                if (entity.getDepartement() == null) {
                    continue;
                }
                Long nDepId = entity.getDepartement().getId();
                if (nDepId == null) {
                    continue;
                }
                if (!oDeps.containsKey(nDepId)) {
                    Departement px = em.find(Departement.class, nDepId);
                    if (px == null) {
                        continue;
                    }
                    oDeps.put(nDepId, px);
                }
                String sigle = entity.getSigle();
                if (sigle == null) {
                    continue;
                }
                q.setParameter("id", nDepId);
                q.setParameter("sigle", sigle);
                List<Groupe> ylist = q.getResultList();
                if (!ylist.isEmpty()) {
                    p = ylist.get(0);
                    if (bDelete) {
                        this.remove(p);
                    } else {
                        entity.setId(p.getId());
                        this.edit(entity);
                    }
                } else if (!bDelete) {
                    Departement dep = oDeps.get(nDepId);
                    entity.setDepartement(dep);
                    entity.setId(null);
                    this.create(entity);
                }
            }// entity
        }// entity
    }// maintains
}
