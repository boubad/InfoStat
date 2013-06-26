/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.infostat.data.entities.AffectationEnseignant;
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.EtudiantEvent;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.GroupeEvent;
import org.infostat.data.entities.Semestre;

/**
 *
 * @author boubad
 */
@Stateless
public class GroupeEventFacade extends AbstractFacade<GroupeEvent> implements GroupeEventFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupeEventFacade() {
        super(GroupeEvent.class);
    }

    @Override
    public void checkNotes(GroupeEvent entity) {
        Long nId = entity.getId();
        if (nId == null) {
            return;
        }
        if (nId.longValue() == 0) {
            return;
        }
        GroupeEvent evt = em.find(GroupeEvent.class, nId);
        if (evt == null){
            return;
        }
        String sGenre = "NOTE";
        Query qx = em.createNamedQuery("EtudiantEvent.findByEtudiantGroupeEventGenre", EtudiantEvent.class);
        AffectationEnseignant aff = evt.getAffectationenseignant();
        Semestre sem = aff.getSemestre();
        Groupe groupe = aff.getGroupe();
        Query q = em.createNamedQuery("AffectationEtudiant.findBySemestreGroupe", AffectationEtudiant.class);
        q.setParameter("semestreid", sem.getId());
        q.setParameter("groupeid", groupe.getId());
        List<AffectationEtudiant> oList = q.getResultList();
        for (AffectationEtudiant pAff : oList) {
            Etudiant etud = pAff.getEtudiant();
            List<EtudiantEvent> xList = new ArrayList<EtudiantEvent>();
            try {
                qx.setParameter("evtid", nId);
                qx.setParameter("etudiantid", etud.getId());
                qx.setParameter("genre", sGenre);
                xList = qx.getResultList();
            } catch (Exception ex) {
            }
            if (xList.isEmpty()) {
                EtudiantEvent pEvt = new EtudiantEvent();
                pEvt.setEtudiant(etud);
                pEvt.setGroupevent(evt);
                pEvt.setGenre(sGenre);
                em.persist(pEvt);
            }
        }// pAff
    }// checkNotes

    @Override
    public void remove(GroupeEvent entity) {
        GroupeEvent p = em.find(GroupeEvent.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(GroupeEvent groupeEvent, boolean bCreateNotes) {
        AffectationEnseignant aff = em.find(AffectationEnseignant.class, groupeEvent.getAffectationenseignant().getId());
        groupeEvent.setAffectationenseignant(aff);
        groupeEvent.setEtudiantevents(new ArrayList<EtudiantEvent>());
        Date d1 = groupeEvent.getDate();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(d1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        groupeEvent.setDate(cal.getTime());
        //
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(groupeEvent.getStarttime());
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        cal1.set(Calendar.YEAR, yy);
        cal1.set(Calendar.MONTH, mm);
        cal1.set(Calendar.DAY_OF_MONTH, dd);
        groupeEvent.setStarttime(cal1.getTime());
        //
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(groupeEvent.getEndtime());
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.YEAR, yy);
        cal2.set(Calendar.MONTH, mm);
        cal2.set(Calendar.DAY_OF_MONTH, dd);
        groupeEvent.setEndtime(cal2.getTime());
        super.create(groupeEvent);
        if (bCreateNotes) {
            checkNotes(groupeEvent);
        }
    }

    @Override
    public void edit(GroupeEvent entity, boolean bCreateNotes) {
        GroupeEvent p = em.find(GroupeEvent.class, entity.getId());
        p.setCoefficient(entity.getCoefficient());
        Date d1 = entity.getDate();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(d1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        p.setDate(cal.getTime());
        p.setDescription(entity.getDescription());
        p.setGenre(entity.getGenre());
        p.setLocation(entity.getLocation());
        p.setNom(entity.getNom());
        p.setStatus(entity.getStatus());
        //
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(entity.getStarttime());
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        cal1.set(Calendar.YEAR, yy);
        cal1.set(Calendar.MONTH, mm);
        cal1.set(Calendar.DAY_OF_MONTH, dd);
        p.setStarttime(cal1.getTime());
        //
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(entity.getEndtime());
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.YEAR, yy);
        cal2.set(Calendar.MONTH, mm);
        cal2.set(Calendar.DAY_OF_MONTH, dd);
        p.setEndtime(cal2.getTime());
        //
        super.edit(p);
        if (bCreateNotes) {
            checkNotes(p);
        }
    }

    @Override
    public List<GroupeEvent> findBySemestre(Integer semestreid) {
        Query q = em.createNamedQuery("GroupeEvent.findBySemestre", GroupeEvent.class);
        q.setParameter("semestreid", semestreid);
        return q.getResultList();
    }

    @Override
    public List<GroupeEvent> findBySemestreEnseignant(Integer semestreid, Integer enseignantid) {
        Query q = em.createNamedQuery("GroupeEvent.findBySemestreEnseignant", GroupeEvent.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("enseignantid", enseignantid);
        return q.getResultList();
    }

    @Override
    public List<GroupeEvent> findBySemestreEnseignantMatiere(Integer semestreid, Integer enseignantid, Integer matiereid) {
        Query q = em.createNamedQuery("GroupeEvent.findBySemestreEnseignantMatiere", GroupeEvent.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("enseignantid", enseignantid);
        q.setParameter("matiereid", matiereid);
        return q.getResultList();
    }

    @Override
    public List<GroupeEvent> findBySemestreEnseignantMatiereGroupe(Integer semestreid, Integer enseignantid, Integer matiereid, Integer groupeid) {
        Query q = em.createNamedQuery("GroupeEvent.findBySemestreEnseignantMatiereGroupe", GroupeEvent.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("enseignantid", enseignantid);
        q.setParameter("matiereid", matiereid);
        q.setParameter("groupeid", groupeid);
        return q.getResultList();
    }

    @Override
    public List<GroupeEvent> findBySemestreGroupe(Integer semestreid, Integer groupeid) {
        Query q = em.createNamedQuery("GroupeEvent.findBySemestreGroupe", GroupeEvent.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("groupeid", groupeid);
        return q.getResultList();
    }
}
