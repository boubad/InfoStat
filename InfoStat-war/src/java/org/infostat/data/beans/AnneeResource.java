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
import org.infostat.data.dto.AnneeDTO;
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.beans.AnneeFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("annee")
public class AnneeResource extends BaseDataBean {
    @EJB
    private AnneeFacadeLocal anneeFacade;

    public AnneeResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(AnneeDTO entity) {
        Annee p = convertAnnee(entity);
        anneeFacade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(AnneeDTO entity) {
        Annee p = convertAnnee(entity);
        anneeFacade.edit(p);
    }// edit

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Annee p = anneeFacade.find(id);
        anneeFacade.remove(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<AnneeDTO> findAll() {
        List<Annee> oList = anneeFacade.findAll();
        List<AnneeDTO> oRet = new ArrayList<AnneeDTO>();
        for (Annee p : oList) {
            AnneeDTO pp = convertAnnee(p);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<AnneeDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Annee> oList = anneeFacade.findRange(new int[]{from, to});
        List<AnneeDTO> oRet = new ArrayList<AnneeDTO>();
        for (Annee p : oList) {
            AnneeDTO pp = convertAnnee(p);
        }
        return oRet;
    }

    @GET
    @Path("departement/{id}")
    @Produces({"application/xml", "application/json"})
    public List<AnneeDTO> findByDepartement(@PathParam("id") Integer id) {
        List<Annee> oList =
                anneeFacade.findAnneesByDepartement(new Long(id.longValue()));
        List<AnneeDTO> oRet = new ArrayList<AnneeDTO>();
        for (Annee p : oList) {
            AnneeDTO pp = convertAnnee(p);
        }
        return oRet;
    }

    @GET
    @Path("count")
    @Produces({"text/plain", "application/xml", "application/json"})
    public String countREST() {
        int nRet = anneeFacade.count();
        return String.valueOf(nRet);
        //return String.valueOf(super.count());
    }
}
