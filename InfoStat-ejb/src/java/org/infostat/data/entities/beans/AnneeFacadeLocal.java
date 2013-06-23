/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Annee;

/**
 *
 * @author boubad
 */
@Local
public interface AnneeFacadeLocal {

    void create(Annee annee);

    void edit(Annee annee);

    void remove(Annee annee);

    Annee find(Object id);

    List<Annee> findAll();

    List<Annee> findRange(int[] range);

    int count();
    List<Annee> findAnneesByDepartement(Long depId);
    
}
