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
@Table(name = "DBETUDIANT")
@XmlRootElement
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ETUDIANTID", nullable = false)
    private Long id;
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String firstname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String lastname;
    @Size(max = 32)
    @Column(length = 32)
    private String seriebac;
    @Size(max = 32)
    @Column(length = 32)
    private String optionbac;
    @Size(max = 32)
    @Column(length = 32)
    private String mentionbac;
    @Size(max = 32)
    @Column(name = "ETSUP", length = 32)
    private String etudessuperieures;
    @Size(max = 32)
    @Column(name = "CRED", length = 32)
    private String redoublant;
    @Size(max = 64)
    @Column(name = "CVILLE", length = 64)
    private String ville;
    @Size(max = 64)
    @Column(name = "CLYCEE", length = 64)
    private String lycee;
    @Size(max = 16)
    @Column(length = 16)
    private String status;
    @Size(max = 255)
    @Column(length = 255)
    private String description;
    @Lob
    private byte[] photodata;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "etudiant",orphanRemoval = true)
    private Collection<AffectationEtudiant> affectationetudiants;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "etudiant",orphanRemoval = true)
    private Collection<EtudiantEvent> etudiantevents;

    public Etudiant() {
    }

    public Etudiant(Long etudiantid) {
        this.id = etudiantid;
    }

    public Etudiant(Long etudiantid, String username, String firstname, String lastname) {
        this.id = etudiantid;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSeriebac() {
        return seriebac;
    }

    public void setSeriebac(String seriebac) {
        this.seriebac = seriebac;
    }

    public String getOptionbac() {
        return optionbac;
    }

    public void setOptionbac(String optionbac) {
        this.optionbac = optionbac;
    }

    public String getMentionbac() {
        return mentionbac;
    }

    public void setMentionbac(String mentionbac) {
        this.mentionbac = mentionbac;
    }

    public String getEtudessuperieures() {
        return etudessuperieures;
    }

    public void setEtudessuperieures(String etudessuperieures) {
        this.etudessuperieures = etudessuperieures;
    }

    public String getRedoublant() {
        return redoublant;
    }

    public void setRedoublant(String redoublant) {
        this.redoublant = redoublant;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getLycee() {
        return lycee;
    }

    public void setLycee(String lycee) {
        this.lycee = lycee;
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

    public byte[] getPhotodata() {
        return photodata;
    }

    public void setPhotodata(byte[] photodata) {
        this.photodata = photodata;
    }

    @XmlTransient
    public Collection<AffectationEtudiant> getAffectationetudiants() {
        return affectationetudiants;
    }

    public void setAffectationetudiants(Collection<AffectationEtudiant> affectationetudiants) {
        this.affectationetudiants = affectationetudiants;
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
        if (!(object instanceof Etudiant)) {
            return false;
        }
        Etudiant other = (Etudiant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.Etudiant[ etudiantid=" + id + " ]";
    }
}
