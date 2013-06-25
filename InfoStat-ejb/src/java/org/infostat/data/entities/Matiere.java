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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "DBMATIERE")
@NamedQueries(
        {
    @NamedQuery(name = "Matiere.findByUnite",
            query = "SELECT a from Matiere a WHERE  a.unite.id = :id"),
    @NamedQuery(name = "Matiere.findByUniteSigle",
            query = "SELECT a from Matiere a WHERE  a.unite.id = :id AND a.sigle = :sigle"),
    @NamedQuery(name = "Matiere.findByDepartement",
            query = "SELECT a from Matiere a WHERE  a.unite.departement.id = :id")
})
@XmlRootElement
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "MATIEREID", nullable = false)
    private Long id;
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String sigle;
    @Size(max = 64)
    @Column(length = 64)
    private String nom;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 23)
    private Float coefficient;
    @Column(name = "MORDER")
    private Integer order;
    @Size(max = 16)
    @Column(length = 16)
    private String status;
    @Size(max = 255)
    @Column(length = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matiere",orphanRemoval = true)
    private Collection<AffectationEnseignant> affectationenseignants;
    @JoinColumn(name = "UNITEID", referencedColumnName = "UNITEID", nullable = false)
    @ManyToOne(optional = false)
    private Unite unite;

    public Matiere() {
    }

    public Matiere(Long matiereid) {
        this.id = matiereid;
    }

    public Matiere(Long matiereid, String sigle) {
        this.id = matiereid;
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

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<AffectationEnseignant> getAffectationenseignants() {
        return affectationenseignants;
    }

    public void setAffectationenseignants(Collection<AffectationEnseignant> affectationenseignants) {
        this.affectationenseignants = affectationenseignants;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
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
        if (!(object instanceof Matiere)) {
            return false;
        }
        Matiere other = (Matiere) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.Matiere[ matiereid=" + id + " ]";
    }
}
