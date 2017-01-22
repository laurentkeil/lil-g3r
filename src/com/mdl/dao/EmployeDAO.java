package com.mdl.dao;

import com.mdl.beans.Employe;

/**
 * Interface de la classe DAO de l'employé qui prépare et envoie des requête sql
 * afin d'accèder et de récupérer ou créer des données sur les employés.
 */
public interface EmployeDAO {
	/**
	 * Prépare et envoie la requête SQL permettant de créer un employé dans la 
	 * base de données.
	 * @param employes
	 * @throws DAOException
	 */
	void creer( Employe employes ) throws DAOException;

	/**
	 * Prépare et envoie la requête SQL permettant de trouver un employé dans la 
	 * base de données.
	 * @param email
	 * @return un objet Employe
	 * @throws DAOException
	 */
    Employe trouver( String email ) throws DAOException;

}
