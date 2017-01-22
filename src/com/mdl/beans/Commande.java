package com.mdl.beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * bean Commande contenant les informations sur une commande d'un utilisateur de
 * l'application web Setter permettant d'y déposer l'information et getter
 * permettant de la récupérer.
 * 
 */
public class Commande {

    private Integer      id;

    /* information sur la commande */

    private boolean      add_carnet_exp;

    private String       nom_expediteur;
    private String       prenom_expediteur;
    private String       tel_expediteur;
    private String       adresse_num_expediteur;
    private String       adresse_rue_expediteur;
    private String       adresse_loc_expediteur;
    private String       adresse_code_expediteur;
    private String       adresse_boite_expediteur;

    private boolean      add_carnet_dest;
    private String       nom_destinataire;
    private String       prenom_destinataire;
    private String       tel_destinataire;
    private String       adresse_pays_destinataire;
    private String       adresse_num_destinataire;
    private String       adresse_rue_destinataire;
    private String       adresse_loc_destinataire;
    private String       adresse_code_destinataire;
    private String       adresse_boite_destinataire;

    private String       jourCommande;
    private String       moisCommande;
    private String       anneeCommande;
    private Date         date_pickup;
    private String       date_enregistrement;

    private List<String> listeVilles;

    private Integer      centre_exp_id;
    private Integer      centre_dest_id;

    /* information sur un colis */
    private Double       poids;
    private Integer      dimension_hauteur;
    private Integer      dimension_longueur;
    private Integer      dimension_largeur;
    private Double       valeurEstimee;
    private String       typeAssurance;
    private Boolean      accuseReception;

    private String       idColis;
    private String       icu;
    private String       statut;
    private int          statutCompte;

    private Double       prix;
    private Double       prixBase;
    private Double       prixCentreTraverse;
    private Double       prixAssurance;
    private Double       prixAccuse;

    private String       client;

    private String       pays;

    /**
     * @return l'id de la commande
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            = l'id de la commande à placer
     */
    public void setId( int id ) {
        this.id = id;
    }

    /**
     * @return true si l'utilisateur souhaite ajouter l'adresse d'expédition
     *         dans le carnet, false sinon
     */
    public boolean isAdd_carnet_exp() {
        return add_carnet_exp;
    }

    /**
     * @param add_carnet_exp
     *            = true si l'utilisateur souhaite ajouter l'adresse
     *            d'expédition dans le carnet, false sinon
     */
    public void setAdd_carnet_exp( boolean add_carnet_exp ) {
        this.add_carnet_exp = add_carnet_exp;
    }

    /**
     * @return the adresse_num_expediteur
     */
    public String getAdresse_num_expediteur() {
        return adresse_num_expediteur;
    }

    /**
     * @param adresse_num_expediteur
     *            the adresse_num_expediteur to set
     */
    public void setAdresse_num_expediteur( String adresse_num_expediteur ) {
        this.adresse_num_expediteur = adresse_num_expediteur;
    }

    /**
     * @return le nom de l'expéditeur de la commande
     */
    public String getNom_expediteur() {
        return nom_expediteur;
    }

    /**
     * 
     * @param nom_expediteur
     *            le nom de l'expéditeur de la commande
     */
    public void setNom_expediteur( String nom_expediteur ) {
        this.nom_expediteur = nom_expediteur;
    }

    /**
     * 
     * @return prenom_expediteur le prenom de l'expéditeur de la commande
     */
    public String getPrenom_expediteur() {
        return prenom_expediteur;
    }

    /**
     * 
     * @param prenom_expediteur
     *            le prenom de l'expéditeur de la commande
     */
    public void setPrenom_expediteur( String prenom_expediteur ) {
        this.prenom_expediteur = prenom_expediteur;
    }

    /**
     * 
     * @return le numéro de téléphone de l'expéditeur
     */
    public String getTel_expediteur() {
        return tel_expediteur;
    }

    /**
     * 
     * @param tel_expediteur
     *            le numéro de téléphone de l'expéditeur
     */
    public void setTel_expediteur( String tel_expediteur ) {
        this.tel_expediteur = tel_expediteur;
    }

    /**
     * @return the adresse_rue_expediteur
     */
    public String getAdresse_rue_expediteur() {
        return adresse_rue_expediteur;
    }

    /**
     * @param adresse_rue_expediteur
     *            the adresse_rue_expediteur to set
     */
    public void setAdresse_rue_expediteur( String adresse_rue_expediteur ) {
        this.adresse_rue_expediteur = adresse_rue_expediteur;
    }

    /**
     * @return the adresse_loc_expediteur
     */
    public String getAdresse_loc_expediteur() {
        return adresse_loc_expediteur;
    }

    /**
     * @param adresse_loc_expediteur
     *            the adresse_loc_expediteur to set
     */
    public void setAdresse_loc_expediteur( String adresse_loc_expediteur ) {
        this.adresse_loc_expediteur = adresse_loc_expediteur;
    }

    /**
     * @return the adresse_code_expediteur
     */
    public String getAdresse_code_expediteur() {
        return adresse_code_expediteur;
    }

    /**
     * @param adresse_code_expediteur
     *            the adresse_code_expediteur to set
     */
    public void setAdresse_code_expediteur( String adresse_code_expediteur ) {
        this.adresse_code_expediteur = adresse_code_expediteur;
    }

    /**
     * @return the adresse_pays_destinataire
     */
    public String getAdresse_pays_destinataire() {
        return adresse_pays_destinataire;
    }

    /**
     * @param adresse_pays_destinataire
     *            the adresse_pays_destinataire to set
     */
    public void setAdresse_pays_destinataire( String adresse_pays_destinataire ) {
        this.adresse_pays_destinataire = adresse_pays_destinataire;
    }

    /**
     * @return the adresse_boite_expediteur
     */
    public String getAdresse_boite_expediteur() {
        return adresse_boite_expediteur;
    }

    /**
     * @param adresse_boite_expediteur
     *            the adresse_boite_expediteur to set
     */
    public void setAdresse_boite_expediteur( String adresse_boite_expediteur ) {
        this.adresse_boite_expediteur = adresse_boite_expediteur;
    }

    /**
     * 
     * @return true si l'utilisateur souhaite ajouter l'adresse de destination
     *         dans le carnet d'adresses, false sinon.
     */
    public boolean isAdd_carnet_dest() {
        return add_carnet_dest;
    }

    /**
     * 
     * @param add_carnet_dest
     *            = true si l'utilisateur souhaite ajouter l'adresse de
     *            destination dans le carnet d'adresses, false sinon.
     */
    public void setAdd_carnet_dest( boolean add_carnet_dest ) {
        this.add_carnet_dest = add_carnet_dest;
    }

    /**
     * @return the adresse_num_destinataire
     */
    public String getAdresse_num_destinataire() {
        return adresse_num_destinataire;
    }

    /**
     * @param adresse_num_destinataire
     *            the adresse_num_destinataire to set
     */
    public void setAdresse_num_destinataire( String adresse_num_destinataire ) {
        this.adresse_num_destinataire = adresse_num_destinataire;
    }

    /**
     * @return the adresse_rue_destinataire
     */
    public String getAdresse_rue_destinataire() {
        return adresse_rue_destinataire;
    }

    /**
     * @param adresse_rue_destinataire
     *            the adresse_rue_destinataire to set
     */
    public void setAdresse_rue_destinataire( String adresse_rue_destinataire ) {
        this.adresse_rue_destinataire = adresse_rue_destinataire;
    }

    /**
     * @return the adresse_loc_destinataire
     */
    public String getAdresse_loc_destinataire() {
        return adresse_loc_destinataire;
    }

    /**
     * @param adresse_loc_destinataire
     *            the adresse_loc_destinataire to set
     */
    public void setAdresse_loc_destinataire( String adresse_loc_destinataire ) {
        this.adresse_loc_destinataire = adresse_loc_destinataire;
    }

    /**
     * @return the adresse_code_destinataire
     */
    public String getAdresse_code_destinataire() {
        return adresse_code_destinataire;
    }

    /**
     * @param adresse_code_destinataire
     *            the adresse_code_destinataire to set
     */
    public void setAdresse_code_destinataire( String adresse_code_destinataire ) {
        this.adresse_code_destinataire = adresse_code_destinataire;
    }

    /**
     * @return the adresse_boite_destinaire
     */
    public String getAdresse_boite_destinataire() {
        return adresse_boite_destinataire;
    }

    /**
     * @param adresse_boite_destinaire
     *            the adresse_boite_destinaire to set
     */
    public void setAdresse_boite_destinataire( String adresse_boite_destinataire ) {
        this.adresse_boite_destinataire = adresse_boite_destinataire;
    }

    /**
     * @return the jourCommande
     */
    public String getJourCommande() {
        return jourCommande;
    }

    /**
     * @param jourCommande
     *            the jourCommande to set
     */
    public void setJourCommande( String jourCommande ) {
        this.jourCommande = jourCommande;
    }

    /**
     * @return the moisCommande
     */
    public String getMoisCommande() {
        return moisCommande;
    }

    /**
     * @param moisCommande
     *            the moisCommande to set
     */
    public void setMoisCommande( String moisCommande ) {
        this.moisCommande = moisCommande;
    }

    /**
     * @return the anneeCommande
     */
    public String getAnneeCommande() {
        return anneeCommande;
    }

    /**
     * @param anneeCommande
     *            the anneeCommande to set
     */
    public void setAnneeCommande( String anneeCommande ) {
        this.anneeCommande = anneeCommande;
    }

    /**
     * @param date_pickup
     *            la date à laquelle l'utilisateur souhaite faire livrer son
     *            colis.
     */
    public void setDatePickup( Date date_pickup ) {
        this.date_pickup = date_pickup;
    }

    /**
     * @return date_pickup la date à laquelle l'utilisateur a souhaité faire
     *         livrer son colis.
     */
    public Date getDatePickup() {
        return date_pickup;
    }

    /**
     * @return la date d'enregistrement de la commande avec précision en
     *         secondes.
     */
    public String getDate_enregistrement() {
        return date_enregistrement;
    }

    /**
     * @param la
     *            date d'enregistrement de la commande avec précision en
     *            secondes.
     */
    public void setDate_enregistrement( String object ) {
    	
        this.date_enregistrement = object;
    }

    /**
     * @return the poids
     */
    public Double getPoids() {
        return poids;
    }

    /**
     * @param poids
     *            the poids to set (qui sera tronqué à 3 chiffres après la
     *            virgule)
     */
    public void setPoids( Double poids ) {
        poids = truncate( poids, 3 );
        this.poids = poids;
    }

    /**
     * @return the valeurEstimee
     */
    public Double getValeurEstimee() {
        return valeurEstimee;
    }

    /**
     * @param valeurEstimee
     *            the valeurEstimee to set
     */
    public void setValeurEstimee( Double valeurEstimee ) {
        valeurEstimee = truncate( valeurEstimee, 2 );
        this.valeurEstimee = valeurEstimee;
    }

    /**
     * @return the typeAssurance
     */
    public String getTypeAssurance() {
        return typeAssurance;
    }

    /**
     * @param typeAssurance
     *            the typeAssurance to set
     */
    public void setTypeAssurance( String typeAssurance ) {
        this.typeAssurance = typeAssurance;
    }

    /**
     * @return the accuseReception
     */
    public Boolean getAccuseReception() {
        return accuseReception;
    }

    /**
     * @param accuseReception
     *            the accuseReception to set
     */
    public void setAccuseReception( Boolean accuseReception ) {
        this.accuseReception = accuseReception;
    }

    /**
     * @return le prix total de la commande.
     */
    public Double getPrix() {
        return prix;
    }

    /**
     * @param prix
     *            = le prix total de la commande, qui sera tronqué à 2 chiffres
     *            après la virgule.
     */
    public void setPrix( Double prix ) {
        prix = truncate( prix, 2 );
        this.prix = prix;
    }

    /**
     * @return le prix de base de la commande de l'utilisateur, prix en fonction
     *         du poids et du pays de destination.
     */
    public Double getPrixBase() {
        return prixBase;
    }

    /**
     * @param le
     *            prix de base de la commande de l'utilisateur, prix en fonction
     *            du poids et du pays de destination.
     */
    public void setPrixBase( Double prixBase ) {
        this.prixBase = prixBase;
    }

    /**
     * @return le sous-prix de la commande de l'utilisateur correspondant au
     *         nombre de centre traversé calculé par un webservice pour
     *         optimiser un chemin le plus court entre 2 adresses en Europe.
     */
    public Double getPrixCentreTraverse() {
        return prixCentreTraverse;
    }

    /**
     * @param prixCentreTraverse
     *            = le sous-prix de la commande de l'utilisateur correspondant
     *            au nombre de centre traversé calculé par un webservice pour
     *            optimiser un chemin le plus court entre 2 adresses en Europe.
     */
    public void setPrixCentreTraverse( Double prixCentreTraverse ) {
        this.prixCentreTraverse = prixCentreTraverse;
    }

    /**
     * @return le prix de l'assurance de la commande prise (ou non) par
     *         l'utilisateur.
     */
    public Double getPrixAssurance() {
        return prixAssurance;
    }

    /**
     * @param prixAssurance
     *            = le prix de l'assurance de la commande prise (ou non) par
     *            l'utilisateur.
     */
    public void setPrixAssurance( Double prixAssurance ) {
        this.prixAssurance = prixAssurance;
    }

    /**
     * @return le prix de l'accusé de la commande pris (ou non) par
     *         l'utilisateur.
     */
    public Double getPrixAccuse() {
        return prixAccuse;
    }

    /**
     * @param prixAccuse
     *            = le prix de l'accusé de la commande pris (ou non) par
     *            l'utilisateur.
     */
    public void setPrixAccuse( Double prixAccuse ) {
        this.prixAccuse = prixAccuse;
    }

    /**
     * @return le nom du destinataire
     */
    public String getNom_destinataire() {
        return nom_destinataire;
    }

    /**
     * 
     * @param nom_destinataire
     *            le nom du destinataire
     */
    public void setNom_destinataire( String nom_destinataire ) {
        this.nom_destinataire = nom_destinataire;
    }

    /**
     * 
     * @return le prenom du destinataire
     */
    public String getPrenom_destinataire() {
        return prenom_destinataire;
    }

    /**
     * 
     * @param prenom_destinataire
     *            le prenom du destinataire
     */
    public void setPrenom_destinataire( String prenom_destinataire ) {
        this.prenom_destinataire = prenom_destinataire;
    }

    /**
     * 
     * @return le numéro de téléphone du destinataire
     */
    public String getTel_destinataire() {
        return tel_destinataire;
    }

    /**
     * 
     * @param tel_destinataire
     *            le numéro de téléphone du destinataire
     */
    public void setTel_destinataire( String tel_destinataire ) {
        this.tel_destinataire = tel_destinataire;
    }

    /**
     * 
     * @return la hauteur du colis à livrer pour la commande.
     */
    public Integer getDimension_hauteur() {
        return dimension_hauteur;
    }

    /**
     * 
     * @param dimension_hauteur
     *            la hauteur du colis à livrer pour la commande.
     */
    public void setDimension_hauteur( Integer dimension_hauteur ) {
        this.dimension_hauteur = dimension_hauteur;
    }

    /**
     * 
     * @return la longueur du colis à livrer pour la commande.
     */
    public Integer getDimension_longueur() {
        return dimension_longueur;
    }

    /**
     * 
     * @param dimension_longueur
     *            la longueur du colis à livrer pour la commande.
     */
    public void setDimension_longueur( Integer dimension_longueur ) {
        this.dimension_longueur = dimension_longueur;
    }

    /**
     * @return la largeur du colis à livrer pour la commande.
     */
    public Integer getDimension_largeur() {
        return dimension_largeur;
    }

    /**
     * @param dimension_largeur
     *            la largeur du colis à livrer pour la commande.
     */
    public void setDimension_largeur( Integer dimension_largeur ) {
        this.dimension_largeur = dimension_largeur;
    }

    /**
     * @return l'id du colis généré de la commande.
     */
    public String getIdColis() {
        return idColis;
    }

    /**
     * 
     * @param idColis
     *            l'id du colis généré de la commande.
     */
    public void setIdColis( String idColis ) {
        this.idColis = idColis;
    }

    /**
     * @return l'identifiant du centre (canton) d'expédition du colis.
     */
    public Integer getCentre_exp_id() {
        return centre_exp_id;
    }

    /**
     * @param centre_exp_id
     *            l'identifiant du centre (canton) d'expédition du colis.
     */
    public void setCentre_exp_id( Integer centre_exp_id ) {
        this.centre_exp_id = centre_exp_id;
    }

    /**
     * @return l'identifiant du centre (canton) de destination du colis.
     */
    public Integer getCentre_dest_id() {
        return centre_dest_id;
    }

    /**
     * @param centre_exp_id
     *            l'identifiant du centre (canton) de destination du colis.
     */
    public void setCentre_dest_id( Integer centre_dest_id ) {
        this.centre_dest_id = centre_dest_id;
    }

    /**
     * @return la liste des villes des pays d'Europe fournies par le webservice
     */
    public List<String> getListeVilles() {
        return listeVilles;
    }

    /**
     * @param listeVilles
     *            la liste des villes des pays d'Europe fournies par le
     *            webservice
     */
    public void setListeVilles( List<String> listeVilles ) {
        this.listeVilles = listeVilles;
    }

    /**
     * 
     * @return l'icu (l'identifiant unique généré) de la commande de
     *         l'utilisateur.
     */
    public String getIcu() {
        return icu;
    }

    /**
     * 
     * @param l
     *            'icu (l'identifiant unique généré) de la commande de
     *            l'utilisateur.
     */
    public void setIcu( String icu ) {
        this.icu = icu;
    }

    /**
     * 
     * @return le statut de la commande : En attente de prise en charge, Pick-up
     *         prévu, Collecté, En attente de dépot par le client au centre, En
     *         cours de transit, En attente de livraison, En attente de 2e
     *         livraison, Livré, Livré à un partenaire, Retour vers le client
     *         pour impossibilité de livraison, En attente au centre pour
     *         récupération, détruit, perdu ou statut inconnu.
     */
    public String getStatut() {
        return statut;
    }

    /**
     * 
     * @param statut
     *            = le statut de la commande : En attente de prise en charge,
     *            Pick-up prévu, Collecté, En attente de dépot par le client au
     *            centre, En cours de transit, En attente de livraison, En
     *            attente de 2e livraison, Livré, Livré à un partenaire, Retour
     *            vers le client pour impossibilité de livraison, En attente au
     *            centre pour récupération, détruit, perdu ou statut inconnu.
     */
    public void setStatut( String statut ) {
        this.statut = statut;
    }

    /**
     * 
     * @return le client
     */
    public String getClient() {
        return client;
    }

    /**
     * 
     * @param client
     *            le client
     */
    public void setClient( String client ) {
        this.client = client;
    }

    /**
     * 
     * @return le statut du compte de l'utilisateur au moment du passage de la
     *         commande.
     */
    public int getStatutCompte() {
        return statutCompte;
    }

    /**
     * 
     * @param statut
     *            = le statut du compte de l'utilisateur au moment du passage de
     *            la commande.
     */
    public void setStatutCompte( int statutCompte ) {
        this.statutCompte = statutCompte;
    }

    /**
     * @return le pays lorsqu'une commande vient de l'étranger
     */
    public String getPays() {
        return pays;
    }

    /**
     * @param Modifie
     *            le pays lorsqu'une commande vient de l'étranger
     */
    public void setPays( String pays ) {
        this.pays = pays;
    }

    /**
     * 
     * @param number
     *            le nombre à tronquer.
     * @param precision
     *            le nombre de chiffre après la vigule restant souhaité.
     * @return le nombre tronqué.
     */
    double truncate( double number, int precision )
    {
        double prec = Math.pow( 10, precision );
        int integerPart = (int) number;
        double fractionalPart = number - integerPart;
        fractionalPart *= prec;
        int fractPart = (int) fractionalPart;
        fractionalPart = (double) ( integerPart ) + (double) ( fractPart ) / prec;
        return fractionalPart;
    }

}
