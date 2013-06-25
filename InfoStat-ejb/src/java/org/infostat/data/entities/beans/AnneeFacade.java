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
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.Semestre;

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
        Departement dep =em.find(Departement.class, 
                new Long(entity.getDepartement().getId().longValue()));
        entity.setDepartement(dep);
        entity.setSemestres(new ArrayList<Semestre>());
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
        em.persist(entity);
    }

    @Override
    public void edit(Annee entity) {
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
        Annee p = em.find(Annee.class, entity.getId());
        p.setDescription(entity.getDescription());
        p.setEnddate(entity.getEnddate());
        p.setSigle(entity.getSigle());
        p.setStartdate(entity.getStartdate());
        p.setStatus(entity.getStatus());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Annee entity) {
         Annee p = em.find(Annee.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Annee> findAnneesByDepartement(Long depId){
        Query q = em.createNamedQuery("Annee.findByDepartement");
        q.setParameter("id", depId);
        return (List<Annee>)q.getResultList();
    }// findAnneesByDepartement
}
