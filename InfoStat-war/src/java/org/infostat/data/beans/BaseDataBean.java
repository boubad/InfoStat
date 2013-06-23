/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import org.infostat.data.dto.AnneeDTO;
import org.infostat.data.dto.DateDTO;
import org.infostat.data.dto.DepartementDTO;
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.Departement;

/**
 *
 * @author boubad
 */
public class BaseDataBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public BaseDataBean() {
    }
    public Date convertDate(DateDTO p) {
        Date pRet = null;
        if (p != null) {
            GregorianCalendar cal =
                    (GregorianCalendar) GregorianCalendar.getInstance();
            cal.set(p.getYear(), p.getMonth() - 1, p.getDay(),
                    p.getHour(), p.getMinute(), p.getSecond());
            pRet = cal.getTime();
        }// p
        return pRet;
    }// convertDate
    public DateDTO convertDate(Date p) {
        DateDTO pRet = null;
        if (p != null) {
            GregorianCalendar cal =
                    (GregorianCalendar) GregorianCalendar.getInstance();
            cal.setTime(p);
            pRet = new DateDTO(cal.get(GregorianCalendar.YEAR),
                    cal.get(GregorianCalendar.MONTH + 1),
                    cal.get(GregorianCalendar.DAY_OF_MONTH),
                    cal.get(GregorianCalendar.HOUR),
                    cal.get(GregorianCalendar.MINUTE),
                    cal.get(GregorianCalendar.SECOND));
        }// p
        return pRet;
    }// convertDate

    public Departement convertDepartement(DepartementDTO p) {
        Departement pRet = null;
        if (p != null) {
            pRet = new Departement();
            if (p.getId() != 0) {
                pRet.setId(new Long(p.getId()));
            }
            pRet.setVersion(p.getVersion());
            pRet.setSigle(p.getSigle());
            pRet.setNom(p.getNom());
            pRet.setDescription((p.getDescription()));
            pRet.setStatus(p.getStatus());
            pRet.setLogodata(p.getLogodata());
        }// p
        return pRet;
    }// convertDepartement

    public DepartementDTO convertDepartement(Departement p) {
        DepartementDTO pRet = null;
        if (p != null) {
            pRet = new DepartementDTO();
            pRet.setId(p.getId().intValue());
            pRet.setVersion(p.getVersion().intValue());
            pRet.setSigle(p.getSigle());
            pRet.setNom(p.getNom());
            pRet.setDescription((p.getDescription()));
            pRet.setStatus(p.getStatus());
            byte[] data = (byte[]) p.getLogodata();
            pRet.setLogodata(data);
        }// p
        return pRet;
    }// convertDepartement
    public Annee convertAnnee(AnneeDTO p){
        Annee pRet = null;
        if (p != null){
            pRet = new Annee();
             if (p.getId() != 0) {
                pRet.setId(new Long(p.getId()));
            }
            Departement dep = new Departement();
            dep.setId(new Long(p.getDepartementid()));
            pRet.setDepartement(dep);
            pRet.setSigle(p.getSigle());
            pRet.setDescription((p.getDescription()));
            pRet.setStatus(p.getStatus());
            pRet.setStartdate(convertDate(p.getStartdate()));
            pRet.setEnddate(convertDate(p.getEnddate()));
        }
        return pRet;
    }// convertAnnee
    public AnneeDTO convertAnnee(Annee p){
        AnneeDTO pRet = null;
        if (p != null){
            pRet = new AnneeDTO();
            pRet.setId(p.getId().intValue());
            pRet.setVersion(p.getVersion().intValue());
            pRet.setDepartementid(p.getDepartement().getId().intValue());
            pRet.setSigle(p.getSigle());
            pRet.setDescription((p.getDescription()));
            pRet.setStatus(p.getStatus());
            pRet.setStartdate(convertDate(p.getStartdate()));
            pRet.setEnddate(convertDate(p.getEnddate()));
        }
        return pRet;
    }// convertAnnee
}// BaseDataBean
