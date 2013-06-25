/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.EtudiantEvent;

/**
 *
 * @author boubad
 */
@Local
public interface EtudiantEventFacadeLocal {

    void create(EtudiantEvent etudiantEvent);

    void edit(EtudiantEvent etudiantEvent);

    void remove(EtudiantEvent etudiantEvent);

    EtudiantEvent find(Object id);

    List<EtudiantEvent> findAll();

    List<EtudiantEvent> findRange(int[] range);

    int count();

    List<EtudiantEvent> findByGroupeEvent(Integer evtid);

    List<EtudiantEvent> findBySemestreGenre(Integer semestreid,String genre);

    List<EtudiantEvent> findByEtudiant(Integer etudiantid);

    List<EtudiantEvent> findByEtudiantGroupeEventGenre(Integer etudiantid,
            Integer evtid, String genre);
}
