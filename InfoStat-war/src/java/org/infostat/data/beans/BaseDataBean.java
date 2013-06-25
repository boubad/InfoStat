/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import org.infostat.data.dto.AffectationEnseignantDTO;
import org.infostat.data.dto.AffectationEtudiantDTO;
import org.infostat.data.dto.AnneeDTO;
import org.infostat.data.dto.DateDTO;
import org.infostat.data.dto.DepartementDTO;
import org.infostat.data.dto.EnseignantDTO;
import org.infostat.data.dto.EtudiantDTO;
import org.infostat.data.dto.EtudiantEventDTO;
import org.infostat.data.dto.GroupeDTO;
import org.infostat.data.dto.GroupeEventDTO;
import org.infostat.data.dto.MatiereDTO;
import org.infostat.data.dto.SemestreDTO;
import org.infostat.data.dto.UniteDTO;
import org.infostat.data.entities.AffectationEnseignant;
import org.infostat.data.entities.AffectationEtudiant;
import org.infostat.data.entities.Annee;
import org.infostat.data.entities.Departement;
import org.infostat.data.entities.Enseignant;
import org.infostat.data.entities.Etudiant;
import org.infostat.data.entities.EtudiantEvent;
import org.infostat.data.entities.Groupe;
import org.infostat.data.entities.GroupeEvent;
import org.infostat.data.entities.Matiere;
import org.infostat.data.entities.Semestre;
import org.infostat.data.entities.Unite;

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
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId()));
            }
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

    public Annee convertAnnee(AnneeDTO p) {
        Annee pRet = null;
        if (p != null) {
            pRet = new Annee();
            if (p.getId() != null) {
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

    public AnneeDTO convertAnnee(Annee p) {
        AnneeDTO pRet = null;
        if (p != null) {
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

    public Semestre convertSemestre(SemestreDTO p) {
        Semestre pRet = null;
        if (p != null) {
            pRet = new Semestre();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getAnneeid().longValue()));
            }
            Annee annee = new Annee();
            if (p.getAnneeid() != null) {
                annee.setId(new Long(p.getId().longValue()));
            }
            pRet.setAnnee(annee);
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setStartdate(convertDate(p.getStartdate()));
            pRet.setEnddate(convertDate(p.getEnddate()));
        }
        return pRet;
    }

    public SemestreDTO convertSemestre(Semestre p) {
        SemestreDTO pRet = null;
        if (p != null) {
            pRet = new SemestreDTO();
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setVersion(p.getVersion());
            pRet.setAnneeid(new Integer(p.getAnnee().getId().intValue()));
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setStartdate(convertDate(p.getStartdate()));
            pRet.setEnddate(convertDate(p.getEnddate()));
        }
        return pRet;
    }

    public Unite convertUnite(UniteDTO p) {
        Unite pRet = null;
        if (p != null) {
            pRet = new Unite();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId().longValue()));
            }
            Departement dep = new Departement();
            if (p.getDepartementid() != null) {
                dep.setId(new Long(p.getDepartementid().longValue()));
            }
            pRet.setDepartement(dep);
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setNom(p.getNom());
            pRet.setOrder(p.getOrder());
        }
        return pRet;
    }

    public UniteDTO convertUnite(Unite p) {
        UniteDTO pRet = null;
        if (p != null) {
            pRet = new UniteDTO();
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setVersion(p.getVersion());
            pRet.setDepartementid(new Integer(p.getDepartement().getId().intValue()));
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setNom(p.getNom());
            pRet.setOrder(p.getOrder());
        }
        return pRet;
    }

    public Matiere convertMatiere(MatiereDTO p) {
        Matiere pRet = null;
        if (p != null) {
            pRet = new Matiere();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId().longValue()));
            }
            Unite un = new Unite();
            if (p.getUniteid() != null) {
                un.setId(new Long(p.getUniteid().longValue()));
            }
            pRet.setUnite(un);
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setNom(p.getNom());
            pRet.setOrder(p.getOrder());
        }
        return pRet;
    }

    public MatiereDTO convertMatiere(Matiere p) {
        MatiereDTO pRet = null;
        if (p != null) {
            pRet = new MatiereDTO();
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setVersion(p.getVersion());
            pRet.setUniteid(new Integer(p.getUnite().getId().intValue()));
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setNom(p.getNom());
            pRet.setOrder(p.getOrder());
        }
        return pRet;
    }
    //

    public Groupe convertGroupe(GroupeDTO p) {
        Groupe pRet = null;
        if (p != null) {
            pRet = new Groupe();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId().longValue()));
            }
            Departement dep = new Departement();
            if (p.getDepartementid() != null) {
                dep.setId(new Long(p.getDepartementid().longValue()));
            }
            pRet.setDepartement(dep);
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setNom(p.getNom());
        }
        return pRet;
    }

    public GroupeDTO convertGroupe(Groupe p) {
        GroupeDTO pRet = null;
        if (p != null) {
            pRet = new GroupeDTO();
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setVersion(p.getVersion());
            pRet.setDepartementid(new Integer(p.getDepartement().getId().intValue()));
            pRet.setSigle(p.getSigle());
            pRet.setStatus(p.getStatus());
            pRet.setDescription(p.getDescription());
            pRet.setNom(p.getNom());
        }
        return pRet;
    }

    public Enseignant convertEnseignant(EnseignantDTO p) {
        Enseignant pRet = null;
        if (p != null) {
            pRet = new Enseignant();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId().longValue()));
            }
            pRet.setDescription(p.getDescription());
            pRet.setFirstname(p.getFirstname());
            pRet.setLastname(p.getLastname());
            pRet.setPassword(p.getPassword());
            pRet.setPhotodata(p.getPhotodata());
            pRet.setStatus(p.getStatus());
            pRet.setUsername(p.getUsername());
        }
        return pRet;
    }

    public EnseignantDTO convertEnseignant(Enseignant p) {
        EnseignantDTO pRet = null;
        if (p != null) {
            pRet = new EnseignantDTO();
            pRet.setVersion(p.getVersion());
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setDescription(p.getDescription());
            pRet.setFirstname(p.getFirstname());
            pRet.setLastname(p.getLastname());
            pRet.setPassword(p.getPassword());
            pRet.setPhotodata(p.getPhotodata());
            pRet.setStatus(p.getStatus());
            pRet.setUsername(p.getUsername());
        }
        return pRet;
    }

    public Etudiant convertEtudiant(EtudiantDTO p) {
        Etudiant pRet = null;
        if (p != null) {
            pRet = new Etudiant();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId().longValue()));
            }
            pRet.setEtudessuperieures(p.getEtudessuperieures());
            pRet.setDescription(p.getDescription());
            pRet.setLycee(p.getLycee());
            pRet.setFirstname(p.getFirstname());
            pRet.setLastname(p.getLastname());
            pRet.setPhotodata(p.getPhotodata());
            pRet.setStatus(p.getStatus());
            pRet.setUsername(p.getUsername());
            pRet.setMentionbac(p.getMentionbac());
            pRet.setOptionbac(p.getOptionbac());
            pRet.setRedoublant(p.getRedoublant());
            pRet.setSeriebac(p.getSeriebac());
            pRet.setVille(p.getVille());
        }
        return pRet;
    }

    public EtudiantDTO convertEtudiant(Etudiant p) {
        EtudiantDTO pRet = null;
        if (p != null) {
            pRet = new EtudiantDTO();
            pRet.setVersion(p.getVersion());
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setEtudessuperieures(p.getEtudessuperieures());
            pRet.setDescription(p.getDescription());
            pRet.setLycee(p.getLycee());
            pRet.setFirstname(p.getFirstname());
            pRet.setLastname(p.getLastname());
            pRet.setPhotodata(p.getPhotodata());
            pRet.setStatus(p.getStatus());
            pRet.setUsername(p.getUsername());
            pRet.setMentionbac(p.getMentionbac());
            pRet.setOptionbac(p.getOptionbac());
            pRet.setRedoublant(p.getRedoublant());
            pRet.setSeriebac(p.getSeriebac());
            pRet.setVille(p.getVille());
        }
        return pRet;
    }

    public AffectationEnseignant convertAffectationEnseignant(AffectationEnseignantDTO p) {
        AffectationEnseignant pRet = null;
        if (p != null) {
            pRet = new AffectationEnseignant();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId().longValue()));
            }
            Semestre pSem = new Semestre();
            Matiere pMat = new Matiere();
            Groupe pGroupe = new Groupe();
            Enseignant prof = new Enseignant();
            if (p.getSemestreid() != null) {
                pSem.setId(new Long(p.getSemestreid().longValue()));
            }
            if (p.getEnseignantid() != null) {
                prof.setId(new Long(p.getEnseignantid().longValue()));
            }
            if (p.getMatiereid() != null) {
                pMat.setId(new Long(p.getMatiereid().longValue()));
            }
            if (p.getGroupeid() != null) {
                pGroupe.setId(new Long(p.getGroupeid().longValue()));
            }
            pRet.setSemestre(pSem);
            pRet.setEnseignant(prof);
            pRet.setMatiere(pMat);
            pRet.setGroupe(pGroupe);
        }
        return pRet;
    }

    public AffectationEnseignantDTO convertAffectationEnseignant(AffectationEnseignant p) {
        AffectationEnseignantDTO pRet = null;
        if (p != null) {
            pRet.setId(p.getId().intValue());
            pRet.setVersion(p.getVersion());
            pRet.setEnseignantid(p.getEnseignant().getId().intValue());
            pRet.setGroupeid(p.getGroupe().getId().intValue());
            pRet.setMatiereid(p.getMatiere().getId().intValue());
            pRet.setSemestreid(p.getSemestre().getId().intValue());
        }
        return pRet;
    }

    public AffectationEtudiant convertAffectationEtudiant(AffectationEtudiantDTO p) {
        AffectationEtudiant pRet = null;
        if (p != null) {
            pRet = new AffectationEtudiant();
            if (p.getId() != null) {
                pRet.setId(new Long(p.getId().longValue()));
            }
            Semestre pSem = new Semestre();
            Groupe pGroupe = new Groupe();
            Etudiant etud = new Etudiant();
            if (p.getSemestreid() != null) {
                pSem.setId(new Long(p.getSemestreid().longValue()));
            }
            if (p.getEtudiantid() != null) {
                etud.setId(new Long(p.getEtudiantid().longValue()));
            }
            if (p.getGroupeid() != null) {
                pGroupe.setId(new Long(p.getGroupeid().longValue()));
            }
            pRet.setSemestre(pSem);
            pRet.setEtudiant(etud);
            pRet.setGroupe(pGroupe);
        }
        return pRet;
    }

    public AffectationEtudiantDTO convertAffectationEtudiant(AffectationEtudiant p) {
        AffectationEtudiantDTO pRet = null;
        if (p != null) {
            pRet.setId(p.getId().intValue());
            pRet.setVersion(p.getVersion());
            pRet.setEtudiantid(p.getEtudiant().getId().intValue());
            pRet.setGroupeid(p.getGroupe().getId().intValue());
            pRet.setSemestreid(p.getSemestre().getId().intValue());
        }
        return pRet;
    }

    public GroupeEvent convertGroupeEvent(GroupeEventDTO p) {
        GroupeEvent pRet = null;
        if (p != null) {
            pRet = new GroupeEvent();
            AffectationEnseignant aff = new AffectationEnseignant();
            if (p.getAffectationenseignantid() != null) {
                aff.setId(new Long(p.getAffectationenseignantid().longValue()));
            }
            pRet.setAffectationenseignant(aff);
            pRet.setCoefficient(p.getCoefficient());
            pRet.setDate(convertDate(p.getDate()));
            pRet.setDescription(p.getDescription());
            pRet.setEndtime(convertDate(p.getEndtime()));
            pRet.setGenre(p.getGenre());
            pRet.setLocation(p.getLocation());
            pRet.setNom(p.getNom());
            pRet.setStarttime(convertDate(p.getStarttime()));
            pRet.setStatus(p.getStatus());
        }
        return pRet;
    }

    public GroupeEventDTO convertGroupeEvent(GroupeEvent p) {
        GroupeEventDTO pRet = null;
        if (p != null) {
            pRet = new GroupeEventDTO();
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setVersion(p.getVersion());
            pRet.setAffectationenseignantid(new Integer(p.getAffectationenseignant().getId().intValue()));
            pRet.setCoefficient(p.getCoefficient());
            pRet.setDate(convertDate(p.getDate()));
            pRet.setDescription(p.getDescription());
            pRet.setEndtime(convertDate(p.getEndtime()));
            pRet.setGenre(p.getGenre());
            pRet.setLocation(p.getLocation());
            pRet.setNom(p.getNom());
            pRet.setStarttime(convertDate(p.getStarttime()));
            pRet.setStatus(p.getStatus());
        }
        return pRet;
    }

    public EtudiantEvent convertEtudiantEvent(EtudiantEventDTO p) {
        EtudiantEvent pRet = null;
        if (p != null) {
            pRet = new EtudiantEvent();
            GroupeEvent aff = new GroupeEvent();
            if (p.getGroupeventid() != null) {
                aff.setId(new Long(p.getGroupeventid().longValue()));
            }
            pRet.setGroupevent(aff);
            Etudiant etud = new Etudiant();
            if (p.getEtudiantid() != null) {
                etud.setId(new Long(p.getEtudiantid().longValue()));
            }
            pRet.setEtudiant(etud);
            pRet.setGenre(p.getGenre());
            pRet.setNote(p.getNote());
            pRet.setObservations(p.getObservations());
        }
        return pRet;
    }

    public EtudiantEventDTO convertEtudiantEvent(EtudiantEvent p) {
        EtudiantEventDTO pRet = null;
        if (p != null) {
            pRet = new EtudiantEventDTO();
            pRet.setId(new Integer(p.getId().intValue()));
            pRet.setVersion(p.getVersion());
            pRet.setGroupeventid(new Integer(p.getGroupevent().getId().intValue()));
            pRet.setEtudiantid(new Integer(p.getEtudiant().getId().intValue()));
            pRet.setGenre(p.getGenre());
            pRet.setNote(p.getNote());
            pRet.setObservations(p.getObservations());
        }
        return pRet;
    }
}// BaseDataBean
