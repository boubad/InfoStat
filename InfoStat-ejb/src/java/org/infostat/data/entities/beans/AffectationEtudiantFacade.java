/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.infostat.data.entities.AffectationEtudiant;

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
    
}
