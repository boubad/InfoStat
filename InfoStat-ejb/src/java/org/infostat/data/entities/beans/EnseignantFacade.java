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

/**
 *
 * @author boubad
 */
@Stateless
public class EnseignantFacade extends AbstractFacade<Enseignant> implements EnseignantFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnseignantFacade() {
        super(Enseignant.class);
    }

    @Override
    public void create(Enseignant entity) {
        entity.setAffectationenseignants(new ArrayList<AffectationEnseignant>());
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Enseignant entity) {
        Enseignant p = em.find(Enseignant.class, entity.getId());
        p.setDescription(entity.getDescription());
        p.setFirstname(entity.getFirstname());
        p.setLastname(entity.getLastname());
        p.setPassword(entity.getPassword());
        p.setPhotodata(entity.getPhotodata());
        p.setStatus(entity.getStatus());
        p.setUsername(entity.getUsername());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Enseignant entity) {
        Enseignant p = em.find(Enseignant.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Enseignant> findByUsername(String username) {
        Query q = em.createNamedQuery("Enseignant.findByUsername", Enseignant.class);
        q.setParameter("username", username);
        return q.getResultList();
    }
}
