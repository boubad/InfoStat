/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Departement;

/**
 *
 * @author boubad
 */
@Local
public interface DepartementFacadeLocal {

    void create(Departement departement);

    void edit(Departement departement);

    void remove(Departement departement);

    Departement find(Object id);

    List<Departement> findAll();

    List<Departement> findRange(int[] range);

    int count();

    List<Departement> findBySigle(String sigle);

    void maintains(List<Departement> oList, boolean bDelete);
}
