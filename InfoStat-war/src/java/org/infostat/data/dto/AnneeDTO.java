/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author boubad
 */
@XmlRootElement(name="annee")
public class AnneeDTO implements Serializable{
     private static final long serialVersionUID = 1L;
     private int id;
     private int version;
     private int departementid;
     private String departementsigle;
     private String sigle;
     private String status;
     private String description;
     private DateDTO startdate;
     private DateDTO enddate;

    //
    public AnneeDTO() {
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
    @XmlAttribute
    public int getDepartementid() {
        return departementid;
    }

    public void setDepartementid(int departementid) {
        this.departementid = departementid;
    }

    public String getDepartementsigle() {
        return departementsigle;
    }

    public void setDepartementsigle(String departementsigle) {
        this.departementsigle = departementsigle;
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

    public DateDTO getStartdate() {
        return startdate;
    }
    
    public void setStartdate(DateDTO startdate) {
        this.startdate = startdate;
    }

    public DateDTO getEnddate() {
        return enddate;
    }

    public void setEnddate(DateDTO enddate) {
        this.enddate = enddate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        hash = 37 * hash + this.departementid;
        hash = 37 * hash + Objects.hashCode(this.sigle);
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
        final AnneeDTO other = (AnneeDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.departementid != other.departementid) {
            return false;
        }
        if (!Objects.equals(this.sigle, other.sigle)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AnneeDTO{" + "id=" + id + ", version=" + version + ", departementid=" + departementid + ", sigle=" + sigle + ", status=" + status + ", description=" + description + ", startdate=" + startdate + ", enddate=" + enddate + '}';
    }
     
}
