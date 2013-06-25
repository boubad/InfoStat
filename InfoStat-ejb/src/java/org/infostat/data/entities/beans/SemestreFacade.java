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
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.Semestre;

/**
 *
 * @author boubad
 */
@Stateless
public class SemestreFacade extends AbstractFacade<Semestre> implements SemestreFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SemestreFacade() {
        super(Semestre.class);
    }

    @Override
    public void create(Semestre entity) {
        Annee annee = em.find(Annee.class, entity.getAnnee().getId());
        entity.setAnnee(annee);
        entity.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        entity.setAffectationetudiants(new ArrayList<AffectationEtudiant>());
        Date d1 = entity.getStartdate();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(d1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        entity.setStartdate(cal.getTime());
        Date d2 = entity.getEnddate();
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(d2);
        cal2.set(Calendar.HOUR, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        entity.setEnddate(cal2.getTime());
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Semestre entity) {
        Date d1 = entity.getStartdate();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(d1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        entity.setStartdate(cal.getTime());
        Date d2 = entity.getEnddate();
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(d2);
        cal2.set(Calendar.HOUR, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        entity.setEnddate(cal2.getTime());
        Semestre p = em.find(Semestre.class, entity.getId());
        p.setDescription(entity.getDescription());
        p.setEnddate(entity.getEnddate());
        p.setSigle(entity.getSigle());
        p.setStartdate(entity.getStartdate());
        p.setStatus(entity.getStatus());
        super.edit(p); //To chang
    }

    @Override
    public void remove(Semestre entity) {
        Semestre p = em.find(Semestre.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Semestre> findByAnnee(Integer nId) {
        Query q = em.createNamedQuery("Semestre.findByAnnee");
        q.setParameter("id", nId);
        return q.getResultList();
    }

    @Override
    public List<Semestre> findByAnneeSigle(Integer nId, String sigle) {
        Query q = em.createNamedQuery("Semestre.findByAnneeSigle");
        q.setParameter("id", nId);
        q.setParameter("sigle", sigle);
        return q.getResultList();
    }

    @Override
    public List<Semestre> findByDepartement(Integer nId) {
        Query q = em.createNamedQuery("Semestre.findByDepartement");
        q.setParameter("id", nId);
        return q.getResultList();
    }
}
