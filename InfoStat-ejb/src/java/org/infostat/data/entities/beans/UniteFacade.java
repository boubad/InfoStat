/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.infostat.data.entities.Unite;

/**
 *
 * @author boubad
 */
@Stateless
public class UniteFacade extends AbstractFacade<Unite> implements UniteFacadeLocal {
    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UniteFacade() {
        super(Unite.class);
    }
    
}
