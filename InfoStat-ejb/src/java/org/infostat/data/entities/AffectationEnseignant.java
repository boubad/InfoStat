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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author boubad
 */
@Entity
@Table(name = "DBAFFPROF")
@XmlRootElement
public class AffectationEnseignant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "AFFPROFID", nullable = false)
    private Long id;
    @Version
    @Column(name="OPTLOCK")
    private Integer version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "affectationenseignant")
    private Collection<GroupeEvent> groupeevents;
    @JoinColumn(name = "SEMESTREID", referencedColumnName = "SEMESTREID", nullable = false)
    @ManyToOne(optional = false)
    private Semestre semestre;
    @JoinColumn(name = "PROFID", referencedColumnName = "PROFID", nullable = false)
    @ManyToOne(optional = false)
    private Enseignant enseignant;
    @JoinColumn(name = "MATIEREID", referencedColumnName = "MATIEREID", nullable = false)
    @ManyToOne(optional = false)
    private Matiere matiere;
    @JoinColumn(name = "GROUPEID", referencedColumnName = "GROUPEID", nullable = false)
    @ManyToOne(optional = false)
    private Groupe groupe;

    public AffectationEnseignant() {
    }

    public AffectationEnseignant(Long affprofid) {
        this.id = affprofid;
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

    @XmlTransient
    public Collection<GroupeEvent> getGroupeevents() {
        return groupeevents;
    }

    public void setGroupeevents(Collection<GroupeEvent> groupeevents) {
        this.groupeevents = groupeevents;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
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
        if (!(object instanceof AffectationEnseignant)) {
            return false;
        }
        AffectationEnseignant other = (AffectationEnseignant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.AffectationEnseignant[ affprofid=" + id + " ]";
    }
}
