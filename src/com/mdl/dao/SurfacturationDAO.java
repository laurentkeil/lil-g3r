package com.mdl.dao;

import java.util.List;

import com.mdl.beans.Surfacturation;

/**
 * Interface de la classe surfacturationDaoImpl qui prépare et envoie des
 * requêtes sql afin d'accèder et de récupérer ou créer des données sur les
 * surfacturation des commandes d'un client.
 * 
 */
public interface SurfacturationDAO {

    /**
     * 
     * @param mail
     * @return la surfacturation causant le blocage du compte
     * @throws DAOException
     */
    Surfacturation trouver( String mail ) throws DAOException;

    /**
     * 
     * @param id_client
     * @return Liste de toutes les surfacturations d'un client
     * @throws DAOException
     */
    List<Surfacturation> lister( String id_client ) throws DAOException;

    /**
     * 
     * @param email
     * @return la liste des surfacturations payées du client ayant l'email email
     * @throws DAOException
     */
    List<Surfacturation> listerPayees( String email ) throws DAOException;

    /**
     * 
     * @param email
     * @return la liste des surfacturations impayées du client ayant l'email
     *         email
     * @throws DAOException
     */
    List<Surfacturation> listerImpayees( String email ) throws DAOException;

    /**
     * Permet de retrouver la surfacturation causant le blocage du compte
     */
    Surfacturation trouverSurfactCommande( String icu ) throws DAOException;

    /**
     * Met à jour la surfacturation en confirmant son paiement en bd.
     * 
     * @param icu
     *            l'icu de la commande, identifiant unique généré
     * @throws DAOException
     */
    void payer( String icu, String prix ) throws DAOException;

    /**
     * 
     * @return la liste des surfacturations des clients
     * @throws DAOException
     */
    List<Surfacturation> listerAll() throws DAOException;

    /**
     * 
     * @return la liste des surfacturations payées des clients
     * @throws DAOException
     */
    List<Surfacturation> listerAllPayees() throws DAOException;

    /**
     * 
     * @return la liste des surfacturations impayées des clients
     * @throws DAOException
     */
    List<Surfacturation> listerAllImpayees() throws DAOException;

    /**
     * 
     * @return la liste des surfacturations des clients en fonction d'un
     *         recherche
     * @throws DAOException
     */
    List<Surfacturation> trouverSurfactRech( String rech, String research, String surfact ) throws DAOException;
}
