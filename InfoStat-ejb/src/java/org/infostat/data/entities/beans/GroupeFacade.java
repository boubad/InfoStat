/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.infostat.data.entities.Groupe;

/**
 *
 * @author boubad
 */
@Stateless
public class GroupeFacade extends AbstractFacade<Groupe> implements GroupeFacadeLocal {
    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupeFacade() {
        super(Groupe.class);
    }
    
}
