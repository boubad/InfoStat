/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.AffectationEnseignant;

/**
 *
 * @author boubad
 */
@Local
public interface AffectationEnseignantFacadeLocal {

    void create(AffectationEnseignant affectationEnseignant);

    void edit(AffectationEnseignant affectationEnseignant);

    void remove(AffectationEnseignant affectationEnseignant);

    AffectationEnseignant find(Object id);

    List<AffectationEnseignant> findAll();

    List<AffectationEnseignant> findRange(int[] range);

    int count();

    List<AffectationEnseignant> findBySemestre(Integer semestreid);

    List<AffectationEnseignant> findBySemestreEnseignant(Integer semestreid, Integer enseignantid);

    List<AffectationEnseignant> findBySemestreMatiere(Integer semestreid, Integer matiereid);

    List<AffectationEnseignant> findBySemestreGroupe(Integer semestreid, Integer groupeid);

    List<AffectationEnseignant> findBySemestreEnseignantMatiereGroupe(Integer semestreid,
            Integer enseignantid, Integer matiereid, Integer groupeid);
}
