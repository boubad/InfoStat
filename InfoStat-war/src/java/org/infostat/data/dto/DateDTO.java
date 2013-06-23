/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infostat.data.dto;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author boubad
 */
@XmlRootElement(name="datetime")
public class DateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    //
    public DateDTO() {
    }

    public DateDTO(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateDTO(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    //
    @XmlAttribute
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
     @XmlAttribute
    public int getMonth() {
        return month;
    }
     
    public void setMonth(int month) {
        this.month = month;
    }
     @XmlAttribute
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
     @XmlAttribute
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
     @XmlAttribute
    public int getMinute() {
        return minute;
    }
     
    public void setMinute(int minute) {
        this.minute = minute;
    }
     @XmlAttribute
    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.year;
        hash = 71 * hash + this.month;
        hash = 71 * hash + this.day;
        hash = 71 * hash + this.hour;
        hash = 71 * hash + this.minute;
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
        final DateDTO other = (DateDTO) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        if (this.hour != other.hour) {
            return false;
        }
        if (this.minute != other.minute) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DateDTO{" + "year=" + year + ", month=" + month + ", day=" + day + ", hour=" + hour + ", minute=" + minute + '}';
    }
    
}// class DateDTO
