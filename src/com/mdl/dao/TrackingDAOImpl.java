package com.mdl.dao;

import static com.mdl.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mdl.beans.Tracking;

import java.sql.PreparedStatement;

/**
 * @see TrackingDAO
 * 
 */
public class TrackingDAOImpl implements TrackingDAO {
	private static DAOFactory	daoFactory;
	private static DAOFactory	daoFact;
	// requête sur les mouvements du colis, insérer id colis
	// CEDRIC si type = 7, ça veut dire
	// "réception du colis provenant de l'étranger" et récupérer ref_centre_id
	private static final String	SQL_SELECT_OPE_COLIS		= "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, type, ref_coursier_id, raison_refus, ref_centre_id FROM OPERATION_COLIS WHERE ref_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?)   order by date_operation asc";

	// CEDRIC utiliser ça pour obtenir le nom du pays en fonction de son ID
	private static final String	SQL_SELECT_NOM_CENTRE		= "SELECT adr_localite FROM CENTRE_DISPATCHING WHERE identifiant = ?";
	// requête sur les mouvements du chariot, insérer id colis
	private static final String	SQL_SELECT_OPE_CHARIOT		= "SELECT COUNT(*) AS opeChariot FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?)  AND type = 6";
	// si c'est 1, insérer id colis
	private static final String	SQL_SELECT_OPE_CHARIOT_1	= "SELECT COUNT(*) AS opeChariot1 FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?)  AND type = 7 ";
	// si c'est 0, insérer id colis
	private static final String	SQL_SELECT_OPE_CHARIOT_1_0	= "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, (type + 10) as type, ref_operation_coursier_id, ref_operation_centre_id "
																	+ "	FROM OPERATION_CHARIOT WHERE ref_operation_chariot_id = (SELECT ref_operation_chariot_id FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = "
																	+ " (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) AND date_operation >= (SELECT date_operation FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = "
																	+ " (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) AND type != 3 AND type != 4 AND type != 6 AND type != 7  AND type != 8 order by date_operation asc";
	// si c'est 1, insérer id colis
	private static final String	SQL_SELECT_OPE_CHARIOT_1_1	= "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, (type + 10) as type, ref_operation_coursier_id, ref_operation_centre_id"
																	+ " FROM OPERATION_CHARIOT WHERE ref_operation_chariot_id IN (SELECT ref_operation_chariot_id FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) "
																	+ " AND date_operation BETWEEN (SELECT max(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) AND"
																	+ " (SELECT max(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 7) AND type != 3 AND type != 4 AND type != 6 AND type != 7  AND type != 8"
								                                    + " UNION "
								                                    + "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, (type + 10) as type, ref_operation_coursier_id, ref_operation_centre_id"
																	+ " FROM OPERATION_CHARIOT WHERE ref_operation_chariot_id IN (SELECT ref_operation_chariot_id FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) "
																	+ " AND date_operation BETWEEN (SELECT min(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) AND"
																	+ " (SELECT min(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 7) AND type != 3 AND type != 4 AND type != 6 AND type != 7  AND type != 8 order by date_operation asc";
	// si c'est 2
	private static final String	SQL_SELECT_OPE_CHARIOT_2	= "SELECT COUNT(*) AS opeChariot2 FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?)  AND type = 7";
	// si c'est 1, insérer id colis
	private static final String	SQL_SELECT_OPE_CHARIOT_2_1	= "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, (type + 10) as type, ref_operation_coursier_id, ref_operation_centre_id "
																	+ "FROM OPERATION_CHARIOT WHERE ref_operation_chariot_id IN (SELECT ref_operation_chariot_id FROM OPERATION_CHARIOT "
																	+ "WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) AND date_operation >= (SELECT max(date_operation) FROM OPERATION_CHARIOT "
																	+ "WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) AND type != 3 AND type != 4 AND type != 6 AND type != 7  AND type != 8"
								                                    + " UNION "
								                                    + "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, (type + 10) as type, ref_operation_coursier_id, ref_operation_centre_id"
																	+ " FROM OPERATION_CHARIOT WHERE ref_operation_chariot_id IN (SELECT ref_operation_chariot_id FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) "
																	+ " AND date_operation BETWEEN (SELECT min(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6) AND"
																	+ " (SELECT min(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 7) AND type != 3 AND type != 4 AND type != 6 AND type != 7  AND type != 8 order by date_operation asc";
	// si c'est 2, insérer id colis
	private static final String	SQL_SELECT_OPE_CHARIOT_2_2	= "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, (type + 10) as type, ref_operation_coursier_id, ref_operation_centre_id "
																	+ "FROM OPERATION_CHARIOT WHERE ref_operation_chariot_id IN (SELECT ref_operation_chariot_id FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6)"
																	+ " AND date_operation BETWEEN (SELECT max(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6)"
																	+ " AND (SELECT max(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?)  AND type = 7)"
																	+ " AND type != 3 AND type != 4 AND type != 6 AND type != 7 AND type != 8 "
								                                    + " UNION "
								                                    + "SELECT to_char(date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, (type + 10) as type, ref_operation_coursier_id, ref_operation_centre_id "
																	+ "FROM OPERATION_CHARIOT WHERE ref_operation_chariot_id IN (SELECT ref_operation_chariot_id FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6)"
																	+ " AND date_operation BETWEEN (SELECT min(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?) AND type = 6)"
																	+ " AND (SELECT min(date_operation) FROM OPERATION_CHARIOT WHERE ref_operation_colis_id = (SELECT COLIS.identifiant FROM COLIS WHERE COLIS.ICU = ?)  AND type = 7)"
																	+ " AND type != 3 AND type != 4 AND type != 6 AND type != 7 AND type != 8 order by date_operation asc";

	private static final String	SQL_SELECT_CENTRE			= "SELECT * FROM centre_dispatching WHERE identifiant = ?";

	private static final String	SQL_EXIST_COLIS				= "SELECT * FROM colis WHERE ICU = ?";

	private static final String	SQL_SAME_MAIL				= "SELECT  CLIENT.mail FROM CLIENT , COLIS , COMMANDE  "
																	+ "WHERE COLIS.ICU = ? AND COLIS.ref_commande_id = COMMANDE.identifiant AND COMMANDE.ref_client_id = CLIENT.identifiant ";

	//trouver l'id du centre de dispatching du coursier dans lequel il travaille normalement, insérer id du coursier
	private static final String SQL_CENTRE_DISPATCHING		= "SELECT COURSIER.ref_coursier_centre_id FROM COURSIER  WHERE COURSIER.identifiant = ?";
	// voir si le coursier travaille dans un autre centre aujourd'hui (retourne 0 lignes s'il reste dans son centre normal), 
	//insérer date_operation et id coursier
	private static final String SQL_CENTRE_DISPATCHING_CSR	= "SELECT TRAVAIL_TEMPORAIRE.ref_travail_centre_id FROM TRAVAIL_TEMPORAIRE "
															+ "WHERE  TRAVAIL_TEMPORAIRE.ref_travail_coursier_id = ?";
	/**
	 * Constructeur de TrackingDAOImpl
	 * 
	 * @param daoFactory
	 */
	TrackingDAOImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;

	}

	/**
	 * @see TrackingDAO
	 * 
	 */
	public boolean existeColis(String idColis) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean existe = false;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
					connexion, SQL_EXIST_COLIS, true, idColis);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				existe = true;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return existe;
	}

	/**
	 * @see TrackingDAO
	 * 
	 */
	public List<Tracking> trackingColis(String idColis) throws DAOException {
		List<Tracking> trackingList = new ArrayList<Tracking>();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
					connexion, SQL_SELECT_OPE_COLIS, true, idColis);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				trackingList.add(mapColis(resultSet));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return trackingList;
	}

	/**
	 * @see TrackingDAO
	 * 
	 */
	public static int opeChariot(String idColis) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int opeChariot;

		try {
			connexion = daoFactory.getConnection();

			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
					connexion, SQL_SELECT_OPE_CHARIOT, true, idColis);
			resultSet = preparedStatement.executeQuery();
			
			resultSet.next();
			opeChariot = resultSet.getInt("opeChariot");
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return opeChariot;
	}

	/**
	 * @see TrackingDAO
	 * 
	 */
	public static int opeChariot_1(String idColis) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int opeChariot;

		try {
			connexion = daoFactory.getConnection();

			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
					connexion, SQL_SELECT_OPE_CHARIOT_1, true, idColis);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();

			opeChariot = resultSet.getInt("opeChariot1");
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return opeChariot;
	}

	/**
	 * @see TrackingDAO
	 * 
	 */
	public static int opeChariot_2(String idColis) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int opeChariot;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
					connexion, SQL_SELECT_OPE_CHARIOT_2, true, idColis);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			opeChariot = resultSet.getInt("opeChariot2");
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return opeChariot;
	}

	/**
	 * @see TrackingDAO
	 * 
	 */
	public List<Tracking> trackingChariot(String idColis) throws DAOException {
		List<Tracking> trackingList = new ArrayList<Tracking>();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connexion = daoFactory.getConnection();
			int opeChariot = opeChariot(idColis);
			if (opeChariot == 1) {
				int opeChariot_1 = opeChariot_1(idColis);
				if (opeChariot_1 == 0) {
					preparedStatement = (PreparedStatement) initialisationRequetePreparee(
							connexion, SQL_SELECT_OPE_CHARIOT_1_0, true,
							idColis, idColis);
				} else {
					preparedStatement = (PreparedStatement) initialisationRequetePreparee(
							connexion, SQL_SELECT_OPE_CHARIOT_1_1, true,
							idColis, idColis, idColis,idColis, idColis, idColis);
				}
			} else {
				int opeChariot_2 = opeChariot_2(idColis);
				if (opeChariot_2 == 1) {
					preparedStatement = (PreparedStatement) initialisationRequetePreparee(
							connexion, SQL_SELECT_OPE_CHARIOT_2_1, true,
							idColis, idColis,idColis, idColis, idColis);

				} else {
					preparedStatement = (PreparedStatement) initialisationRequetePreparee(
							connexion, SQL_SELECT_OPE_CHARIOT_2_2, true,
							idColis, idColis, idColis, idColis, idColis, idColis);

				}
			}
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				trackingList.add(mapChariot(resultSet));

			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return trackingList;
	}

	/**
	 * Permet de retrouver le nom d'un centre
	 * @param id
	 * @return le nom du centre
	 * @throws DAOException
	 * 
	 */
	public static String trouve_centre(String id) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String centre = null;
		if (id != null){
		try {
			connexion = daoFactory.getConnection();
			// Vérifie d'abord si l'employé n'est pas temporairement dans un centre de dispatching 
			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
							connexion, SQL_CENTRE_DISPATCHING_CSR, false, id);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()){
				resultSet.next();
				centre = resultSet.getString("ref_travail_centre_id");
			}else{
				// Si non, on va voir dans quel centre il travaille normalement
				preparedStatement = (PreparedStatement) initialisationRequetePreparee(
						connexion, SQL_CENTRE_DISPATCHING, false, id);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				centre = resultSet.getString("ref_coursier_centre_id");
				
			}
			
			// Ensuite, on cherche le centre grâce à son ID
			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
					connexion, SQL_SELECT_CENTRE, true, centre);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			centre = resultSet.getString("adr_localite");
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		}
		return centre;
	}
	
	/**
	 * Permet de retrouver le nom d'un partenaire
	 * @param id
	 * @return le nom du pays
	 * @throws DAOException
	 * 
	 */
	 public static String trouve_partenaire( String id ) throws DAOException {
	        Connection connexion = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        String centre = null;
	        if (id != null){
		        try {
		            connexion = daoFactory.getConnection();
		            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_NOM_CENTRE,
		                    false, id );
		            resultSet = preparedStatement.executeQuery();

		            resultSet.next();
		            centre = resultSet.getString( "adr_localite" );
		            
		            	
		        } catch ( SQLException e ) {
		            throw new DAOException( e );
		        } finally {
		            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		        }
	        }
	        return centre;
	    }

	public boolean emailIdentique(String mail, String ICU) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean same = false;
		try {
			connexion = daoFactory.getConnection();

			preparedStatement = (PreparedStatement) initialisationRequetePreparee(
					connexion, SQL_SAME_MAIL, false, ICU);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getString("mail").equals(mail)) {
					same = true;
				}
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return same;
	}

	/**
	 * Permet de récupérer les informations issues de la requête SQL en les
	 * ajoutant dans le bean Tracking.
	 * 
	 * @param resultSet
	 * @return un objet Tracking
	 * @throws SQLException
	 */
	private static Tracking mapColis(ResultSet resultSet) throws SQLException {
		Tracking tracking = new Tracking();
		tracking.setType_ope(resultSet.getString("type"));
		tracking.setCoursier(resultSet.getString("ref_coursier_id"));
		tracking.setRefus(resultSet.getString("raison_refus"));
		tracking.setDate(resultSet.getString("date_operation"));
		tracking.setCentre(null);
		String partenaire = trouve_partenaire(resultSet
				.getString("ref_centre_id"));
		tracking.setPartenaire(partenaire);
		return tracking;
	}

	/**
	 * Permet de récupérer les informations issues de la requête SQL en les
	 * ajoutant dans le bean Tracking.
	 * 
	 * @param resultSet
	 * @return un objet Tracking
	 * @throws SQLException
	 */
	private static Tracking mapChariot(ResultSet resultSet) throws SQLException {
		Tracking tracking = new Tracking();
		tracking.setType_ope(resultSet.getString("type"));
		
		tracking.setCoursier(resultSet.getString("ref_operation_coursier_id"));
		String partenaire = trouve_partenaire(resultSet
				.getString("ref_operation_centre_id"));
		tracking.setPartenaire(partenaire);
		String centre = trouve_centre(resultSet
				.getString("ref_operation_coursier_id"));
		tracking.setCentre(centre);
		tracking.setDate( resultSet.getString("date_operation"));
		tracking.setRefus(null);
		return tracking;
	}
}
