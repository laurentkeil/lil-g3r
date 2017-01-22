package com.mdl.dao;

import java.util.List;

import com.mdl.beans.Utilisateur;

/**
 * Interface de la classe DAO de l'utilisateur qui prépare et envoie des requête
 * sql afin d'accèder et de récupérer ou créer des données sur les utilisateurs.
 */
public interface UtilisateurDAO {

    /**
     * Prépare et envoie la requête SQL permettant la création d'un compte
     * utilisateur.
     * 
     * @param utilisateur
     * @throws DAOException
     * 
     */
    void creer( Utilisateur utilisateur ) throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de retrouver les informations
     * d'un utilisateur à partir de son adresse mail.
     * 
     * @param email
     * @return
     * @throws DAOException
     * 
     */
    Utilisateur trouver( String email ) throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de retrouver les informations
     * d'un utilisateur à partir de son identifiant de la base de données.
     * 
     * @param id
     * @return les informations de l'utilisateur
     * @throws DAOException
     * 
     */
    Utilisateur trouverAvecId( String id ) throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de valider l'inscription d'un
     * utilisateur en modifiant la colonne "validation" de la base de données.
     * 
     * @param email
     * @return les informations de l'utilisateur
     * @throws DAOException
     * 
     */
    boolean inscription( String email ) throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de modifier les informations
     * liées à un utilisateur grâce au bean Utilisateur.
     * 
     * @param utilisateur
     * @throws DAOException
     * 
     */
    void modifier( Utilisateur utilisateur ) throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de lister toutes les
     * informations de tous les utilisateurs se trouvant dans la base de
     * données.
     * 
     * @return une liste des informations des utilisateurs.
     * @throws DAOException
     * 
     */
    List<Utilisateur> listerAll() throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de lister toutes les
     * informations de tous les utilisateurs inscrits se trouvant dans la base
     * de données.
     * 
     * @return une liste des informations des utilisateurs.
     * @throws DAOException
     * 
     */
    List<Utilisateur> listerAllInscrit() throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de lister les informations
     * des utilisateurs selon certains critères de recherche.
     * 
     * @param rech
     * @param research
     * @return une liste des informations de certains utilisateurs.
     * @throws DAOException
     */
    List<Utilisateur> trouverClientRech( String rech, String research ) throws DAOException;
}
