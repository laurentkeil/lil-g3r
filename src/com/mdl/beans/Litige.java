package com.mdl.beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * bean Litige contenant les informations sur un litige d'un utilisateur de
 * l'application web. Setter permettant d'y déposer l'information et getter
 * permettant de la récupérer.
 * 
 */

public class Litige {
    private String       id;
    private String       objet;
    private String       description;
    private String       colis;
    private String       type;
    private String       statut;
    private String       dateCreation;
    private String       dateReponse;
    private String       date;
    private List<Litige> detail;
    private boolean      reponse;
    private String       mail;
    private String       nom;
    private String       prenom;
    private String       descriptionCoursier;
    private String       nomCoursier;
    private String       prenomCoursier;
    private String       verrou;
    private String       numLitigeCoursier;
    private String       ICU;

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet( String objet ) {
        this.objet = objet;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getColis() {
        return colis;
    }

    public void setColis( String colis ) {
        this.colis = colis;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut( String statut ) {
        this.statut = statut;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation( String string ) {
        this.dateCreation = string;
    }

    public String getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse( String dateReponse ) {
        this.dateReponse = dateReponse;
    }

    public List<Litige> getDetail() {
        return detail;
    }

    public void setDetail( List<Litige> detail ) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date ) {
        this.date = date;
    }

    public boolean isReponse() {
        return reponse;
    }

    public void setReponse( boolean reponse ) {
        this.reponse = reponse;
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

    public String getDescriptionCoursier() {
        return descriptionCoursier;
    }

    public void setDescriptionCoursier( String descriptionCoursier ) {
        this.descriptionCoursier = descriptionCoursier;
    }

    public String getNomCoursier() {
        return nomCoursier;
    }

    public void setNomCoursier( String nomCoursier ) {
        this.nomCoursier = nomCoursier;
    }

    public String getPrenomCoursier() {
        return prenomCoursier;
    }

    public void setPrenomCoursier( String prenomCoursier ) {
        this.prenomCoursier = prenomCoursier;
    }

    public String getVerrou() {
        return verrou;
    }

    public void setVerrou( String verrou ) {
        this.verrou = verrou;
    }

    public String getNumLitigeCoursier() {
        return numLitigeCoursier;
    }

    public void setNumLitigeCoursier( String numLitigeCoursier ) {
        this.numLitigeCoursier = numLitigeCoursier;
    }

    public String getICU() {
        return ICU;
    }

    public void setICU( String iCU ) {
        ICU = iCU;
    }

}
