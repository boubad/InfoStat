/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "DBSEMESTRE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ANNEEID", "SIGLE"})})
@NamedQueries(
        {
    @NamedQuery(name = "Semestre.findByAnnee",
            query = "SELECT a from Semestre a WHERE  a.annee.id = :id"),
    @NamedQuery(name = "Semestre.findByAnneeSigle",
            query = "SELECT a from Semestre a WHERE  a.annee.id = :id AND a.sigle = :sigle"),
    @NamedQuery(name = "Semestre.findByDepartement",
            query = "SELECT a from Semestre a WHERE  a.annee.departement.id = :id")
})
@XmlRootElement
public class Semestre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEMESTREID", nullable = false)
    private Long id;
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DSTART", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startdate = new Date();
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEND", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enddate = new Date();
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String sigle;
    @Size(max = 16)
    @Column(length = 16)
    private String status;
    @Size(max = 255)
    @Column(length = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semestre",orphanRemoval = true)
    private Collection<AffectationEnseignant> affectationenseignants;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semestre",orphanRemoval = true)
    private Collection<AffectationEtudiant> affectationetudiants;
    @JoinColumn(name = "ANNEEID", referencedColumnName = "ANNEEID", nullable = false)
    @ManyToOne(optional = false)
    private Annee annee;

    public Semestre() {
    }

    public Semestre(Long semestreid) {
        this.id = semestreid;
    }

    public Semestre(Long semestreid, Date dstart, Date dend, String sigle) {
        this.id = semestreid;
        this.startdate = dstart;
        this.enddate = dend;
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

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
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

    @XmlTransient
    public Collection<AffectationEtudiant> getAffectationetudiants() {
        return affectationetudiants;
    }

    public void setAffectationetudiants(Collection<AffectationEtudiant> affectationetudiants) {
        this.affectationetudiants = affectationetudiants;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
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
        if (!(object instanceof Semestre)) {
            return false;
        }
        Semestre other = (Semestre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.Semestre[ semestreid=" + id + " ]";
    }
}
