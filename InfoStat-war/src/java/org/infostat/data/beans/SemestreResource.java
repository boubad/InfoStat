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
import org.infostat.data.dto.SemestreDTO;
import org.infostat.data.entities.Semestre;
import org.infostat.data.entities.beans.SemestreFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("semestre")
public class SemestreResource extends BaseDataBean {

    @EJB
    private SemestreFacadeLocal facade;

    public SemestreResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(SemestreDTO entity) {
        Semestre p = convertSemestre(entity);
        facade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(SemestreDTO entity) {
        Semestre p = convertSemestre(entity);
        facade.edit(p);
    }// edit

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Semestre p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public SemestreDTO find(@PathParam("id") Long id) {
        Semestre p = facade.find(id);
        return convertSemestre(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<SemestreDTO> findAll() {
        List<Semestre> oList = facade.findAll();
        List<SemestreDTO> oRet = new ArrayList<SemestreDTO>();
        for (Semestre p : oList) {
            SemestreDTO pp = convertSemestre(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<SemestreDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Semestre> oList = facade.findRange(new int[]{from, to});
        List<SemestreDTO> oRet = new ArrayList<SemestreDTO>();
        for (Semestre p : oList) {
            SemestreDTO pp = convertSemestre(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("departement/{id}")
    @Produces({"application/xml", "application/json"})
    public List<SemestreDTO> findByDepartement(@PathParam("id") Integer id) {
        List<Semestre> oList =
                facade.findByDepartement(id);
        List<SemestreDTO> oRet = new ArrayList<SemestreDTO>();
        for (Semestre p : oList) {
            SemestreDTO pp = convertSemestre(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("annee/{id}")
    @Produces({"application/xml", "application/json"})
    public List<SemestreDTO> findByAnnee(@PathParam("id") Integer id) {
        List<Semestre> oList =
                facade.findByAnnee(id);
        List<SemestreDTO> oRet = new ArrayList<SemestreDTO>();
        for (Semestre p : oList) {
            SemestreDTO pp = convertSemestre(p);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("user")
    @Produces({"application/xml", "application/json"})
    public List<SemestreDTO> findByAnneeSigle(@QueryParam("id") Integer id,
            @QueryParam("sigle") String sigle) {
        List<Semestre> oList =
                facade.findByAnneeSigle(id, sigle);
        List<SemestreDTO> oRet = new ArrayList<SemestreDTO>();
        for (Semestre p : oList) {
            SemestreDTO pp = convertSemestre(p);
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
