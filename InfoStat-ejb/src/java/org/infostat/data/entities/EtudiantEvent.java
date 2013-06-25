/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author boubad
 */
@Entity
@Table(name = "DBETUDEVENT")
@NamedQueries(
        {
    @NamedQuery(name = "EtudiantEvent.findByGroupeEvent",
            query = "SELECT a from EtudiantEvent a WHERE  a.groupevent.id = :evtid"),
    @NamedQuery(name = "EtudiantEvent.findByEtudiant",
            query = "SELECT a from EtudiantEvent a WHERE  a.etudiant.id = :etudiantid"),
    @NamedQuery(name = "EtudiantEvent.findByEtudiantGroupeEventGenre",
            query = "SELECT a from EtudiantEvent a WHERE  a.etudiant.id = :etudiantid AND a.groupevent.id = :evtid AND a.genre = :genre"),
    @NamedQuery(name = "EtudiantEvent.findBySemestreGenre",
            query = "SELECT a from EtudiantEvent a WHERE a.groupevent.affectationenseignant.semestre.id = :semestreid AND a.genre = :genre")
})
@XmlRootElement
public class EtudiantEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ETUDEVENTID", nullable = false)
    private Long id;
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String genre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FVAL", precision = 23)
    private Float note;
    @Size(max = 64)
    @Column(length = 64)
    private String observations;
    @JoinColumn(name = "GROUPEVENTID", referencedColumnName = "GROUPEVENTID", nullable = false)
    @ManyToOne(optional = false)
    private GroupeEvent groupevent;
    @JoinColumn(name = "ETUDIANTID", referencedColumnName = "ETUDIANTID", nullable = false)
    @ManyToOne(optional = false)
    private Etudiant etudiant;

    public EtudiantEvent() {
    }

    public EtudiantEvent(Long etudeventid) {
        this.id = etudeventid;
    }

    public EtudiantEvent(Long etudeventid, String genre) {
        this.id = etudeventid;
        this.genre = genre;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public GroupeEvent getGroupevent() {
        return groupevent;
    }

    public void setGroupevent(GroupeEvent groupevent) {
        this.groupevent = groupevent;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
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
        if (!(object instanceof EtudiantEvent)) {
            return false;
        }
        EtudiantEvent other = (EtudiantEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.EtudiantEvent[ etudeventid=" + id + " ]";
    }
}
