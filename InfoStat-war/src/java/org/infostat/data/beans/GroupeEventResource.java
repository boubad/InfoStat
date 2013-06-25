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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.infostat.data.dto.GroupeEventDTO;
import org.infostat.data.entities.GroupeEvent;
import org.infostat.data.entities.beans.GroupeEventFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("grpevt")
public class GroupeEventResource extends BaseDataBean {

    @EJB
    private GroupeEventFacadeLocal facade;

    public GroupeEventResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(GroupeEventDTO entity) {
        GroupeEvent p = convertGroupeEvent(entity);
        boolean bNotes = entity.isCreatenotes();
        facade.create(p, bNotes);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(GroupeEventDTO entity) {
        GroupeEvent p = convertGroupeEvent(entity);
        boolean bNotes = entity.isCreatenotes();
        facade.edit(p, bNotes);
    }// Create

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        GroupeEvent p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public GroupeEventDTO find(@PathParam("id") Long id) {
        GroupeEvent p = facade.find(id);
        return convertGroupeEvent(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<GroupeEventDTO> findAll() {
        List<GroupeEvent> oList = facade.findAll();
        List<GroupeEventDTO> oRet = new ArrayList<GroupeEventDTO>();
        for (GroupeEvent p : oList) {
            GroupeEventDTO pp = convertGroupeEvent(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<GroupeEventDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<GroupeEvent> oList = facade.findRange(new int[]{from, to});
        List<GroupeEventDTO> oRet = new ArrayList<GroupeEventDTO>();
        for (GroupeEvent p : oList) {
            GroupeEventDTO pp = convertGroupeEvent(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("user")
    @Produces({"application/xml", "application/json"})
    public List<GroupeEventDTO> findData(@QueryParam("semestreid") Integer semestreid,
            @QueryParam("enseignantid") Integer enseignantid,
            @QueryParam("matiereid") Integer matiereid,
            @QueryParam("groupeid") Integer groupeid) {
        if (semestreid == null) {
            return findAll();
        }
        List<GroupeEvent> oList = null;
        List<GroupeEventDTO> oRet = new ArrayList<GroupeEventDTO>();
        if ((enseignantid == null) && (matiereid == null) && (groupeid == null)) {
            oList = facade.findBySemestre(semestreid);
        } else if ((matiereid == null) && (groupeid == null) && (enseignantid != null)) {
            oList = facade.findBySemestreEnseignant(semestreid, enseignantid);
        } else if ((enseignantid != null) && (matiereid != null) && (groupeid == null)) {
            oList = facade.findBySemestreEnseignantMatiere(semestreid, enseignantid, matiereid);
        } else if ((enseignantid != null) && (matiereid != null) && (groupeid != null)) {
            oList = facade.findBySemestreEnseignantMatiereGroupe(semestreid, enseignantid, matiereid, groupeid);
        }
        if (oList != null) {
            for (GroupeEvent p : oList) {
                GroupeEventDTO pp = convertGroupeEvent(p);
                oRet.add(pp);
            }
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
