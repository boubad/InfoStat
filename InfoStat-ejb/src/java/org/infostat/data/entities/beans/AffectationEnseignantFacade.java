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
import org.infostat.data.entities.Enseignant;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.GroupeEvent;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Semestre;

/**
 *
 * @author boubad
 */
@Stateless
public class AffectationEnseignantFacade extends AbstractFacade<AffectationEnseignant> implements AffectationEnseignantFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AffectationEnseignantFacade() {
        super(AffectationEnseignant.class);
    }

    @Override
    public void create(AffectationEnseignant entity) {
        entity.setGroupeevents(new ArrayList<GroupeEvent>());
        Semestre pSem = em.find(Semestre.class, entity.getSemestre().getId());
        Enseignant prof = em.find(Enseignant.class, entity.getEnseignant().getId());
        Matiere mat = em.find(Matiere.class, entity.getMatiere().getId());
        Groupe groupe = em.find(Groupe.class, entity.getGroupe().getId());
        entity.setEnseignant(prof);
        entity.setGroupe(groupe);
        entity.setMatiere(mat);
        entity.setSemestre(pSem);
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(AffectationEnseignant entity) {
        AffectationEnseignant p = em.find(AffectationEnseignant.class, entity.getId());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AffectationEnseignant entity) {
        AffectationEnseignant p = em.find(AffectationEnseignant.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AffectationEnseignant> findBySemestre(Integer semestreid) {
        Query q = em.createNamedQuery("AffectationEnseignant.findBySemestre", AffectationEnseignant.class);
        q.setParameter("semestreid", semestreid);
        return q.getResultList();
    }

    @Override
    public List<AffectationEnseignant> findBySemestreEnseignant(Integer semestreid, Integer enseignantid) {
        Query q = em.createNamedQuery("AffectationEnseignant.findBySemestreEnseignant", AffectationEnseignant.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("enseignantid", enseignantid);
        return q.getResultList();
    }

    @Override
    public List<AffectationEnseignant> findBySemestreMatiere(Integer semestreid, Integer matiereid) {
        Query q = em.createNamedQuery("AffectationEnseignant.findBySemestreMatiere", AffectationEnseignant.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("matiereid", matiereid);
        return q.getResultList();
    }

    @Override
    public List<AffectationEnseignant> findBySemestreGroupe(Integer semestreid, Integer groupeid) {
        Query q = em.createNamedQuery("AffectationEnseignant.findBySemestreGroupe", AffectationEnseignant.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("groupeid", groupeid);
        return q.getResultList();
    }

    @Override
    public List<AffectationEnseignant> findBySemestreEnseignantMatiereGroupe(Integer semestreid,
            Integer enseignantid, Integer matiereid, Integer groupeid) {
        Query q = em.createNamedQuery("AffectationEnseignant.findBySemestreEnseignantMatiereGroupe",
                AffectationEnseignant.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("groupeid", groupeid);
        q.setParameter("matiereid", matiereid);
        q.setParameter("enseignantid", enseignantid);
        return q.getResultList();
    }
}
