package com.mdl.dao;

import java.util.List;

import com.mdl.beans.Statut_client;

public interface StatutDAO {

    Statut_client trouver( int idClient ) throws DAOException;

    List<Statut_client> trouverAllBloques() throws DAOException;

    void nouveauClient( int idClient ) throws DAOException;

    void bloquerCompte( int idClient, String cause ) throws DAOException;

    /**
     * met un statut au compte de l'utilisateur ayant l'id idClient : normal,
     * avancé ou premium.
     * 
     * @param idClient
     * @param statutCompte
     *            1,2,3 suivant le compte souhaité de l'utilisateur.
     * @throws DAOException
     */
    void setStatut( int idClient, int statutCompte ) throws DAOException;

    /**
     * Mets le dernier colis qui a été rendu gratuit grâce au compte avancé du
     * client.
     * 
     * @param idColis
     * @param idClient
     * @throws DAOException
     */
    void setIdFree( int idColis, int idClient ) throws DAOException;
}
