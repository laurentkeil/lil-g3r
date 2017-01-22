package com.mdl.dao;

import java.util.List;

import com.mdl.beans.Tracking;

/**
 * Interface de la classe DAO du Tracking qui prépare et envoie des requête sql
 * afin d'accèder et de récupérer des données sur le cheminement d'un colis d'un
 * client.
 * @author CrespeigneRomain
 * 
 *
 */
public interface TrackingDAO {
	
	/**
	 * Prépare et envoie la requête SQL permettant de lister les informations liées
	 * au colis durant son transport, de la prise du pickup à la livraison à la destination
	 * @param id_colis
	 * @return une liste d'informations liées au colis
	 * @throws DAOException
	 * 
	 */
    List<Tracking> trackingColis( String id_colis ) throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de lister les informations liées au chariot
     * contenant le colis concerné durant son transport entre différents centres de dispatching.
     * @param idColis
     * @return une liste d'informations liées au chariot
     * @throws DAOException
     * 
     */
    List<Tracking> trackingChariot( String idColis ) throws DAOException;

    /**
     * Prépare et envoie la requête SQL permettant de savoir si le colis recherché existe ou non
     * @param icu
     * @return un booléen sur l'existant du colis ou non
     * @throws DAOException
     * 
     */
    boolean existeColis( String icu ) throws DAOException;
    
    /**
     * Prépare et envoie la requête SQL permettant de savoir si le e-mail renseigné est le même
     * que l'e-mail du responsable du colis.
     * @param mail
     * @param ICU
     * @return un booléen
     * @throws DAOException
     */
    boolean emailIdentique(String mail, String ICU) throws DAOException;
}
