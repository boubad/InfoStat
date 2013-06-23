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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "DBGROUPEEVENT")
@XmlRootElement
public class GroupeEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "GROUPEVENTID", nullable = false)
    private Long id;
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String genre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DDATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(nullable = false, length = 64)
    private String nom;
    @Temporal(TemporalType.TIME)
    @Column(name = "TSTART")
    private Date starttime;
    @Temporal(TemporalType.TIME)
    @Column(name = "TEND")
    private Date endtime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 23)
    private Float coefficient;
    @Size(max = 16)
    @Column(length = 16)
    private String status;
    @Size(max = 255)
    @Column(length = 255)
    private String description;
    @Column(name = "CLOCATION", length = 64)
    private String location;
    @JoinColumn(name = "AFFPROFID", referencedColumnName = "AFFPROFID", nullable = false)
    @ManyToOne(optional = false)
    private AffectationEnseignant affectationenseignant;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupevent")
    private Collection<EtudiantEvent> etudiantevents;

    public GroupeEvent() {
    }

    public GroupeEvent(Long groupeventid) {
        this.id = groupeventid;
    }

    public GroupeEvent(Long groupeventid, String genre, Date ddate, String nom) {
        this.id = groupeventid;
        this.genre = genre;
        this.date = ddate;
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
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

    public AffectationEnseignant getAffectationenseignant() {
        return affectationenseignant;
    }

    public void setAffectationenseignant(AffectationEnseignant affectationenseignant) {
        this.affectationenseignant = affectationenseignant;
    }

    @XmlTransient
    public Collection<EtudiantEvent> getEtudiantevents() {
        return etudiantevents;
    }

    public void setEtudiantevents(Collection<EtudiantEvent> etudiantevents) {
        this.etudiantevents = etudiantevents;
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
        if (!(object instanceof GroupeEvent)) {
            return false;
        }
        GroupeEvent other = (GroupeEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.GroupeEvent[ groupeventid=" + id + " ]";
    }
}
