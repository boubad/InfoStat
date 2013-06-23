/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.infostat.data.entities.Departement;

/**
 *
 * @author boubad
 */
@Stateless
public class DepartementFacade extends AbstractFacade<Departement> implements DepartementFacadeLocal {
    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartementFacade() {
        super(Departement.class);
    }
    public Departement findBySigle(String sigle){
        EntityManager e = getEntityManager();
        Query q = e.createNamedQuery("Departement.findBySigle");
        q.setParameter("sigle", sigle);
        return (Departement)q.getSingleResult();
    }
}
