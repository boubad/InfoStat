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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.infostat.data.dto.AffectationEnseignantDTO;
import org.infostat.data.entities.AffectationEnseignant;
import org.infostat.data.entities.beans.AffectationEnseignantFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("affprof")
public class AffectationEnseignantResource extends BaseDataBean {

    @EJB
    private AffectationEnseignantFacadeLocal facade;

    public AffectationEnseignantResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(AffectationEnseignantDTO entity) {
        AffectationEnseignant p = convertAffectationEnseignant(entity);
        facade.create(p);
    }// Create

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        AffectationEnseignant p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public AffectationEnseignantDTO find(@PathParam("id") Long id) {
        AffectationEnseignant p = facade.find(id);
        return convertAffectationEnseignant(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<AffectationEnseignantDTO> findAll() {
        List<AffectationEnseignant> oList = facade.findAll();
        List<AffectationEnseignantDTO> oRet = new ArrayList<AffectationEnseignantDTO>();
        for (AffectationEnseignant p : oList) {
            AffectationEnseignantDTO pp = convertAffectationEnseignant(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<AffectationEnseignantDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<AffectationEnseignant> oList = facade.findRange(new int[]{from, to});
        List<AffectationEnseignantDTO> oRet = new ArrayList<AffectationEnseignantDTO>();
        for (AffectationEnseignant p : oList) {
            AffectationEnseignantDTO pp = convertAffectationEnseignant(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("user")
    @Produces({"application/xml", "application/json"})
    public List<AffectationEnseignantDTO> findData(@QueryParam("semestreid") Integer semestreid,
            @QueryParam("enseignantid") Integer enseignantid,
            @QueryParam("matiereid") Integer matiereid,
            @QueryParam("groupeid") Integer groupeid) {
        if (semestreid == null) {
            return findAll();
        }
        List<AffectationEnseignant> oList = null;
        List<AffectationEnseignantDTO> oRet = new ArrayList<AffectationEnseignantDTO>();
        if ((enseignantid == null) && (matiereid == null) && (groupeid == null)) {
            oList = facade.findBySemestre(semestreid);
        } else if ((matiereid != null) && (groupeid == null) && (enseignantid == null)) {
            oList = facade.findBySemestreMatiere(semestreid, matiereid);
        } else if ((enseignantid != null) && (matiereid == null) && (groupeid == null)) {
            oList = facade.findBySemestreEnseignant(semestreid, enseignantid);
        } else if ((enseignantid == null) && (matiereid == null) && (groupeid != null)) {
            oList = facade.findBySemestreGroupe(semestreid, groupeid);
        } else if ((enseignantid != null) && (matiereid != null) && (groupeid != null)) {
            oList = facade.findBySemestreEnseignantMatiereGroupe(semestreid, enseignantid, matiereid, groupeid);
        }
        if (oList != null) {
            for (AffectationEnseignant p : oList) {
                AffectationEnseignantDTO pp = convertAffectationEnseignant(p);
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
