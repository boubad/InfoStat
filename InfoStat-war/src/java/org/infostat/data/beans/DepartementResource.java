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

    @EJB
    private DepartementFacadeLocal departementFacade;

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
    @Path("{id}")
    public DepartementDTO find(@PathParam("id") Long id) {
        Departement p = departementFacade.find(id);
        return convertDepartement(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("user")
    public List<DepartementDTO> findBySigle(@QueryParam("sigle") String sigle) {
        List<Departement> oList = departementFacade.findBySigle(sigle);
        List<DepartementDTO> oRet = new ArrayList<DepartementDTO>();
        for (Departement p : oList) {
            DepartementDTO pp = convertDepartement(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<DepartementDTO> findAll() {
        List<Departement> oList = departementFacade.findAll();
        List<DepartementDTO> oRet = new ArrayList<DepartementDTO>();
        for (Departement p : oList) {
            DepartementDTO pp = convertDepartement(p);
            oRet.add(pp);
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
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("count")
    @Produces({"text/plain", "application/xml", "application/json"})
    public String count() {
        int nRet = departementFacade.count();
        return String.valueOf(nRet);
        //return String.valueOf(super.count());
    }
}
