/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Unite;

/**
 *
 * @author boubad
 */
@Local
public interface UniteFacadeLocal {

    void create(Unite unite);

    void edit(Unite unite);

    void remove(Unite unite);

    Unite find(Object id);

    List<Unite> findAll();

    List<Unite> findRange(int[] range);

    int count();
    
}
