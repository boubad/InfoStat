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

    void create(GroupeEvent groupeEvent, boolean bCreateNotes);

    void edit(GroupeEvent groupeEvent, boolean bCreateNotes);

    void remove(GroupeEvent groupeEvent);

    GroupeEvent find(Object id);

    List<GroupeEvent> findAll();

    List<GroupeEvent> findRange(int[] range);

    int count();

    List<GroupeEvent> findBySemestre(Integer semestreid);

    List<GroupeEvent> findBySemestreEnseignant(Integer semestreid, Integer enseignantid);

    List<GroupeEvent> findBySemestreEnseignantMatiere(Integer semestreid,
            Integer enseignantid, Integer matiereid);

    List<GroupeEvent> findBySemestreEnseignantMatiereGroupe(Integer semestreid,
            Integer enseignantid, Integer matiereid, Integer groupeid);

    List<GroupeEvent> findBySemestreGroupe(Integer semestreid, Integer groupeid);

    void checkNotes(GroupeEvent entity);
}
