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
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.Semestre;

/**
 *
 * @author boubad
 */
@Stateless
public class AffectationEtudiantFacade extends AbstractFacade<AffectationEtudiant> implements AffectationEtudiantFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AffectationEtudiantFacade() {
        super(AffectationEtudiant.class);
    }

    @Override
    public void create(AffectationEtudiant entity) {
        Semestre pSem = em.find(Semestre.class, entity.getSemestre().getId());
        Etudiant etud = em.find(Etudiant.class, entity.getEtudiant().getId());
        Groupe groupe = em.find(Groupe.class, entity.getGroupe().getId());
        entity.setEtudiant(etud);
        entity.setGroupe(groupe);
        entity.setSemestre(pSem);
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(AffectationEtudiant entity) {
        AffectationEtudiant p = em.find(AffectationEtudiant.class, entity.getId());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AffectationEtudiant entity) {
        AffectationEtudiant p = em.find(AffectationEtudiant.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AffectationEtudiant> findBySemestre(Integer semestreid) {
        Query q = em.createNamedQuery("AffectationEtudiant.findBySemestre", AffectationEtudiant.class);
        q.setParameter("semestreid", semestreid);
        return q.getResultList();
    }

    @Override
    public List<AffectationEtudiant> findBySemestreEtudiant(Integer semestreid, Integer etudiantid) {
        Query q = em.createNamedQuery("AffectationEtudiant.findBySemestreEtudiant", AffectationEtudiant.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("etudiantid", etudiantid);
        return q.getResultList();
    }

    @Override
    public List<AffectationEtudiant> findBySemestreGroupe(Integer semestreid, Integer groupeid) {
        Query q = em.createNamedQuery("AffectationEtudiant.findBySemestreGroupe", AffectationEtudiant.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("groupeid", groupeid);
        return q.getResultList();
    }

    @Override
    public List<AffectationEtudiant> findBySemestreEtudiantGroupe(Integer semestreid, Integer etudiantid, Integer groupeid) {
        Query q = em.createNamedQuery("AffectationEtudiant.findBySemestreEtudiantGroupe", AffectationEtudiant.class);
        q.setParameter("semestreid", semestreid);
        q.setParameter("groupeid", groupeid);
        q.setParameter("etudiantid", etudiantid);
        return q.getResultList();
    }
}
