/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.HashMap;
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
        HashMap<Long, Etudiant> oEtuds = new HashMap<Long, Etudiant>();
        HashMap<Long, GroupeEvent> oEvents = new HashMap<Long, GroupeEvent>();
        Query q = em.createNamedQuery("EtudiantEvent.findByEtudiantGroupeEventGenre", EtudiantEvent.class);
        for (EtudiantEvent entity : oList) {
            if (entity != null) {
                EtudiantEvent p = null;
                Long nId = entity.getId();
                if ((nId != null) && (nId.longValue() != 0)) {
                    p = em.find(EtudiantEvent.class, nId);
                    if (p != null) {
                        if (bDelete) {
                            this.remove(p);
                        } else {
                            this.edit(entity);
                        }
                        continue;
                    }
                }
                Long nEtudId = entity.getEtudiant().getId();
                Long nevtid = entity.getGroupevent().getId();
                String genre = entity.getGenre();
                if ((nEtudId != null) && (nevtid != null) && (genre != null)
                        && (nEtudId.longValue() != 0) && (nevtid.longValue() != 0)) {
                    q.setParameter("etudiantid", nEtudId);
                    q.setParameter("evtid", nevtid);
                    q.setParameter("genre", genre);
                    List<EtudiantEvent> ylist = q.getResultList();
                    if (!ylist.isEmpty()) {
                        p = ylist.get(0);
                        if (bDelete) {
                            super.remove(p);
                        } else {
                            p.setNote(entity.getNote());
                            p.setObservations(entity.getObservations());
                            this.edit(p);
                        }
                    } else if (!bDelete) {
                        if (!oEtuds.containsKey(nEtudId)) {
                            Etudiant px = em.find(Etudiant.class, nEtudId);
                            if (px == null) {
                                continue;
                            }
                            oEtuds.put(nEtudId, px);
                        }
                        if (!oEvents.containsKey(nevtid)) {
                            GroupeEvent px = em.find(GroupeEvent.class, nevtid);
                            if (px == null) {
                                continue;
                            }
                            oEvents.put(nevtid, px);
                        }
                        Etudiant pEtud = oEtuds.get(nEtudId);
                        GroupeEvent pEvt = oEvents.get(nevtid);
                        entity.setId(null);
                        entity.setEtudiant(pEtud);
                        entity.setGroupevent(pEvt);
                        super.create(entity);
                    }
                }
            }// entity
        }// entity
    }// maintains
}
