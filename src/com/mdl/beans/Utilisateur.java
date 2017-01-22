package com.mdl.beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

/**
 * 
 * bean Utilisateur contenant les informations sur un utilisateur enregistré de
 * l'application web. Setter permettant d'y déposer l'information et getter
 * permettant de la récupérer.
 * 
 */

public class Utilisateur {

    private Integer                id;
    private String                 email;
    private String                 motDePasse;
    private String                 sexe;
    private String                 nom;
    private String                 prenom;
    private String                 adresse_rue;
    private String                 adresse_num;
    private String                 adresse_boite;
    private String                 adresse_loc;
    private String                 adresse_code;
    private String                 pays;
    private String                 jourNaissance;
    private String                 moisNaissance;
    private String                 anneeNaissance;
    private Date                   date_naissance;
    private String                 telephonePortable;
    private String                 telephoneFixe;
    private String                 num_tva;
    private String                 validation;
    private Date                   dateInscription;
    private Map<Integer, Commande> commandes;
    private String                 statut;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe( String sexe ) {
        this.sexe = sexe;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setMotDePasse( String motDePasse ) {
        this.motDePasse = motDePasse;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setAdresseRue( String adresse_rue ) {
        this.adresse_rue = adresse_rue;
    }

    public String getAdresseRue() {
        return adresse_rue;
    }

    public void setAdresseNum( String adresse_num ) {
        this.adresse_num = adresse_num;
    }

    public String getAdresseNum() {
        return adresse_num;
    }

    public void setAdresseBoite( String adresse_boite ) {
        this.adresse_boite = adresse_boite;
    }

    public String getAdresseBoite() {
        return adresse_boite;
    }

    public void setAdresseLoc( String adresse_loc ) {
        this.adresse_loc = adresse_loc;
    }

    public String getAdresseLoc() {
        return adresse_loc;
    }

    public void setAdresseCode( String adresse_code ) {
        this.adresse_code = adresse_code;
    }

    public String getAdresseCode() {
        return adresse_code;
    }

    public void setAdressePays( String pays ) {
        this.pays = pays;
    }

    public String getAdressePays() {
        return pays;
    }

    public void setTelephonePortable( String telephone ) {
        this.telephonePortable = telephone;
    }

    public String getTelephonePortable() {
        return telephonePortable;
    }

    public String getNum_tva() {
        return num_tva;
    }

    public void setNum_tva( String num_tva ) {
        this.num_tva = num_tva;
    }

    public void setJourNaissance( String jourNaissance ) {
        this.jourNaissance = jourNaissance;
    }

    public String getJourNaissance() {
        return jourNaissance;
    }

    public void setMoisNaissance( String moisNaissance ) {
        this.moisNaissance = moisNaissance;
    }

    public String getMoisNaissance() {
        return moisNaissance;
    }

    public void setAnneeNaissance( String anneeNaissance ) {
        this.anneeNaissance = anneeNaissance;
    }

    public String getAnneeNaissance() {
        return anneeNaissance;
    }

    public void setDateNaissance( Date date_naissance ) {
        this.date_naissance = date_naissance;
    }

    public Date getDateNaissance() {
        return date_naissance;
    }

    public void setValidation( String validation ) {
        this.validation = validation;
    }

    public String getValidation() {
        return validation;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription( Date dateInscription ) {
        this.dateInscription = dateInscription;
    }

    public Map<Integer, Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes( Map<Integer, Commande> commandes ) {
        this.commandes = commandes;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut( String statut ) {
        this.statut = statut;
    }

    public String getTelephoneFixe() {
        return telephoneFixe;
    }

    public void setTelephoneFixe( String telephoneFixe ) {
        this.telephoneFixe = telephoneFixe;
    }
}
