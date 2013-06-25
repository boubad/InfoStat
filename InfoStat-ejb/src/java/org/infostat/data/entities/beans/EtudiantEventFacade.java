/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.EtudiantEvent;
import org.infostat.data.entities.GroupeEvent;

/**
 *
 * @author boubad
 */
@Stateless
public class EtudiantEventFacade extends AbstractFacade<EtudiantEvent> implements EtudiantEventFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtudiantEventFacade() {
        super(EtudiantEvent.class);
    }

    @Override
    public void create(EtudiantEvent entity) {
        GroupeEvent evt = em.find(GroupeEvent.class, entity.getGroupevent().getId());
        entity.setGroupevent(evt);
        Etudiant etud = em.find(Etudiant.class, entity.getEtudiant().getId());
        entity.setEtudiant(etud);
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(EtudiantEvent entity) {
        EtudiantEvent p = em.find(EtudiantEvent.class, entity.getId());
        p.setGenre(entity.getGenre());
        p.setNote(entity.getNote());
        p.setObservations(entity.getObservations());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(EtudiantEvent entity) {
        EtudiantEvent p = em.find(EtudiantEvent.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EtudiantEvent> findByGroupeEvent(Integer evtid) {
        Query q = em.createNamedQuery("EtudiantEvent.findByGroupeEvent", EtudiantEvent.class);
        q.setParameter("evtid", evtid);
        return q.getResultList();
    }

    @Override
    public List<EtudiantEvent> findBySemestreGenre(Integer semestreid, String genre) {
        Query q = em.createNamedQuery("EtudiantEvent.findBySemestreGenre", EtudiantEvent.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("genre", genre);
        return q.getResultList();
    }

    @Override
    public List<EtudiantEvent> findByEtudiant(Integer etudiantid) {
        Query q = em.createNamedQuery("EtudiantEvent.findByEtudiant", EtudiantEvent.class);
        q.setParameter("etudiantid", etudiantid);
        return q.getResultList();
    }

    @Override
    public List<EtudiantEvent> findByEtudiantGroupeEventGenre(Integer etudiantid, Integer evtid, String genre) {
        Query q = em.createNamedQuery("EtudiantEvent.findByEtudiantGroupeEventGenre", EtudiantEvent.class);
        q.setParameter("etudiantid", etudiantid);
        q.setParameter("evtid", evtid);
        q.setParameter("genre", genre);
        return q.getResultList();
    }

    @Override
    public void maintains(List<EtudiantEvent> oList, boolean bDelete) {
        if (oList == null) {
            return;
        }
        for (EtudiantEvent entity : oList) {
            if (entity != null) {
                EtudiantEvent p = null;
                Long nId = entity.getId();
                if (nId != null) {
                    p = em.find(EtudiantEvent.class, nId);
                }
                if (p != null) {
                    if (bDelete) {
                        super.edit(entity);
                    } else {
                        super.remove(entity);
                    }
                } else {
                    if (!bDelete) {
                        super.create(entity);
                    }
                }
            }// entity
        }// entity
    }// maintains
}
