/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Groupe;

/**
 *
 * @author boubad
 */
@Local
public interface GroupeFacadeLocal {

    void create(Groupe groupe);

    void edit(Groupe groupe);

    void remove(Groupe groupe);

    Groupe find(Object id);

    List<Groupe> findAll();

    List<Groupe> findRange(int[] range);

    int count();

    List<Groupe> findByDepartementId(Integer depId);

    List<Groupe> findByDepartementSigle(Integer depId, String sigle);

    void maintains(List<Groupe> oList, boolean bDelete);
}
