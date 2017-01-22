package com.mdl.dao;

import java.io.IOException;
import java.util.List;

import com.mdl.beans.Litige;

/**
 * Interface de la classe DAO du Litige qui prépare et envoie des requête sql
 * afin d'accèder et de récupérer ou créer des données concernant les litiges d'un client 
 * concernant un colis.
 * @author CrespeigneRomain
 * 
 *
 */
public interface LitigeDAO {
	/**
	 * Prépare et envoie la requête SQL permettant de créer un litige à partir des informations 
	 * fournies par le client qui sont placées dans un objet Litige.
	 * @param litige
	 * @throws DAOException
	 * 
	 */
	void creer(Litige litige) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de créer un litige à partir d'un problème signalé
	 * par un coursier. 
	 * @param litige
	 * @param idEmpl
	 * @throws DAOException
	 * 
	 */
	void creerLitigeCoursier(Litige litige, String idEmpl) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de retrouver un colis à partir de l'identifiant de sa commande
	 * @param id_commande
	 * @return l'identifiant du colis
	 * @throws DAOException
	 * 
	 */
	String trouverColis(String id_commande) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de savoir si un litige a déjà été créé
	 * @param objet
	 * @param id_colis
	 * @return un booléen sur l'existence du litige
	 * @throws DAOException
	 * 
	 */
	Boolean existeLitige(String objet, String id_colis) throws DAOException;

	/**
	 * Prépare et envoie la requête SQL permettant de lister les litiges à partir de l'adresse
	 * e-mail du client.
	 * @param email
	 * @return une liste de litiges du client
	 * @throws DAOException
	 * 
	 */
	List<Litige> lister(String email) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de trouver un litige à partir de son identifiant 
	 * afin d'avoir les détails de celui-ci.
	 * @param numLitige
	 * @return un objet Litige.
	 * @throws DAOException
	 * 
	 */
	Litige trouver(String numLitige) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant d'insérer une réponse à un certain litige.
	 * @param note
	 * @param statut
	 * @param type
	 * @param id
	 * @param idEmpl
	 * @throws DAOException
	 * 
	 */
	void insererReponse(String note, String statut, String type, String id, String idEmpl) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de fermer le litige d'un client.
	 * @param note
	 * @param statut
	 * @param type
	 * @param id
	 * @param idEmpl
	 * @throws DAOException
	 * 
	 */
	void fermerLitige(String note, String statut, String type, String id, String idEmpl) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de lister tous les litiges de tous les clients
	 * de la base de données.
	 * @return une liste d'objets Litige
	 * @throws DAOException
	 * 
	 */
	List<Litige> listerAll() throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de trouver les litiges pris en charge par un 
	 * certain employé.
	 * @param numLitige
	 * @return un objet Litige
	 * @throws DAOException
	 * 
	 */
	public Litige trouverLitigeEmpl(String numLitige) throws DAOException;

	/**
	 * Prépare et envoie la requête SQL permettant de lister les litiges en fonction d'une
	 * catégorie choisie (les nouveaux litiges, les litiges en attente d'une réponse d'un client, 
	 * les litiges en attente d'une réponse de l'employé,...)
	 * @param statut
	 * @return une liste d'objets Litige
	 * @throws DAOException
	 * 
	 */
	List<Litige> trouverLitigesCat(String statut) throws DAOException;

	/**
	 * Prépare et envoie la requête SQL permettant de lister les litiges pris en charge par 
	 * l'employé courant (grâce à son e-mail utilisé à la connexion).
	 * @param statut
	 * @param mail
	 * @return une liste d'objets Litige.
	 * @throws DAOException
	 * 
	 */
	List<Litige> trouverLitigesEmpl(String statut, String mail) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de lister les litiges en fonction de certains
	 * critères.
	 * @param statut
	 * @param mail
	 * @param rech
	 * @param search
	 * @return une liste d'objets Litige
	 * @throws DAOException
	 * 
	 */
	List<Litige> trouverLitigesRech(String statut, String mail, String rech, String search) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de lister les problèmes signalés par un coursier.
	 * @param verrou
	 * @return une liste d'objet Litige
	 * @throws DAOException
	 * 
	 */
	List<Litige> trouverLitigesCoursier(String verrou) throws DAOException;
	
	/**
	 * Prépare et envoie la requête permettant de débloquer le traitement d'un problème signalé
	 * par un coursier dans le cas où l'employé n'a pas traité ce problème endéans l'heure.
	 * @throws DAOException
	 * 
	 * @throws IOException 
	 */
	void unlockLitige() throws DAOException, IOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de référencer un problème signalé par un coursier
	 * dans le cas où son problème a été traité et a conduit à un litige.
	 * @param idLitige
	 * @param numLitigeCoursier
	 * @throws DAOException
	 * 
	 */
	void refLitige( String idLitige, String numLitigeCoursier ) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de bloquer un problème signalé par un coursier
	 * car il a été traité.
	 * @param id
	 * @throws DAOException
	 * 
	 */
	void lockLitigeCoursier(String id) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de savoir si un problème signalé par un coursier
	 * est en cours de traitement ou non.
	 * @param id
	 * @return un booleén sur le traitement du problème
	 * @throws DAOException
	 * 
	 */
	Boolean traitementProbleme(String id ) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de trouver les certains informations sur le client
	 * afin de les afficher dans les détails du litige.
	 * @param id_colis
	 * @return un objet Litige
	 * @throws DAOException
	 * 
	 */
	Litige trouverClient(String id_colis) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de trouver un litige qui a été créé ou modifié à partir
	 * d'un problème signalé par un coursier
	 * @param idNotif
	 * @return un objet Litige
	 * @throws DAOException
	 * 
	 */
	public Litige trouverProbleme(String idNotif) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de bloquer le problème du coursier le temps de la prise
	 * en charge par un employé
	 * @param id
	 * @throws DAOException
	 * 
	 */
	void priseEnCharge(String id) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de fermer un problème signalé par un coursier lorsque
	 * celui-ci n'est pas pris en charge pour quelque raison que ce soit.
	 * @param numLitigeCoursier
	 * @throws DAOException
	 * 
	 */
	void fermerProbleme(String numLitigeCoursier) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de trouver un litige à partir de son identifiant dans la base 
	 * de données et de son type.
	 * @param id_colis
	 * @param type
	 * @return un objet Litige
	 * @throws DAOException
	 * 
	 */
	Litige trouverLitige(String id_colis, String type) throws DAOException;
	
	/**
	 * Prépare et envoie la requête SQL permettant de préciser la responsabilité du client lors de la 
	 * fermeture de celle-ci.
	 * @param id
	 * @param responsabilite
	 * @throws DAOException
	 * 
	 */
	void responsabiliteLitige(String id, String responsabilite) throws DAOException;
	
	/**
	 * Prépare et envoir la requête SQL permettant de savoir si le client a au moins 3 litiges en tort
	 * @param mail
	 * @return un booléen
	 * @throws DAOException
	 * 
	 * @throws IOException 
	 */
	boolean litigeResponsable(String mail) throws DAOException, IOException;
}
