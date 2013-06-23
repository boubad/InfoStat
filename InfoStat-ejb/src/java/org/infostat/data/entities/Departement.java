/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author boubad
 */
@Entity
@Table(name = "DBDEPARTEMENT", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"SIGLE"})})
@NamedQueries(
        @NamedQuery(name = "Departement.findBySigle",
        query = "SELECT a from Departement a WHERE a.sigle = :sigle"))
@XmlRootElement
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEPARTEMENTID", nullable = false)
    private Long id;
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "SIGLE", nullable = false, length = 32)
    private String sigle;
    @Size(max = 64)
    @Column(name = "NOM", length = 64)
    private String nom;
    @Size(max = 16)
    @Column(name = "STATUS", length = 16)
    private String status;
    @Size(max = 255)
    @Column(name = "REM", length = 255)
    private String rem;
    @Lob
    @Column(name = "LOGODATA")
    private Serializable logodata;
    @Size(max = 255)
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departement")
    private Collection<Groupe> groupes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departement")
    private Collection<Unite> unites;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departement")
    private Collection<Annee> annees;

    public Departement() {
    }

    public Departement(Long departementid) {
        this.id = departementid;
    }

    public Departement(Long departementid, String sigle) {
        this.id = departementid;
        this.sigle = sigle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public Serializable getLogodata() {
        return logodata;
    }

    public void setLogodata(Serializable logodata) {
        this.logodata = logodata;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Groupe> getGroupes() {
        return groupes;
    }

    public void setGroupes(Collection<Groupe> groupes) {
        this.groupes = groupes;
    }

    @XmlTransient
    public Collection<Unite> getUnites() {
        return unites;
    }

    public void setUnites(Collection<Unite> unites) {
        this.unites = unites;
    }

    @XmlTransient
    public Collection<Annee> getAnnees() {
        return annees;
    }

    public void setAnnees(Collection<Annee> annees) {
        this.annees = annees;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departement)) {
            return false;
        }
        Departement other = (Departement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.Departement[ departementid=" + id + " ]";
    }
}
