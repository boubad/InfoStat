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
import org.infostat.data.dto.GroupeDTO;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.beans.GroupeFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("groupe")
public class GroupeResource extends BaseDataBean {

    @EJB
    private GroupeFacadeLocal facade;

    public GroupeResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(GroupeDTO entity) {
        Groupe p = convertGroupe(entity);
        facade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(GroupeDTO entity) {
        Groupe p = convertGroupe(entity);
        facade.edit(p);
    }// edit

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Groupe p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public GroupeDTO find(@PathParam("id") Long id) {
        Groupe p = facade.find(id);
        return convertGroupe(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<GroupeDTO> findAll() {
        List<Groupe> oList = facade.findAll();
        List<GroupeDTO> oRet = new ArrayList<GroupeDTO>();
        for (Groupe p : oList) {
            GroupeDTO pp = convertGroupe(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<GroupeDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Groupe> oList = facade.findRange(new int[]{from, to});
        List<GroupeDTO> oRet = new ArrayList<GroupeDTO>();
        for (Groupe p : oList) {
            GroupeDTO pp = convertGroupe(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("departement/{id}")
    @Produces({"application/xml", "application/json"})
    public List<GroupeDTO> findByDepartement(@PathParam("id") Integer id) {
        List<Groupe> oList =
                facade.findByDepartementId(id);
        List<GroupeDTO> oRet = new ArrayList<GroupeDTO>();
        for (Groupe p : oList) {
            GroupeDTO pp = convertGroupe(p);
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
