package com.mdl.beans;

import java.sql.Date;

/**
 * 
 * bean Employe contenant les informations sur un employé de HLB-Express. Setter
 * permettant d'y déposer l'information et getter permettant de la récupérer.
 * 
 */

public class Employe {
    private Integer id;
    private String  email;
    private String  motDePasse;
    private String  sexe;
    private String  nom;
    private String  prenom;
    private String  adresse_rue;
    private String  adresse_num;
    private String  adresse_boite;
    private String  adresse_loc;
    private String  adresse_code;
    private String  pays;
    private String  jourNaissance;
    private String  moisNaissance;
    private String  anneeNaissance;
    private Date    date_naissance;
    private String  telephonePortable;
    private String  telephoneFixe;
    private String  statutMarital;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse( String motDePasse ) {
        this.motDePasse = motDePasse;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe( String sexe ) {
        this.sexe = sexe;
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

    public String getAdresse_rue() {
        return adresse_rue;
    }

    public void setAdresse_rue( String adresse_rue ) {
        this.adresse_rue = adresse_rue;
    }

    public String getAdresse_num() {
        return adresse_num;
    }

    public void setAdresse_num( String adresse_num ) {
        this.adresse_num = adresse_num;
    }

    public String getAdresse_boite() {
        return adresse_boite;
    }

    public void setAdresse_boite( String adresse_boite ) {
        this.adresse_boite = adresse_boite;
    }

    public String getAdresse_loc() {
        return adresse_loc;
    }

    public void setAdresse_loc( String adresse_loc ) {
        this.adresse_loc = adresse_loc;
    }

    public String getAdresse_code() {
        return adresse_code;
    }

    public void setAdresse_code( String adresse_code ) {
        this.adresse_code = adresse_code;
    }

    public String getPays() {
        return pays;
    }

    public void setPays( String pays ) {
        this.pays = pays;
    }

    public String getJourNaissance() {
        return jourNaissance;
    }

    public void setJourNaissance( String jourNaissance ) {
        this.jourNaissance = jourNaissance;
    }

    public String getMoisNaissance() {
        return moisNaissance;
    }

    public void setMoisNaissance( String moisNaissance ) {
        this.moisNaissance = moisNaissance;
    }

    public String getAnneeNaissance() {
        return anneeNaissance;
    }

    public void setAnneeNaissance( String anneeNaissance ) {
        this.anneeNaissance = anneeNaissance;
    }

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance( Date date_naissance ) {
        this.date_naissance = date_naissance;
    }

    public String getTelephonePortable() {
        return telephonePortable;
    }

    public void setTelephonePortable( String telephonePortable ) {
        this.telephonePortable = telephonePortable;
    }

    public String getTelephoneFixe() {
        return telephoneFixe;
    }

    public void setTelephoneFixe( String telephoneFixe ) {
        this.telephoneFixe = telephoneFixe;
    }

    public String getStatutMarital() {
        return statutMarital;
    }

    public void setStatutMarital( String statutMarital ) {
        this.statutMarital = statutMarital;
    }

}
