/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.infostat.data.entities.GroupeEvent;

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
    
}
