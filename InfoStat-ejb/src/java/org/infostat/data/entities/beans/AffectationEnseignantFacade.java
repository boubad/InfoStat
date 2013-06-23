/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.infostat.data.entities.AffectationEnseignant;

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
    
}
