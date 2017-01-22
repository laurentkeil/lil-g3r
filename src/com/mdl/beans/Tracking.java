package com.mdl.beans;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 
 * bean Tracking contenant les informations sur le tracking d'un colis d'un
 * utilisateur de l'application web. Setter permettant d'y déposer l'information
 * et getter permettant de la récupérer.
 * 
 */

public class Tracking {
    private String    colis;
    private String    date;
    private String    type_ope;
    private String    refus;
    private String    coursier;
    private String    centre;
    private String    partenaire;

    public String getColis() {
        return colis;
    }

    public void setColis( String colis ) {
        this.colis = colis;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date ) {
        this.date = date;
    }

    public String getType_ope() {
        return type_ope;
    }

    public void setType_ope( String type_ope ) {
        this.type_ope = type_ope;
    }

    public String getRefus() {
        return refus;
    }

    public void setRefus( String refus ) {
        this.refus = refus;
    }

    public String getCoursier() {
        return coursier;
    }

    public void setCoursier( String coursier ) {
        this.coursier = coursier;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre( String centre ) {
        this.centre = centre;
    }

    public String getPartenaire() {
        return partenaire;
    }

    public void setPartenaire( String partenaire ) {
        this.partenaire = partenaire;
    }

}
