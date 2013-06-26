/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.List;
import javax.ejb.Local;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Module;

/**
 *
 * @author boubad
 */
@Local
public interface ModuleFacadeLocal {

    void create(Module module);

    void edit(Module module);

    void remove(Module module);

    Module find(Object id);

    List<Module> findAll();

    List<Module> findRange(int[] range);

    List<Module> findBySigle(String sigle);

    int count();

    void addModuleMatiere(Module module, Matiere matiere);

    void removeModuleMatiere(Module module, Matiere matiere);

    void maintains(List<Module> oList, boolean bDelete);
}
