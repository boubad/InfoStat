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
import org.infostat.data.dto.UniteDTO;
import org.infostat.data.entities.Unite;
import org.infostat.data.entities.beans.UniteFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("unite")
public class UniteResource extends BaseDataBean {

    @EJB
    private UniteFacadeLocal facade;

    public UniteResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(UniteDTO entity) {
        Unite p = convertUnite(entity);
        facade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(UniteDTO entity) {
        Unite p = convertUnite(entity);
        facade.edit(p);
    }// edit

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Unite p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public UniteDTO find(@PathParam("id") Long id) {
        Unite p = facade.find(id);
        return convertUnite(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<UniteDTO> findAll() {
        List<Unite> oList = facade.findAll();
        List<UniteDTO> oRet = new ArrayList<UniteDTO>();
        for (Unite p : oList) {
            UniteDTO pp = convertUnite(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<UniteDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Unite> oList = facade.findRange(new int[]{from, to});
        List<UniteDTO> oRet = new ArrayList<UniteDTO>();
        for (Unite p : oList) {
            UniteDTO pp = convertUnite(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("departement/{id}")
    @Produces({"application/xml", "application/json"})
    public List<UniteDTO> findByDepartement(@PathParam("id") Integer id) {
        List<Unite> oList =
                facade.findByDepartementId(id);
        List<UniteDTO> oRet = new ArrayList<UniteDTO>();
        for (Unite p : oList) {
            UniteDTO pp = convertUnite(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("user")
    @Produces({"application/xml", "application/json"})
    public List<UniteDTO> findByDepartementSigle(@QueryParam("id") Integer id,
            @QueryParam("sigle") String sigle) {
        List<Unite> oList =
                facade.findByDepartementSigle(id, sigle);
        List<UniteDTO> oRet = new ArrayList<UniteDTO>();
        for (Unite p : oList) {
            UniteDTO pp = convertUnite(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("count")
    @Produces({"text/plain", "application/xml", "application/json"})
    public String count() {
        int nRet = facade.count();
        return String.valueOf(nRet);
        //return String.valueOf(super.count());
    }
}
