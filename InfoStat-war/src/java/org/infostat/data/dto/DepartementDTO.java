/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author boubad
 */
@XmlRootElement(name = "departement")
public class DepartementDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    //
    private int id;
    private int version;
    private String sigle;
    private String nom;
    private String description;
    private byte[] logodata;
    private String status;
    //

    public DepartementDTO() {
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getLogodata() {
        return logodata;
    }

    public void setLogodata(byte[] logodata) {
        this.logodata = logodata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.id;
        hash = 41 * hash + Objects.hashCode(this.sigle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DepartementDTO other = (DepartementDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.sigle, other.sigle)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNom();
    }
}// class DepartementDTO
