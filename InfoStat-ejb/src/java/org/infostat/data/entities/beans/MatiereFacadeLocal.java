/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Matiere;

/**
 *
 * @author boubad
 */
@Local
public interface MatiereFacadeLocal {

    void create(Matiere matiere);

    void edit(Matiere matiere);

    void remove(Matiere matiere);

    Matiere find(Object id);

    List<Matiere> findAll();

    List<Matiere> findRange(int[] range);

    int count();
    
}
