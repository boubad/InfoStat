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
import org.infostat.data.dto.AffectationEtudiantDTO;
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.beans.AffectationEtudiantFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("affetud")
public class AffectationEtudiantResource extends BaseDataBean {

    @EJB
    private AffectationEtudiantFacadeLocal facade;

    public AffectationEtudiantResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(AffectationEtudiantDTO entity) {
        AffectationEtudiant p = convertAffectationEtudiant(entity);
        facade.create(p);
    }// Create

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        AffectationEtudiant p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public AffectationEtudiantDTO find(@PathParam("id") Long id) {
        AffectationEtudiant p = facade.find(id);
        return convertAffectationEtudiant(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<AffectationEtudiantDTO> findAll() {
        List<AffectationEtudiant> oList = facade.findAll();
        List<AffectationEtudiantDTO> oRet = new ArrayList<AffectationEtudiantDTO>();
        for (AffectationEtudiant p : oList) {
            AffectationEtudiantDTO pp = convertAffectationEtudiant(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<AffectationEtudiantDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<AffectationEtudiant> oList = facade.findRange(new int[]{from, to});
        List<AffectationEtudiantDTO> oRet = new ArrayList<AffectationEtudiantDTO>();
        for (AffectationEtudiant p : oList) {
            AffectationEtudiantDTO pp = convertAffectationEtudiant(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("user")
    @Produces({"application/xml", "application/json"})
    public List<AffectationEtudiantDTO> findData(@QueryParam("semestreid") Integer semestreid,
            @QueryParam("etudiantid") Integer etudiantid,
            @QueryParam("groupeid") Integer groupeid) {
        if (semestreid == null) {
            return findAll();
        }
        List<AffectationEtudiant> oList = null;
        List<AffectationEtudiantDTO> oRet = new ArrayList<AffectationEtudiantDTO>();
        if ((etudiantid == null) && (groupeid == null)) {
            oList = facade.findBySemestre(semestreid);
        } else if ((etudiantid != null) && (groupeid == null)) {
            oList = facade.findBySemestreEtudiant(semestreid, etudiantid);
        } else if ((etudiantid == null) && (groupeid != null)) {
            oList = facade.findBySemestreGroupe(semestreid, groupeid);
        } else if ((etudiantid != null) && (groupeid != null)) {
            oList = facade.findBySemestreEtudiantGroupe(semestreid, etudiantid, groupeid);
        }
        if (oList != null) {
            for (AffectationEtudiant p : oList) {
                AffectationEtudiantDTO pp = convertAffectationEtudiant(p);
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
