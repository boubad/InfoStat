/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Etudiant;

/**
 *
 * @author boubad
 */
@Local
public interface EtudiantFacadeLocal {

    void create(Etudiant etudiant);

    void edit(Etudiant etudiant);

    void remove(Etudiant etudiant);

    Etudiant find(Object id);

    List<Etudiant> findAll();

    List<Etudiant> findRange(int[] range);

    int count();

    List<Etudiant> findByLastnameFirstname(String lastname, String firstname);
}
