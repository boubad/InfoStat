/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.infostat.data.dto.DepartementDTO;
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.beans.DepartementFacadeLocal;

/**
 * REST Web Service
 *
 * @author boubad
 */
@Stateless
@Path("departement")
public class DepartementResource extends BaseDataBean {

    DepartementFacadeLocal departementFacade = lookupDepartementFacadeLocal();

    /**
     * Creates a new instance of DepartementResource
     */
    public DepartementResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(DepartementDTO entity) {
        Departement p = convertDepartement(entity);
        departementFacade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(DepartementDTO entity) {
        Departement p = convertDepartement(entity);
        departementFacade.edit(p);
    }// edit

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Departement p = departementFacade.find(id);
        departementFacade.remove(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<DepartementDTO> findAll() {
        List<Departement> oList = departementFacade.findAll();
        List<DepartementDTO> oRet = new ArrayList<DepartementDTO>();
        for (Departement p : oList) {
            DepartementDTO pp = convertDepartement(p);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<DepartementDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Departement> oList = departementFacade.findRange(new int[]{from, to});
        List<DepartementDTO> oRet = new ArrayList<DepartementDTO>();
        for (Departement p : oList) {
            DepartementDTO pp = convertDepartement(p);
        }
        return oRet;
    }

    @GET
    @Path("count")
    @Produces({"text/plain", "application/xml", "application/json"})
    public String countREST() {
        int nRet = departementFacade.count();
        return String.valueOf(nRet);
        //return String.valueOf(super.count());
    }

    private DepartementFacadeLocal lookupDepartementFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (DepartementFacadeLocal) c.lookup("java:global/InfoStat/InfoStat-ejb/DepartementFacade!org.infostat.data.entities.beans.DepartementFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
