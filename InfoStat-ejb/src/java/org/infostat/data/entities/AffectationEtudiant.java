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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author boubad
 */
@Entity
@Table(name = "DBAFFETUD")
@XmlRootElement
public class AffectationEtudiant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name="AFFETUDID",nullable = false)
    private Long id;
    @Version
    @Column(name="OPTLOCK")
    private Integer version;
    @JoinColumn(name = "SEMESTREID", referencedColumnName = "SEMESTREID", nullable = false)
    @ManyToOne(optional = false)
    private Semestre semestre;
    @JoinColumn(name = "GROUPEID", referencedColumnName = "GROUPEID", nullable = false)
    @ManyToOne(optional = false)
    private Groupe groupe;
    @JoinColumn(name = "ETUDIANTID", referencedColumnName = "ETUDIANTID", nullable = false)
    @ManyToOne(optional = false)
    private Etudiant etudiant;

    public AffectationEtudiant() {
    }

    public AffectationEtudiant(Long affetudid) {
        this.id = affetudid;
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

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
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
        if (!(object instanceof AffectationEtudiant)) {
            return false;
        }
        AffectationEtudiant other = (AffectationEtudiant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.AffectationEtudiant[ affetudid=" + id + " ]";
    }
    
}
