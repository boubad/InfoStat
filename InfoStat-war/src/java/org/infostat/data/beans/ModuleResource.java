/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.infostat.data.dto.ModuleAssocDTO;
import org.infostat.data.dto.ModuleAssocsList;
import org.infostat.data.dto.ModuleDTO;
import org.infostat.data.dto.ModulesList;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Module;
import org.infostat.data.entities.beans.MatiereFacadeLocal;
import org.infostat.data.entities.beans.ModuleFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("module")
public class ModuleResource extends BaseDataBean {
    
    @EJB
    private ModuleFacadeLocal facade;
    @EJB
    private MatiereFacadeLocal matiereFacade;

    public ModuleResource() {
    }
    
    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(ModuleDTO entity) {
        Module p = convertModule(entity);
        facade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(ModuleDTO entity) {
        Module p = convertModule(entity);
        facade.edit(p);
    }// edit

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Module p = facade.find(id);
        facade.remove(p);
    }
    
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public ModuleDTO find(@PathParam("id") Long id) {
        Module p = facade.find(id);
        return convertModule(p);
    }
    
    @GET
    @Produces({"application/xml", "application/json"})
    public List<ModuleDTO> findAll() {
        List<Module> oList = facade.findAll();
        List<ModuleDTO> oRet = new ArrayList<ModuleDTO>();
        for (Module p : oList) {
            ModuleDTO pp = convertModule(p);
            oRet.add(pp);
        }
        return oRet;
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<ModuleDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Module> oList = facade.findRange(new int[]{from, to});
        List<ModuleDTO> oRet = new ArrayList<ModuleDTO>();
        for (Module p : oList) {
            ModuleDTO pp = convertModule(p);
            oRet.add(pp);
        }
        return oRet;
    }
    
    @GET
    @Path("user")
    @Produces({"application/xml", "application/json"})
    public List<ModuleDTO> findBySigle(@QueryParam("sigle") String sigle) {
        List<Module> oList = facade.findBySigle(sigle);
        List<ModuleDTO> oRet = new ArrayList<ModuleDTO>();
        for (Module p : oList) {
            ModuleDTO pp = convertModule(p);
            oRet.add(pp);
        }
        return oRet;
    }
    
    @POST
    @Path("user")
    @Consumes({"application/xml", "application/json"})
    public void maintains(ModulesList oList) {
        if (oList != null) {
            String mode = oList.getOperation();
            boolean bDelete = false;
            if (mode != null) {
                mode = mode.toLowerCase();
                if (mode.contains("delete")) {
                    bDelete = true;
                }
            }
            List<Module> xList = new ArrayList<Module>();
            ModuleDTO[] col = oList.getModules();
            if (col != null) {
                for (ModuleDTO p : col) {
                    Module pp = convertModule(p);
                    if (pp != null) {
                        xList.add(pp);
                    }
                }// p
            }// col
            if (!xList.isEmpty()) {
                facade.maintains(xList, bDelete);
            }
        }// oList
    }// Create

    @POST
    @Path("assoc")
    @Consumes({"application/xml", "application/json"})
    public void maintains(ModuleAssocsList oList) {
        if (oList != null) {
            ModuleAssocDTO[] col = oList.getAssociations();
            if (col != null) {
                for (ModuleAssocDTO p : col) {
                    if (p != null) {
                        Integer nModuleId = p.getModuleid();
                        Integer nMatiereId = p.getMatiereid();
                        if ((nModuleId != null) && (nMatiereId != null)) {
                            Module module = facade.find(nModuleId);
                            Matiere matiere = matiereFacade.find(nMatiereId);
                            if ((module != null) && (matiere != null)) {
                                boolean bAdd = true;
                                String op = p.getOperation();
                                if (op != null) {
                                    String s = op.trim().toLowerCase();
                                    if (s.equals("remove")) {
                                        bAdd = false;
                                    }
                                }
                                if (bAdd) {
                                    facade.addModuleMatiere(module, matiere);
                                } else {
                                    facade.removeModuleMatiere(module, matiere);
                                }
                            }
                        }
                    }// p
                }// p
            }// col
        }// oList
    }// Create

    @GET
    @Path("count")
    @Produces({"text/plain", "application/xml", "application/json"})
    public String count() {
        int nRet = facade.count();
        return String.valueOf(nRet);
        //return String.valueOf(super.count());
    }
}
