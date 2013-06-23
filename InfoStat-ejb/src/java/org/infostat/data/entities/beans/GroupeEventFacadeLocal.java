/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.GroupeEvent;

/**
 *
 * @author boubad
 */
@Local
public interface GroupeEventFacadeLocal {

    void create(GroupeEvent groupeEvent);

    void edit(GroupeEvent groupeEvent);

    void remove(GroupeEvent groupeEvent);

    GroupeEvent find(Object id);

    List<GroupeEvent> findAll();

    List<GroupeEvent> findRange(int[] range);

    int count();
    
}
