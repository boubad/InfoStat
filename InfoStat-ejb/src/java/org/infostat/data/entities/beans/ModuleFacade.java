/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Module;

/**
 *
 * @author boubad
 */
@Stateless
public class ModuleFacade extends AbstractFacade<Module> implements ModuleFacadeLocal {

    @PersistenceContext(unitName = "InfoStat-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModuleFacade() {
        super(Module.class);
    }

    @Override
    public void create(Module entity) {
        entity.setMatieres(new ArrayList<Matiere>());
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Module entity) {
        Module p = em.find(Module.class, entity.getId());
        p.setDescription(entity.getDescription());
        p.setNom(entity.getNom());
        p.setSigle(entity.getSigle());
        super.edit(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Module entity) {
        Module p = em.find(Module.class, entity.getId());
        super.remove(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addModuleMatiere(Module module, Matiere matiere) {
        Long nMatiereId = matiere.getId();
        Long nModuleId = module.getId();
        if ((nMatiereId == null) || (nModuleId == null)) {
            return;
        }
        Module pModule = em.find(Module.class, nModuleId);
        Matiere pMat = em.find(Matiere.class, nMatiereId);
        if ((pModule == null) || (pMat == null)) {
            return;
        }
        if (module.getMatieres() == null) {
            module.setMatieres(new ArrayList<Matiere>());
        }
        if (pMat.getModules() == null) {
            pMat.setModules(new ArrayList<Module>());
        }
        boolean bFound = false;
        for (Module m : pMat.getModules()) {
            if (m.getId().longValue() == nModuleId) {
                bFound = true;
                break;
            }
        }
        if (!bFound) {
            pMat.getModules().add(pModule);
        }
        bFound = false;
        for (Matiere m : pModule.getMatieres()) {
            if (m.getId().longValue() == nMatiereId.longValue()) {
                bFound = true;
                break;
            }
        }
        if (!bFound) {
            pModule.getMatieres().add(pMat);
        }
    }

    @Override
    public void removeModuleMatiere(Module module, Matiere matiere) {
        Long nMatiereId = matiere.getId();
        Long nModuleId = module.getId();
        if ((nMatiereId == null) || (nModuleId == null)) {
            return;
        }
        Module pModule = em.find(Module.class, nModuleId);
        Matiere pMat = em.find(Matiere.class, nMatiereId);
        if ((pModule == null) || (pMat == null)) {
            return;
        }
        if (module.getMatieres() == null) {
            module.setMatieres(new ArrayList<Matiere>());
        }
        if (pMat.getModules() == null) {
            pMat.setModules(new ArrayList<Module>());
        }
        for (Module m : pMat.getModules()) {
            if (m.getId().longValue() == nModuleId) {
                pMat.getModules().remove(m);
                break;
            }
        }
        for (Matiere m : pModule.getMatieres()) {
            if (m.getId().longValue() == nMatiereId.longValue()) {
                pModule.getMatieres().remove(m);
                break;
            }
        }
    }

    @Override
    public void maintains(List<Module> oList, boolean bDelete) {
        if (oList == null) {
            return;
        }
        Query q = em.createNamedQuery("Module.findBySigle", Module.class);
        for (Module entity : oList) {
            if (entity != null) {
                Module p = null;
                Long nId = entity.getId();
                if ((nId != null) && (nId.longValue() != 0)) {
                    p = em.find(Module.class, nId);
                    if (p != null) {
                        if (bDelete) {
                            this.remove(p);
                        } else {
                            this.edit(entity);
                        }
                        continue;
                    }
                }
                String sigle = entity.getSigle();
                if (sigle != null) {
                    q.setParameter("sigle", sigle);
                    List<Module> ylist = q.getResultList();
                    if (!ylist.isEmpty()) {
                        p = ylist.get(0);
                        if (bDelete) {
                            this.remove(p);
                        } else {
                            this.edit(p);
                        }
                    } else if (!bDelete) {
                        this.create(entity);
                    }
                }
            }// entity
        }// entity
    }// maintains

    @Override
    public List<Module> findBySigle(String sigle) {
         Query q = em.createNamedQuery("Module.findBySigle", Module.class);
         q.setParameter("sigle", sigle);
         return q.getResultList();
    }
    
}
