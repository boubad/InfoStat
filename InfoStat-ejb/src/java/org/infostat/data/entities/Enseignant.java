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
@Table(name = "DBPROF")
@XmlRootElement
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROFID", nullable = false)
    private Long id;
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(nullable = false, length = 64)
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "CPASSWORD", nullable = false, length = 64)
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(nullable = false, length = 32)
    private String firstname;
    @Size(max = 32)
    @Column(length = 32)
    private String lastname;
    @Size(max = 16)
    @Column(length = 16)
    private String status;
    @Size(max = 256)
    @Column(length = 256)
    private String description;
    @Lob
    private byte[] photodata;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enseignant",orphanRemoval = true)
    private Collection<AffectationEnseignant> affectationenseignants;

    public Enseignant() {
    }

    public Enseignant(Long profid) {
        this.id = profid;
    }

    public Enseignant(Long profid, String username, String cpassword, String firstname) {
        this.id = profid;
        this.username = username;
        this.password = cpassword;
        this.firstname = firstname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    public Collection<AffectationEnseignant> getAffectationenseignants() {
        return affectationenseignants;
    }

    public void setAffectationenseignants(Collection<AffectationEnseignant> affectationenseignants) {
        this.affectationenseignants = affectationenseignants;
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
        if (!(object instanceof Enseignant)) {
            return false;
        }
        Enseignant other = (Enseignant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.infostat.data.entities.Enseignant[ profid=" + id + " ]";
    }
}
