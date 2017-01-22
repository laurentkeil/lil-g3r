package com.mdl.beans;

import java.sql.Date;

/**
 * 
 *
 */
public class Statut_client {
    private int    id;
    private String statut;
    private String cause;
    private String client;
    private String colis;
    private String employe;
    private String ICU;
    private Date   date;
    private String mail;
    private String nom;
    private String prenom;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut( String statut ) {
        this.statut = statut;
    }

    public String getCause() {
        return cause;
    }

    public void setCause( String cause ) {
        this.cause = cause;
    }

    public String getClient() {
        return client;
    }

    public void setClient( String client ) {
        this.client = client;
    }

    public String getColis() {
        return colis;
    }

    public void setColis( String colis ) {
        this.colis = colis;
    }

    public String getEmploye() {
        return employe;
    }

    public void setEmploye( String employe ) {
        this.employe = employe;
    }

    public String getICU() {
        return ICU;
    }

    public void setICU( String iCU ) {
        ICU = iCU;
    }

    public Date getDate() {
        return date;
    }

    public void setDate( Date date ) {
        this.date = date;
    }

    public String getMail() {
        return mail;
    }

    public void setMail( String mail ) {
        this.mail = mail;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }
}
