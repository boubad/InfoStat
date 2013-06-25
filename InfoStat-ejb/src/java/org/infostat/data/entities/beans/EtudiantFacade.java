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
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.EtudiantEvent;

/**
 *
 * @author boubad
 */
@Stateless
public class EtudiantFacade extends AbstractFacade<Etudiant> implements EtudiantFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtudiantFacade() {
        super(Etudiant.class);
    }

    @Override
    public void create(Etudiant entity) {
        entity.setAffectationetudiants(new ArrayList<AffectationEtudiant>());
        entity.setEtudiantevents(new ArrayList<EtudiantEvent>());
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Etudiant entity) {
        Etudiant p = em.find(Etudiant.class, entity.getId());
        p.setDescription(entity.getDescription());
        p.setFirstname(entity.getFirstname());
        p.setLastname(entity.getLastname());
        p.setPhotodata(entity.getPhotodata());
        p.setStatus(entity.getStatus());
        p.setUsername(entity.getUsername());
        p.setLycee(entity.getLycee());
        p.setMentionbac(entity.getMentionbac());
        p.setOptionbac(entity.getOptionbac());
        p.setRedoublant(entity.getRedoublant());
        p.setSeriebac(entity.getSeriebac());
        p.setVille(entity.getVille());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Etudiant entity) {
        Etudiant p = em.find(Etudiant.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Etudiant> findByLastnameFirstname(String lastname, String firstname) {
        Query q = em.createNamedQuery("Etudiant.findByLastnameFirstname", Etudiant.class);
        q.setParameter("lastname", lastname);
        q.setParameter("firstname", firstname);
        return q.getResultList();
    }
}
