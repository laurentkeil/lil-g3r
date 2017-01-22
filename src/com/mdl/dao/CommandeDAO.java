package com.mdl.dao;

import java.util.List;

import com.mdl.beans.Commande;

/**
 * Interface de la classe DAO de commande qui prépare et envoie des requêtes sql
 * afin d'accèder et de récupérer ou créer des données sur les commandes d'un
 * client.
 */
public interface CommandeDAO {
    /**
     * prépare et envoie une requête sql pour récupérer la liste des commandes
     * d'un utilisateur à partir de son e-mail et les détails de cette commande
     * et son colis.
     */
    List<Commande> lister( String email ) throws DAOException;

    /**
     * prépare et envoie une requête sql pour récupérer la liste des commandes
     * de tous les utilisateurs et les détails de leurs commandes et leurs
     * colis.
     */
    List<Commande> listerAll() throws DAOException;

    /**
     * prépare et envoie une requête sql pour récupérer les détails de cette
     * commande d'un utilisateur à partir de l'id de cette commande.
     * 
     * @param id
     *            l'id de la commande.
     * @return commande d'un utilisateur correspondant à l'id de celle-ci
     * @throws DAOException
     */
    Commande trouver( String id ) throws DAOException;

    /**
     * 
     * @param email
     *            : l email du client
     * @return le carnet d'adresses du client
     * @throws DAOException
     */
    List<List<String>> listerCarnet( String email ) throws DAOException;

    /**
     * 
     * @param email
     * @param nom
     * @return les adresses du carnet correspondantes au nom encodé par le
     *         client ayant l'email email.
     * @throws DAOException
     */
    List<List<String>> selectCarnet( String email, String nom ) throws DAOException;

    /**
     * supprime une adresse du carnet d'adresses.
     * 
     * @param id
     *            l'id de l'adresse du carnet à supprimer.
     * @throws DAOException
     */
    void deleteCarnet( int id ) throws DAOException;

    /**
     * vérifie si l'icu existe déjà parmi le client ayant l'email email.
     * 
     * @param icu
     *            l'identifiant unique de la commande générée
     * @param email
     *            l'email du client
     * @return true si l'icu existe bien, false sinon
     * @throws DAOException
     */
    boolean existICU( String icu, String email ) throws DAOException;

    /**
     * vérifie si l'icu existe déjà parmi TOUS les clients.
     * 
     * @param icu
     * @return true si l'icu existe déjà, false sinon
     * @throws DAOException
     */
    boolean verifICU( String icu ) throws DAOException;

    /**
     * Prépare et envoie une requête sql afin de créer en base de donnée une
     * commande, son colis et les adresses dans le carnet d'adresses si
     * l'utilisateur le veut
     * 
     * @param commande
     * @param id_client
     * @throws IllegalArgumentException
     * @return l'id de la commande générée
     * @throws DAOException
     */
    int creer( Commande commande, Integer id_client ) throws IllegalArgumentException, DAOException;

    /**
     * creer une adresse dans le carnet en bd
     * 
     * @param carnet
     *            une liste d'information concernant une adresse à ajouter dans
     *            le carnet d'adresses.
     * @param id_client
     *            l'id du client désirant ajouter une adresse dans son carnet.
     * @throws DAOException
     */
    public void addCarnet( List<String> carnet, int id_client ) throws DAOException;

    /**
     * Prépare et envoie une requête sql afin de retrouver en base de donnée une
     * commande, son colis et les adresses dans le carnet d'adresses en fonction
     * du mail ou de l'ICU.
     * 
     * @param rech
     * @param research
     * @throws DAOException
     */
    List<Commande> trouverCommandeRech( String rech, String research, String mail ) throws DAOException;

    /**
     * 
     * @param email
     * @return le chiffre d'affaire du client, la somme total de l'argent payé à
     *         l'entreprise.
     * @throws DAOException
     */
    int chiffreAffaire( String email ) throws DAOException;

    /**
     * 
     * @param email
     * @return le nombre de factures impayées dans les 15 jours imposés d'un
     *         utilisateur ayant pour mail email depuis son inscription sur le
     *         site.
     * @throws DAOException
     */
    int nbSurfactImpayees( String email ) throws DAOException;

    /**
     * 
     * @param email
     * @return le nombre de litiges en tort d'un client depuis son inscription
     * @throws DAOException
     */
    int nbLitigesTort( String email ) throws DAOException;

    /**
     * @param email
     * @return l'id du colis gratuit s'il l'utilisateur ayant l'adresse email en
     *         a déjà eu un gratuit.
     * @throws DAOException
     */
    int getIdLastColis( String email ) throws DAOException;

    /**
     * @param email
     * @return le nombre de commandes passées depuis l'inscription de
     *         l'utilisateur.
     * @throws DAOException
     */
    int nbComForFree( String email ) throws DAOException;

    /**
     * @param email
     * @return le nombre de commandes passées depuis le dernier colis col_id
     *         rendu gratuit.
     * @throws DAOException
     */
    int nbComForFree( String email, int col_id ) throws DAOException;

    /**
     * 
     * @param id_commande
     * @return l'id du colis correspond à id_commande.
     * @throws DAOException
     */
    int getIdColis( int id_commande ) throws DAOException;

    /**
     * prépare et envoie une requête sql pour récupérer la liste des commandes
     * de tous les utilisateurs et les détails de leurs commandes et leurs colis
     * circulant sur le territoire belge.
     */
    List<Commande> listerBel() throws DAOException;

    /**
     * prépare et envoie une requête sql pour récupérer la liste des commandes
     * de tous les utilisateurs et les détails de leurs commandes et leurs colis
     * entrant sur le territoire belge.
     */
    List<Commande> listerEnt() throws DAOException;

    /**
     * prépare et envoie une requête sql pour récupérer la liste des commandes
     * de tous les utilisateurs et les détails de leurs commandes et leurs colis
     * sortant du territoire belge.
     */
    List<Commande> listerSor() throws DAOException;
        
    void notifier(String icu) throws DAOException;

}
