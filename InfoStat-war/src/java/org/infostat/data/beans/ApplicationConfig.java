/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.beans;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author boubad
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        // following code can be used to customize Jersey 2.0 JSON provider:
        try {
            Class jsonProvider = Class.forName("org.glassfish.jersey.jackson.JacksonFeature");
            // Class jsonProvider = Class.forName("org.glassfish.jersey.moxy.json.MoxyJsonFeature");
            // Class jsonProvider = Class.forName("org.glassfish.jersey.jettison.JettisonFeature");
            resources.add(jsonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * re-generated by NetBeans REST support to populate given list with all
     * resources defined in the project.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.infostat.data.beans.AffectationEnseignantResource.class);
        resources.add(org.infostat.data.beans.AffectationEtudiantResource.class);
        resources.add(org.infostat.data.beans.AnneeResource.class);
        resources.add(org.infostat.data.beans.DepartementResource.class);
        resources.add(org.infostat.data.beans.EnseignantResource.class);
        resources.add(org.infostat.data.beans.EtudiantEventResource.class);
        resources.add(org.infostat.data.beans.EtudiantResource.class);
        resources.add(org.infostat.data.beans.GroupeEventResource.class);
        resources.add(org.infostat.data.beans.GroupeResource.class);
        resources.add(org.infostat.data.beans.MatiereResource.class);
        resources.add(org.infostat.data.beans.ModuleResource.class);
        resources.add(org.infostat.data.beans.SemestreResource.class);
        resources.add(org.infostat.data.beans.UniteResource.class);
    }
}
