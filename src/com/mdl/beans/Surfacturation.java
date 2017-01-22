package com.mdl.beans;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 
 * Bean Surfacturation stockant les données concernant une surfacturation sur
 * une commande d'un client, son prix, sa date de notification et de paiement
 * etc.
 * 
 */
public class Surfacturation {

    private int       id;
    private int       id_client;
    private double    poids_reel;
    private double    poids_renseigne;
    private double    prix_before;
    private double    prix_surfact;
    private double    prix_total;
    private String    colis;
    /**
     * l'identifiant unique d'une commande générée dans nos applications.
     */
    private String    ICU;
    private Date      date_paiement;
    private Date      date_notification;
    private int       jourRestant;
    private String    nom;
    private String    prenom;
    private String    mail;
    private String    pays;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public double getPoids_reel() {
        return poids_reel;
    }

    public void setPoids_reel( double poids_reel ) {
        this.poids_reel = poids_reel;
    }

    public double getPoids_renseigne() {
        return poids_renseigne;
    }

    public void setPoids_renseigne( double poids_renseigne ) {
        this.poids_renseigne = poids_renseigne;
    }

    public Date getDate_paiement() {
        return date_paiement;
    }

    public void setDate_paiement( Date date_paiement ) {
        this.date_paiement = date_paiement;
    }

    public Date getDate_notification() {
        return date_notification;
    }

    public void setDate_notification( Date date_notification ) {
        this.date_notification = date_notification;
    }

    public String getColis() {
        return colis;
    }

    public void setColis( String colis ) {
        this.colis = colis;
    }

    public String getICU() {
        return ICU;
    }

    public void setICU( String iCU ) {
        ICU = iCU;
    }

    public double getPrix_before() {
        return prix_before;
    }

    public void setPrix_before( double prix_before ) {
        this.prix_before = prix_before;
    }

    public double getPrix_surfact() {
        return prix_surfact;
    }

    public void setPrix_surfact( double prix_surfact ) {
        this.prix_surfact = prix_surfact;
    }

    public double getPrix_total() {
        return prix_total;
    }

    public void setPrix_total( double prix_total ) {
        this.prix_total = prix_total;
    }

    public int getJourRestant() {
        return jourRestant;
    }

    public void setJourRestant( int jourRestant ) {
        this.jourRestant = jourRestant;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client( int id_client ) {
        this.id_client = id_client;
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

    public String getMail() {
        return mail;
    }

    public void setMail( String mail ) {
        this.mail = mail;
    }

    public String getPays() {
        return pays;
    }

    public void setPays( String pays ) {
        this.pays = pays;
    }
}
