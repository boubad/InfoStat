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
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.infostat.data.dto.EtudiantEventDTO;
import org.infostat.data.dto.EtudiantEventsList;
import org.infostat.data.entities.EtudiantEvent;
import org.infostat.data.entities.beans.EtudiantEventFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("etdevt")
public class EtudiantEventResource extends BaseDataBean {

    @EJB
    private EtudiantEventFacadeLocal facade;

    public EtudiantEventResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(EtudiantEventDTO entity) {
        EtudiantEvent p = convertEtudiantEvent(entity);
        facade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(EtudiantEventDTO entity) {
        EtudiantEvent p = convertEtudiantEvent(entity);
        facade.edit(p);
    }// Create

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        EtudiantEvent p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public EtudiantEventDTO find(@PathParam("id") Long id) {
        EtudiantEvent p = facade.find(id);
        return convertEtudiantEvent(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<EtudiantEventDTO> findAll() {
        List<EtudiantEvent> oList = facade.findAll();
        List<EtudiantEventDTO> oRet = new ArrayList<EtudiantEventDTO>();
        for (EtudiantEvent p : oList) {
            EtudiantEventDTO pp = convertEtudiantEvent(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<EtudiantEventDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<EtudiantEvent> oList = facade.findRange(new int[]{from, to});
        List<EtudiantEventDTO> oRet = new ArrayList<EtudiantEventDTO>();
        for (EtudiantEvent p : oList) {
            EtudiantEventDTO pp = convertEtudiantEvent(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("user")
    @Produces({"application/xml", "application/json"})
    public List<EtudiantEventDTO> findData(@QueryParam("semestreid") Integer semestreid,
            @QueryParam("evtid") Integer evtid,
            @QueryParam("etudiantid") Integer etudiantid,
            @QueryParam("genre") String genre) {
        if (semestreid == null) {
            return findAll();
        }
        List<EtudiantEvent> oList = null;
        List<EtudiantEventDTO> oRet = new ArrayList<EtudiantEventDTO>();
        if (evtid != null) {
            if ((etudiantid == null) || (genre == null)) {
                oList = facade.findByGroupeEvent(evtid);
            } else {
                oList = facade.findByEtudiantGroupeEventGenre(etudiantid, evtid, genre);
            }
        } else if ((semestreid != null) && (genre != null)) {
            oList = facade.findBySemestreGenre(semestreid, genre);
        } else if (etudiantid != null) {
            if ((evtid != null) && (genre != null)) {
                oList = facade.findByEtudiantGroupeEventGenre(etudiantid, evtid, genre);
            } else {
                oList = facade.findByEtudiant(etudiantid);
            }
        }
        if (oList != null) {
            for (EtudiantEvent p : oList) {
                EtudiantEventDTO pp = convertEtudiantEvent(p);
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

    @POST
    @Path("user")
    @Consumes({"application/xml", "application/json"})
    public void maintains(EtudiantEventsList oList) {
        if (oList != null) {
            String mode = oList.getOperation();
            boolean bDelete = false;
            if (mode != null) {
                mode = mode.toLowerCase();
                if (mode.contains("delete")) {
                    bDelete = true;
                }
            }
            List<EtudiantEvent> xList = new ArrayList<EtudiantEvent>();
            EtudiantEventDTO[] col = oList.getEvents();
            if (col != null) {
                for (EtudiantEventDTO p : col) {
                    EtudiantEvent pp = convertEtudiantEvent(p);
                    if (pp != null){
                        xList.add(pp);
                    }
                }// p
            }// col
            if (!xList.isEmpty()) {
                facade.maintains(xList, bDelete);
            }
        }// oList
    }// Create
}
