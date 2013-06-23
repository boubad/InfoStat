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
@Table(name = "DBANNEE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"DEPARTEMENTID", "SIGLE"}),
    @UniqueConstraint(columnNames = {"DEPARTEMENTID", "DEND"}),
    @UniqueConstraint(columnNames = {"DEPARTEMENTID", "DSTART"})})
@NamedQueries(
        @NamedQuery(name="Annee.findByDepartement",
        query = "SELECT a FROM Annee a WHERE a.departement.id = :id ORDER BY a.startdate DESC")
        )
@XmlRootElement
public class Annee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name="ANNEEID",nullable = false)
    private Long id;
    @Version
    @Column(name="OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Column(name="DSTART",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startdate;
    @Basic(optional = false)
    @NotNull
    @Column(name="DEND",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enddate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name="SIGLE",nullable = false, length = 32)
    private String sigle;
    @Size(max = 16)
    @Column(name="STATUS",length = 16)
    private String status;
    @Size(max = 255)
    @Column(name="DESCRIPTION",length = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "annee")
    private Collection<Semestre> semestres;
    @JoinColumn(name = "DEPARTEMENTID", referencedColumnName = "DEPARTEMENTID", nullable = false)
    @ManyToOne(optional = false)
    private Departement departement;

    public Annee() {
    }

    public Annee(Long anneeid) {
        this.id = anneeid;
    }

    public Annee(Long anneeid, Date dstart, Date dend, String sigle) {
        this.id = anneeid;
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
    public Collection<Semestre> getSemestres() {
        return semestres;
    }

    public void setSemestres(Collection<Semestre> semestres) {
        this.semestres = semestres;
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
        if (!(object instanceof Annee)) {
            return false;
        }
        Annee other = (Annee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.Annee[ anneeid=" + id + " ]";
    }
    
}
