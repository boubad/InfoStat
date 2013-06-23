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
@Table(name = "DBUNITE")
@XmlRootElement
public class Unite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNITEID", nullable = false)
    private Long id;
    @Version
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String sigle;
    @Size(max = 64)
    @Column(length = 64)
    private String nom;
    @Size(max = 16)
    @Column(length = 16)
    private String status;
    @Size(max = 255)
    @Column(length = 255)
    private String description;
    @Column(name = "MORDER")
    private Integer order;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unite")
    private Collection<Matiere> matieres;
    @JoinColumn(name = "DEPARTEMENTID", referencedColumnName = "DEPARTEMENTID", nullable = false)
    @ManyToOne(optional = false)
    private Departement departement;

    public Unite() {
    }

    public Unite(Long uniteid) {
        this.id = uniteid;
    }

    public Unite(Long uniteid, String sigle) {
        this.id = uniteid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @XmlTransient
    public Collection<Matiere> getMatieres() {
        return matieres;
    }

    public void setMatieres(Collection<Matiere> matieres) {
        this.matieres = matieres;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
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
        if (!(object instanceof Unite)) {
            return false;
        }
        Unite other = (Unite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.Unite[ uniteid=" + id + " ]";
    }
}
