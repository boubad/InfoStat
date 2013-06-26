/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.AffectationEtudiant;

/**
 *
 * @author boubad
 */
@Local
public interface AffectationEtudiantFacadeLocal {

    void create(AffectationEtudiant affectationEtudiant);

    void edit(AffectationEtudiant affectationEtudiant);

    void remove(AffectationEtudiant affectationEtudiant);

    AffectationEtudiant find(Object id);

    List<AffectationEtudiant> findAll();

    List<AffectationEtudiant> findRange(int[] range);

    int count();

    List<AffectationEtudiant> findBySemestre(Integer semestreid);

    List<AffectationEtudiant> findBySemestreEtudiant(Integer semestreid, Integer etudiantid);

    List<AffectationEtudiant> findBySemestreGroupe(Integer semestreid, Integer groupeid);

    List<AffectationEtudiant> findBySemestreEtudiantGroupe(Integer semestreid,
            Integer etudiantid, Integer groupeid);

    void maintains(List<AffectationEtudiant> oList, boolean bDelete);
}
