/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Enseignant;

/**
 *
 * @author boubad
 */
@Local
public interface EnseignantFacadeLocal {

    void create(Enseignant enseignant);

    void edit(Enseignant enseignant);

    void remove(Enseignant enseignant);

    Enseignant find(Object id);

    List<Enseignant> findAll();

    List<Enseignant> findRange(int[] range);

    int count();
    
}
