/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.beans;

import java.io.ByteArrayInputStream;
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
import javax.ws.rs.core.Response;
import org.infostat.data.dto.EnseignantDTO;
import org.infostat.data.entities.Enseignant;
import org.infostat.data.entities.beans.EnseignantFacadeLocal;

/**
 *
 * @author boubad
 */
@Stateless
@Path("enseignant")
public class EnseignantResource extends BaseDataBean {

    @EJB
    private EnseignantFacadeLocal facade;

    /**
     * Creates a new instance of EnseignantResource
     */
    public EnseignantResource() {
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(EnseignantDTO entity) {
        Enseignant p = convertEnseignant(entity);
        facade.create(p);
    }// Create

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(EnseignantDTO entity) {
        Enseignant p = convertEnseignant(entity);
        facade.edit(p);
    }// edit

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Enseignant p = facade.find(id);
        facade.remove(p);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("{id}")
    public EnseignantDTO find(@PathParam("id") Long id) {
        Enseignant p = facade.find(id);
        EnseignantDTO pRet = convertEnseignant(p);
        if (pRet != null) {
            pRet.setPhotodata(null);
        }
        return pRet;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("user")
    public List<EnseignantDTO> findByUsername(@QueryParam("username") String sigle) {
        List<Enseignant> oList = facade.findByUsername(sigle);
        List<EnseignantDTO> oRet = new ArrayList<EnseignantDTO>();
        for (Enseignant p : oList) {
            EnseignantDTO pp = convertEnseignant(p);
            pp.setPhotodata(null);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Produces({"image/jpeg"})
    @Path("photo/{id}")
    public Response findPhoto(@PathParam("id") Long id) {
        byte[] data = null;
        try {
            Enseignant p = facade.find(id);
            if (p != null) {
                data = p.getPhotodata();
            }
        } catch (Exception ex) {
        }
        if (data != null) {
            ByteArrayInputStream bs = new ByteArrayInputStream(data);
            return Response.ok(data).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<EnseignantDTO> findAll() {
        List<Enseignant> oList = facade.findAll();
        List<EnseignantDTO> oRet = new ArrayList<EnseignantDTO>();
        for (Enseignant p : oList) {
            EnseignantDTO pp = convertEnseignant(p);
            pp.setPhotodata(null);
            oRet.add(pp);
        }
        return oRet;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<EnseignantDTO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Enseignant> oList = facade.findRange(new int[]{from, to});
        List<EnseignantDTO> oRet = new ArrayList<EnseignantDTO>();
        for (Enseignant p : oList) {
            EnseignantDTO pp = convertEnseignant(p);
            pp.setPhotodata(null);
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
