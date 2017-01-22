package com.mdl.dao;

import static com.mdl.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import com.mdl.beans.Litige;
import com.mdl.parser.Convertion;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

/**
 * @see LitigeDAO
 *  
 *
 */
public class LitigeDAOImpl implements LitigeDAO {
	
	private static final String FICHIER_PROPERTIES      = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_LITIGE_JOUR    = "litige_jour";
    private static final String PROPERTY_NB_LITIGE_TORT = "nb_litige_tort";
    private static final String PROPERTY_TPS_VERROU     = "tps_verrou";
	
    private static DAOFactory   daoFactory;
    private static DAOFactory   daoFact;

    private static final String SQL_RECHERCHE_COLIS          = "SELECT * from colis where ref_commande_id = ?";
    private static final String SQL_RECHERCHE_CLIENT         = "SELECT CLIENT.nom, CLIENT.mail, CLIENT.prenom, COLIS.ICU FROM CLIENT, COMMANDE,"
                                                                     + "COLIS WHERE COLIS.identifiant = ? AND COLIS.ref_commande_id = COMMANDE.identifiant and COMMANDE.ref_client_id = CLIENT.identifiant";
    private static final String SQL_INSERT_LITIGE            = " INSERT INTO litige (type, ref_colis_id,date_creation) VALUES (?, ?, to_date(to_char(sysdate,'DD-MM-YYYY HH24:MI:SS'),'DD-MM-YYYY HH24:MI:SS' ))";
    private static final String SQL_INSERT_LITIGE_CARAC      = "INSERT INTO OPERATION_LITIGE (type, note, ref_litige_id, date_operation) VALUES (?, ?, ?, to_date(to_char(sysdate,'DD-MM-YYYY HH24:MI:SS'),'DD-MM-YYYY HH24:MI:SS' ))";

    private static final String SQL_INSERT_LITIGE_CARAC_EMPL = "INSERT INTO OPERATION_LITIGE (type, statut, note, ref_litige_id, ref_employe_id) VALUES (?,?, ?, ?, ?)";
    private static final String SQL_UPDATE_NOTIF             = "UPDATE NOTIFICATION_PROBLEME SET ref_notification_litige_id = ? "
                                                                     + "WHERE identifiant = ?";

    private static final String SQL_REC_OBJET_LITIGE         = "SELECT * FROM LITIGE WHERE type = ? and ref_colis_id = ? ";
    private static final String SQL_LISTER_LITIGE            = "SELECT LITIGE.identifiant, LITIGE.ref_colis_id, to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, LITIGE.type AS objet, OPERATION_LITIGE.statut,"
                                                                     + "OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note, COLIS.ICU FROM "
                                                                     + "LITIGE  , COLIS  , COMMANDE  , CLIENT  , OPERATION_LITIGE "
                                                                     + " WHERE OPERATION_LITIGE.ref_litige_id = LITIGE.identifiant AND date_operation ="
                                                                     + "( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant )"
                                                                     + " AND LITIGE.ref_colis_id = COLIS.identifiant AND COLIS.ref_commande_id = COMMANDE.identifiant AND COMMANDE.ref_client_id = CLIENT.identifiant "
                                                                     + "AND CLIENT.mail = ? order by date_operation desc";
    private static final String SQL_SELECT_LITIGE            = "SELECT LITIGE.identifiant, LITIGE.ref_colis_id, to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, LITIGE.type AS objet, OPERATION_LITIGE.statut, "
                                                                     + "OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note, COLIS.ICU FROM "
                                                                     + "LITIGE  , OPERATION_LITIGE , COLIS  "
                                                                     + " WHERE LITIGE.identifiant = ? AND OPERATION_LITIGE.ref_litige_id = LITIGE.identifiant AND COLIS.identifiant = LITIGE.ref_colis_id order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_LISTER_ALL_LITIGE        = "SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) ORDER BY id_litige asc";

    private static final String SQL_SELECT_LITIGE_EMPL       = "SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM  CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE"
                                                                     + " LITIGE.identifiant = ? AND CLIENT.identifiant = COMMANDE.ref_client_id AND COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id "
                                                                     + " AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_INSERT_REPONSE_CLIENT    = "INSERT INTO OPERATION_LITIGE (type,note,statut, ref_litige_id, ref_employe_id) VALUES (?, ?, ?,?, ?)";
    private static final String SQL_INSERT_EMPL_LITIGE       = "INSERT INTO EMPLOYE_LITIGE (ref_elit_em_id ,ref_employelitige_litige_id) VALUES (?, ?)";

    private static final String SQL_INSERT_FERMETURE         = "INSERT INTO OPERATION_LITIGE (type,note , statut, ref_litige_id, ref_employe_id) VALUES (?,?, ?,?, ?)";
    private static final String SQL_INSERT_EMPL_FERMETURE    = "INSERT INTO EMPLOYE_LITIGE (ref_elit_em_id ,ref_employelitige_litige_id) VALUES (?, ?)";

    private static final String SQL_SELECT_CAT               = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis,COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) AND OPERATION_LITIGE.statut = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_CAT_EMPL          = "SELECT max(LITIGE.type) AS objet, max(OPERATION_LITIGE.statut) AS statut, max(EMPL_SERVICE_CLIENTELE.nom) AS nom, max(EMPL_SERVICE_CLIENTELE.prenom) AS prenom, max(COLIS.identifiant) AS id_colis, max(COLIS.ICU) AS ICU, max(LITIGE.identifiant) AS id_litige,"
														    		+ " max(CLIENT.mail) AS mail, max(CLIENT.nom) AS nom, max(CLIENT.prenom) AS prenom, max(OPERATION_LITIGE.type) AS type, max(to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS')) as date_operation, "
														    		+ "max(to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS')) as date_creation FROM LITIGE  , COLIS  , COMMANDE  , CLIENT  , OPERATION_LITIGE, EMPLOYE_LITIGE , EMPL_SERVICE_CLIENTELE  WHERE OPERATION_LITIGE.ref_litige_id = LITIGE.identifiant AND LITIGE.ref_colis_id = COLIS.identifiant AND "
														    		+ "COLIS.ref_commande_id = COMMANDE.identifiant AND COMMANDE.ref_client_id = CLIENT.identifiant AND EMPLOYE_LITIGE.ref_employelitige_litige_id = LITIGE.identifiant AND EMPLOYE_LITIGE.ref_elit_em_id = EMPL_SERVICE_CLIENTELE.identifiant "
														    		+ "AND  date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant) AND OPERATION_LITIGE.statut = ? AND EMPL_SERVICE_CLIENTELE.mail = ? "
														    		+ " GROUP BY LITIGE.identifiant,OPERATION_LITIGE.date_operation order by OPERATION_LITIGE.date_operation asc";
														    
    private static final String SQL_SELECT_RECH_MAIL         = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) AND OPERATION_LITIGE.statut = ? AND CLIENT.mail = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_RECH_EMPL_MAIL    = "SELECT to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, LITIGE.type AS objet, OPERATION_LITIGE.statut, EMPL_SERVICE_CLIENTELE.nom, EMPL_SERVICE_CLIENTELE.prenom, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige"
                                                                     + ", CLIENT.mail, CLIENT.nom, CLIENT.prenom, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note,to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation FROM LITIGE  , COLIS  , COMMANDE  , CLIENT  ,"
                                                                     + " OPERATION_LITIGE, EMPLOYE_LITIGE , EMPL_SERVICE_CLIENTELE  "
                                                                     + "WHERE OPERATION_LITIGE.ref_litige_id = LITIGE.identifiant AND LITIGE.ref_colis_id = COLIS.identifiant AND COLIS.ref_commande_id = COMMANDE.identifiant AND COMMANDE.ref_client_id = CLIENT.identifiant  "
                                                                     + "AND EMPLOYE_LITIGE.ref_employelitige_litige_id = LITIGE.identifiant AND EMPLOYE_LITIGE.ref_elit_em_id = EMPL_SERVICE_CLIENTELE.identifiant AND date_operation = "
                                                                     + "( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant) AND OPERATION_LITIGE.statut = ? AND EMPL_SERVICE_CLIENTELE.mail = ? AND CLIENT.mail = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_RECH_NOM          = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) AND OPERATION_LITIGE.statut = ? AND CLIENT.nom = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_RECH_EMPL_NOM     = "SELECT to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, LITIGE.type AS objet, OPERATION_LITIGE.statut, EMPL_SERVICE_CLIENTELE.nom, EMPL_SERVICE_CLIENTELE.prenom, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige"
                                                                     + ", CLIENT.mail, CLIENT.nom, CLIENT.prenom, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note,to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation FROM LITIGE  , COLIS  , COMMANDE  , CLIENT  ,"
                                                                     + " OPERATION_LITIGE, EMPLOYE_LITIGE , EMPL_SERVICE_CLIENTELE  "
                                                                     + "WHERE OPERATION_LITIGE.ref_litige_id = LITIGE.identifiant AND LITIGE.ref_colis_id = COLIS.identifiant AND COLIS.ref_commande_id = COMMANDE.identifiant AND COMMANDE.ref_client_id = CLIENT.identifiant  "
                                                                     + "AND EMPLOYE_LITIGE.ref_employelitige_litige_id = LITIGE.identifiant AND EMPLOYE_LITIGE.ref_elit_em_id = EMPL_SERVICE_CLIENTELE.identifiant AND date_operation = "
                                                                     + "( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant) AND OPERATION_LITIGE.statut = ? AND EMPL_SERVICE_CLIENTELE.mail = ? AND CLIENT.nom = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_RECH_LIT          = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE LITIGE.identifiant = ? AND CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) AND OPERATION_LITIGE.statut = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_RECH_EMPL_LIT     = "SELECT to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, LITIGE.type AS objet, OPERATION_LITIGE.statut, EMPL_SERVICE_CLIENTELE.nom, EMPL_SERVICE_CLIENTELE.prenom, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige"
                                                                     + ", CLIENT.mail, CLIENT.nom, CLIENT.prenom, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note,to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation FROM LITIGE  , COLIS  , COMMANDE  , CLIENT  ,"
                                                                     + " OPERATION_LITIGE, EMPLOYE_LITIGE , EMPL_SERVICE_CLIENTELE  "
                                                                     + "WHERE LITIGE.identifiant = ? AND OPERATION_LITIGE.ref_litige_id = LITIGE.identifiant AND LITIGE.ref_colis_id = COLIS.identifiant AND COLIS.ref_commande_id = COMMANDE.identifiant AND COMMANDE.ref_client_id = CLIENT.identifiant  "
                                                                     + "AND EMPLOYE_LITIGE.ref_employelitige_litige_id = LITIGE.identifiant AND EMPLOYE_LITIGE.ref_elit_em_id = EMPL_SERVICE_CLIENTELE.identifiant AND date_operation = "
                                                                     + "( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant) AND OPERATION_LITIGE.statut = ? AND EMPL_SERVICE_CLIENTELE.mail = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_RECH_COL          = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis,COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE COLIS.ICU = ? AND CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) AND OPERATION_LITIGE.statut = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_RECH_EMPL_COL     = "SELECT to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, LITIGE.type AS objet, OPERATION_LITIGE.statut, EMPL_SERVICE_CLIENTELE.nom, EMPL_SERVICE_CLIENTELE.prenom, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige"
                                                                     + ", CLIENT.mail, CLIENT.nom, CLIENT.prenom, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note,to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation FROM LITIGE  , COLIS  , COMMANDE  , CLIENT  ,"
                                                                     + " OPERATION_LITIGE, EMPLOYE_LITIGE, EMPL_SERVICE_CLIENTELE  "
                                                                     + "WHERE  COLIS.identifiant = ? AND OPERATION_LITIGE.ref_litige_id = LITIGE.identifiant AND LITIGE.ref_colis_id = COLIS.identifiant AND COLIS.ref_commande_id = COMMANDE.identifiant AND COMMANDE.ref_client_id = CLIENT.identifiant  "
                                                                     + "AND EMPLOYE_LITIGE.ref_employelitige_litige_id = LITIGE.identifiant AND EMPLOYE_LITIGE.ref_elit_em_id = EMPL_SERVICE_CLIENTELE.identifiant AND date_operation = "
                                                                     + "( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant) AND OPERATION_LITIGE.statut = ? AND EMPL_SERVICE_CLIENTELE.mail = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_ALL_MAIL          = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) AND CLIENT.mail = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_ALL_NOM           = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) AND CLIENT.nom = ? order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_ALL_LIT           = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " to_char(LITIGE.date_creation, 'DD-MM-YYYY HH24:MI:SS') as date_creation , OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE LITIGE.identifiant = ? AND CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_SELECT_ALL_COL           = " SELECT CLIENT.mail, CLIENT.nom, CLIENT.prenom, COMMANDE.identifiant AS id_commande, COLIS.identifiant AS id_colis, COLIS.ICU, LITIGE.identifiant AS id_litige,"
                                                                     + " LITIGE.date_creation, OPERATION_LITIGE.statut, LITIGE.type AS objet, OPERATION_LITIGE.type, to_char(OPERATION_LITIGE.date_operation, 'DD-MM-YYYY HH24:MI:SS') as date_operation, OPERATION_LITIGE.note FROM CLIENT  , COMMANDE  ,"
                                                                     + " COLIS  , LITIGE  , OPERATION_LITIGE WHERE COLIS.ICU = ? AND CLIENT.identifiant = COMMANDE.ref_client_id AND"
                                                                     + " COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id AND LITIGE.identifiant = OPERATION_LITIGE.ref_litige_id "
                                                                     + "AND date_operation = ( SELECT MAX(date_operation) FROM OPERATION_LITIGE WHERE ref_litige_id = LITIGE.identifiant ) order by OPERATION_LITIGE.date_operation asc";

    private static final String SQL_LITIGES_COURSIER         = "SELECT NOTIFICATION_PROBLEME.identifiant, NOTIFICATION_PROBLEME.note, COURSIER.nom, COURSIER.prenom, NOTIFICATION_PROBLEME.ref_notification_colis_id AS id_colis, NOTIFICATION_PROBLEME.date_creation, NOTIFICATION_PROBLEME.type as objet, NOTIFICATION_PROBLEME.verrou, NOTIFICATION_PROBLEME.date_verrou,"
                                                                     + " COLIS.ICU, COLIS.identifiant FROM NOTIFICATION_PROBLEME , COURSIER , COLIS   "
                                                                     + " WHERE NOTIFICATION_PROBLEME.ref_notification_colis_id = COLIS.identifiant AND NOTIFICATION_PROBLEME.ref_not_coursier_id = COURSIER.identifiant and verrou = ? order by NOTIFICATION_PROBLEME.date_creation desc";

    private static final String SQL_UNLOCK                   = "UPDATE NOTIFICATION_PROBLEME SET verrou = 0, date_verrou = NULL  "
    																 + "WHERE verrou = 1 AND (to_char(sysdate,'yyyymmddhh24miss') - to_char(date_verrou,'yyyymmddhh24miss')) > ?";

    private static final String SQL_LOCK                     = "UPDATE NOTIFICATION_PROBLEME SET verrou = 2, date_verrou = NULL "
                                                                     + "WHERE identifiant = ?";

    private static final String SQL_NOTI_PROB_TRAITEMENT     = "SELECT * FROM NOTIFICATION_PROBLEME WHERE identifiant = ? and verrou = 1";

    private static final String SQL_PRISE_EN_CHARGE          = "UPDATE NOTIFICATION_PROBLEME SET verrou = 1, date_verrou = SYSTIMESTAMP "
                                                                     + "WHERE identifiant = ?";

    private static final String SQL_FERMER_PROBL             = "UPDATE NOTIFICATION_PROBLEME SET verrou = 2, date_verrou = null, ref_notification_litige_id = null "
                                                                     + "WHERE identifiant = ?";

    private static final String SQL_RECHERCHE_NOTIF          = "SELECT * FROM NOTIFICATION_PROBLEME WHERE identifiant = ?";

    private static final String SQL_TROUVE_LITIGE            = "SELECT * FROM LITIGE WHERE ref_colis_id = ? and type = ?";

    private static final String SQL_UPDATE_RESPON            = "UPDATE LITIGE SET responsabilite_client = ? WHERE identifiant = ?";

    private static final String SQL_MAUVAIS                  = "SELECT COUNT( * ) AS nombre_litiges_tort FROM CLIENT  , COMMANDE  , COLIS  , LITIGE   "
                                                                     + "WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND COMMANDE.identifiant = COLIS.ref_commande_id AND COLIS.identifiant = LITIGE.ref_colis_id "
                                                                     + "AND LITIGE.responsabilite_client =1 AND CLIENT.mail =  ? ";
    
    private static final String SQL_RECHERCHE_COURSIER  	 = "SELECT * FROM COURSIER WHERE identifiant = ?";
    /**
     * Constructeur de LitigeDAOImpl
     * @param daoFactory
     *  
     */
    LitigeDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public String trouverColis( String id_commande ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String id_colis = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_RECHERCHE_COLIS,
                    false, id_commande );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                id_colis = mapID( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return id_colis;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void creer( Litige litige ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        try {
            connexion = daoFactory.getConnection();
            //preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_LITIGE, true,
            //        litige.getObjet(), litige.getColis() );
            String generatedColumns[] = {"identifiant"};
            preparedStatement = connexion.prepareStatement(SQL_INSERT_LITIGE, generatedColumns);
            preparedStatement.setString(1,litige.getObjet());
            preparedStatement.setString(2,litige.getColis());
            
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
            }

            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                String id = Integer.toString( valeursAutoGenerees.getInt( 1 ) );
                litige.setId( id );
            } else {
                throw new DAOException( "Echec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
            }

            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_LITIGE_CARAC,
                    false,
                    litige.getType(), litige.getDescription(), litige.getId() );

            statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void creerLitigeCoursier( Litige litige, String idEmpl ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        try {
            connexion = daoFactory.getConnection();
            //preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_LITIGE, true,
            //        litige.getObjet(), litige.getColis() );
            
            String generatedColumns[] = {"identifiant"};
            preparedStatement = connexion.prepareStatement(SQL_INSERT_LITIGE, generatedColumns);
            preparedStatement.setString(1,litige.getObjet());
            preparedStatement.setString(2,litige.getColis());


            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
            }

            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                String id = Integer.toString( valeursAutoGenerees.getInt( 1 ) );
                litige.setId( id );
            } else {
                throw new DAOException( "Echec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
            }

            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                    SQL_INSERT_LITIGE_CARAC_EMPL, false,
                    litige.getType(), litige.getStatut(), litige.getDescription(), litige.getId(), idEmpl );

            statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
            }
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_UPDATE_NOTIF,
                    false, litige.getId(), litige.getNumLitigeCoursier() );
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void refLitige( String idLitige, String numLitigeCoursier ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_UPDATE_NOTIF, false,
                    idLitige, numLitigeCoursier );
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public Boolean existeLitige( String objet, String id_colis ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean trouve = false;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_REC_OBJET_LITIGE,
                    false, objet, id_colis );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                trouve = true;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return trouve;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public List<Litige> lister( String email ) throws DAOException {
        List<Litige> litigeList = new ArrayList<Litige>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTER_LITIGE,
                    true, email );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litigeList.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litigeList;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public Litige trouver( String numLitige ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Litige litige = new Litige();
        List<Litige> detail = new ArrayList<Litige>();
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_LITIGE, false,
                    numLitige );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litige = map( resultSet );
                detail = mapDetail( resultSet, detail );
                litige.setDetail( detail );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litige;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void insererReponse( String note, String statut_litige, String type, String id, String idEmpl )
            throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                    SQL_INSERT_REPONSE_CLIENT, false,
                    type, note, statut_litige, id, idEmpl );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
            }
            if ( idEmpl != null ) {
                // Permet de savoir qui a répondu à quel litige
                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                        SQL_INSERT_EMPL_LITIGE, false,
                        idEmpl, id );
                statut = preparedStatement.executeUpdate();
                if ( statut == 0 ) {
                    throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
                }
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void fermerLitige( String note, String statut_litige, String type, String id, String idEmpl )
            throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_FERMETURE,
                    false, type, note, statut_litige, id, idEmpl );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
            }
            if ( idEmpl != null ) {
                // Permet de savoir qui a répondu à quel litige
                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                        SQL_INSERT_EMPL_FERMETURE, false,
                        idEmpl, id );

                statut = preparedStatement.executeUpdate();
                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
                }
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public List<Litige> listerAll() throws DAOException {
        List<Litige> litigeList = new ArrayList<Litige>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTER_ALL_LITIGE,
                    true );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litigeList.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litigeList;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public Litige trouverLitigeEmpl( String numLitige ) throws DAOException {
        /*
         * Lorsqu'un employé souhaite regarder le litige d'un client.
         */
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Litige litige = new Litige();
        List<Litige> detail = new ArrayList<Litige>();
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_LITIGE_EMPL,
                    false, numLitige );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litige = mapAll( resultSet );
                detail = mapDetail( resultSet, detail );
                litige.setDetail( detail );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litige;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public List<Litige> trouverLitigesCat( String statut ) throws DAOException {
        List<Litige> litigeList = new ArrayList<Litige>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_CAT,
                    true, statut );

            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litigeList.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return litigeList;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public List<Litige> trouverLitigesEmpl( String statut, String mail ) throws DAOException {
        List<Litige> litigeList = new ArrayList<Litige>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_CAT_EMPL,
                    true, statut, mail );

            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litigeList.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return litigeList;
    }

    public List<Litige> trouverLitigesRech( String statut, String mail, String rech, String research )
            throws DAOException {
        List<Litige> litigeList = new ArrayList<Litige>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connexion = daoFactory.getConnection();
            if ( statut.equals( "0" ) ) {
            	//permet de savoir la catégorie de litige recherché
                if ( research.equals( "1" ) ) {
                	// permet de savoir l'objet de la recherche
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_ALL_MAIL,
                            true, rech );
                    resultSet = preparedStatement.executeQuery();
                } else if ( research.equals( "2" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_ALL_NOM, false, rech );
                    resultSet = preparedStatement.executeQuery();
                } else if ( research.equals( "3" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_ALL_COL,
                            true, rech );
                    resultSet = preparedStatement.executeQuery();
                } else {

                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_ALL_LIT,
                            true, rech );
                    resultSet = preparedStatement.executeQuery();
                }

            } else if ( ( statut.equals( "1" ) ) || ( statut.equals( "3" ) ) || ( statut.equals( "3" ) )
                    || ( statut.equals( "4" ) ) ) {
                if ( research.equals( "1" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_MAIL,
                            true, statut, rech );
                    resultSet = preparedStatement.executeQuery();
                } else if ( research.equals( "2" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_NOM,
                            true, statut, rech );
                    resultSet = preparedStatement.executeQuery();
                } else if ( research.equals( "3" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_COL,
                            true, rech, statut );
                    resultSet = preparedStatement.executeQuery();
                } else {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_LIT,
                            true, rech, statut );
                    resultSet = preparedStatement.executeQuery();
                }

            } else {
                if ( statut.equals( "5" ) ) {
                	// pour les litiges "personnels" côté employé
                    statut = "2";
                } else {
                    statut = "3";
                }
                if ( research.equals( "1" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_EMPL_MAIL,
                            true, statut, mail, rech );
                    resultSet = preparedStatement.executeQuery();
                } else if ( research.equals( "2" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_EMPL_NOM,
                            true, statut, mail, rech );
                    resultSet = preparedStatement.executeQuery();
                } else if ( research.equals( "3" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_EMPL_COL,
                            true, rech, statut, mail );
                    resultSet = preparedStatement.executeQuery();
                } else {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_EMPL_LIT,
                            true, rech, statut, mail );
                    resultSet = preparedStatement.executeQuery();
                }
            }

            while ( resultSet.next() ) {
                litigeList.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return litigeList;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public List<Litige> trouverLitigesCoursier( String verrou ) throws DAOException {
        List<Litige> litigeList = new ArrayList<Litige>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LITIGES_COURSIER,
                    false, verrou );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litigeList.add( mapCoursier( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litigeList;
    }

    /**
     * @see LitigeDAO
     *  
     * @throws IOException 
     *
     */
    public void unlockLitige() throws DAOException, IOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        // Permet d'aller rechercher les informations dans le fichier de configuration
   	 	Properties properties = new Properties();
        String CHAMP_TPS_VERROU;
        int TPS_VERROU = 10000;
        // temps du verrou bloquant le problème signalé par un coursier (par défaut 10000 = 1h)
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new DAOException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_TPS_VERROU = properties.getProperty( PROPERTY_TPS_VERROU );
            TPS_VERROU = Integer.parseInt(CHAMP_TPS_VERROU);
        } catch ( IOException e ) {
            throw new IOException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_UNLOCK,
                    false, TPS_VERROU );
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void lockLitigeCoursier( String id_litige_coursier ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LOCK,
                    false, id_litige_coursier );
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public Boolean traitementProbleme( String id ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean trouve = false;
        try {
            connexion = daoFactory.getConnection();

            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_NOTI_PROB_TRAITEMENT,
                    false, id );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {

                trouve = true;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return trouve;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public Litige trouverClient( String id_colis ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Litige litige = new Litige();
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_RECHERCHE_CLIENT,
                    false, id_colis );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litige = mapClient( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litige;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void priseEnCharge( String id ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_PRISE_EN_CHARGE,
                    false, id );
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void fermerProbleme( String numLitigeCoursier ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_FERMER_PROBL,
                    false, numLitigeCoursier );
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * Prépare et envoie la requête pour trouver un problème signalé par un coursier à partir de
     * son identifiant
     * @param idNotif
     * @return un objet Litige
     * @throws DAOException
     *  
     */
    public static Litige trouverLitigeCoursier( String idNotif ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Litige litige = new Litige();
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_RECHERCHE_NOTIF,
                    false, idNotif );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                litige.setId( resultSet.getString( "ref_notification_litige_id" ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litige;
    }
    
    /**
     * Prépare et envoie la requête pour trouver un coursier à partir de
     * son identifiant
     * @param idNotif
     * @return un objet Litige
     * @throws DAOException
     *  
     */
    public static Litige trouverCoursier( Litige litige, String id ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_RECHERCHE_COURSIER,
                    false, id );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
            	litige.setNomCoursier(resultSet.getString( "nom" ) );
            	litige.setPrenomCoursier(resultSet.getString( "prenom" ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litige;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public Litige trouverProbleme( String idNotif ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Litige litige = new Litige();
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_RECHERCHE_NOTIF,
                    false, idNotif );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {
                litige = mapProbleme( resultSet );

                litige = trouverCoursier(litige, resultSet.getString( "ref_not_coursier_id" ));
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litige;
    }
    
    /**
     * @see LitigeDAO
     *  
     *
     */
    public Litige trouverLitige( String id_colis, String type ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Litige litige = new Litige();
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_TROUVE_LITIGE, false,
                    id_colis, type );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {
                litige.setId( resultSet.getString( "identifiant" ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return litige;
    }

    /**
     * @see LitigeDAO
     *  
     *
     */
    public void responsabiliteLitige( String id, String responsabilite ) throws IllegalArgumentException, DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_UPDATE_RESPON,
                    false, responsabilite, id );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création du litige, aucune ligne ajoutée dans la table." );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * @see LitigeDAO
     *  
     * @throws IOException 
     *
     */
    public boolean litigeResponsable( String mail ) throws DAOException, IOException {
        
    	// Permet d'aller rechercher les informations dans le fichier de configuration
   	 	Properties properties = new Properties();
        String CHAMP_NB_LITIGE_TORT;
        int NB_LITIGE_TORT = 3;
        // nombre de litiges maximum tenant le client pour responsable

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new DAOException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_NB_LITIGE_TORT = properties.getProperty( PROPERTY_NB_LITIGE_TORT );
            NB_LITIGE_TORT = Integer.parseInt(CHAMP_NB_LITIGE_TORT);
        } catch ( IOException e ) {
            throw new IOException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
    	
    	Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean mauvais = false;
        try {
            connexion = daoFactory.getConnection();

            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_MAUVAIS,
                    false, mail );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                if ( resultSet.getInt( "nombre_litiges_tort" ) >= NB_LITIGE_TORT )
                    mauvais = true;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return mauvais;
    }

    /**
     * Permet de récupérer l'identifiant d'un colis
     * @param resultSet
     * @return l'identifiant du colis
     * @throws SQLException
     *  
     */
    private static String mapID( ResultSet resultSet ) throws SQLException {
        int id_colis = resultSet.getInt( "identifiant" );
        String id = Integer.toString( id_colis );
        return id;
    }

    /**
     * Permet de récupérer les données d'un litige.
     * @param resultSet
     * @return un objet Litige
     * @throws SQLException
     *  
     */
    private static Litige map( ResultSet resultSet ) throws SQLException {
        Litige litige = new Litige();
        litige.setDateReponse(resultSet.getString( "date_operation" ) );
        litige.setDateCreation( resultSet.getString( "date_creation" ) );
        litige.setDate( litige.getDateCreation() );
        litige.setColis( resultSet.getString( "ref_colis_id" ) );
        litige.setICU( resultSet.getString( "ICU" ) );
        litige.setObjet( resultSet.getString( "objet" ) );
        litige.setStatut( resultSet.getString( "statut" ) );
        litige.setId( resultSet.getString( "identifiant" ) );
        return litige;
    }

	/**
	 * Permet de récupérer les informations de tous les litiges (différentes des autres map)
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 *  
	 */
    private static Litige mapAll( ResultSet resultSet ) throws SQLException {
        Litige litige = new Litige();
        litige.setDateReponse(resultSet.getString( "date_operation" ) );
        litige.setDateCreation( resultSet.getString( "date_creation" ) );
        litige.setDate( litige.getDateReponse() );
        litige.setColis( resultSet.getString( "id_colis" ) );
        litige.setICU( resultSet.getString( "ICU" ) );
        litige.setObjet( resultSet.getString( "objet" ) );
        litige.setStatut( resultSet.getString( "statut" ) );
        litige.setId( resultSet.getString( "id_litige" ) );
        litige.setNom( resultSet.getString( "nom" ) );
        litige.setMail( resultSet.getString( "mail" ) );
        litige.setPrenom( resultSet.getString( "prenom" ) );
        return litige;
    }

    /**
	 * Permet de récupérer une liste des détails des litiges.
	 * @param resultSet
	 * @return une liste d'objets Litige
	 * @throws SQLException
	 *  
	 */
    private static List<Litige> mapDetail( ResultSet resultSet, List<Litige> detail ) throws SQLException {
       
    	 Properties properties = new Properties();
         String CHAMP_LITIGE_JOUR;
         int LITIGE_JOUR = 4;
         // Nombre de jours avant qu'un client puisse renvoyer une réponse si on ne lui a pas répondu

         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

         try {
             properties.load( fichierProperties );
             CHAMP_LITIGE_JOUR = properties.getProperty( PROPERTY_LITIGE_JOUR );
             LITIGE_JOUR = Integer.parseInt(CHAMP_LITIGE_JOUR);
             
         } catch ( Exception e ) {
        	 e.printStackTrace();
         }
    	
    	Litige litige = new Litige();
        boolean trouveReponse = false;
        litige.setDateReponse(  resultSet.getString( "date_operation" ) );
        litige.setDate( litige.getDateReponse() );
        litige.setDescription( resultSet.getString( "note" ) );
        litige.setType( resultSet.getString( "type" ) );
        Date ts = new Date(System.currentTimeMillis());
        //Timestamp ts = new Timestamp(new Date().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date d = new Date(System.currentTimeMillis());
        try {
        	java.util.Date date = (java.util.Date) sdf.parse(litige.getDateReponse());
        	d = new java.sql.Date (date.getTime());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        if ( ( resultSet.getString( "type" ).equals( "3" ) ) || ( resultSet.getString( "type" ).equals( "4" ) ) ) {
            trouveReponse = true;
        }
        if (d.getDay() < (ts.getDay()- LITIGE_JOUR) || d.getYear() < (ts.getYear()) || d.getMonth() < (ts.getMonth())){
        	// permet de débloquer une réponse après 4 jours
        	trouveReponse = true;
        }
        litige.setReponse( trouveReponse );
        detail.add( litige );
        return detail;
    }

    /**
     * Permet de récupérer les informations des problèmes signalés par le coursier
     * @param resultSet
     * @return un objet Litige
     * @throws SQLException
     *  
     */
    private static Litige mapCoursier( ResultSet resultSet ) throws SQLException {
        Litige litige = new Litige();
        litige.setNumLitigeCoursier( resultSet.getString( "identifiant" ) );
        Litige litigeNotif = trouverLitigeCoursier( litige.getNumLitigeCoursier() );
        litige.setId( litigeNotif.getId() );
        litige.setDateCreation( resultSet.getString( "date_creation" ) );
        litige.setDate( litige.getDateCreation() );
        litige.setICU( resultSet.getString( "ICU" ) );
        litige.setColis( resultSet.getString( "id_colis" ) );
        litige.setObjet( resultSet.getString( "objet" ) );
        litige.setDescriptionCoursier( resultSet.getString( "note" ) );
        litige.setNomCoursier( resultSet.getString( "nom" ) );
        litige.setPrenomCoursier( resultSet.getString( "prenom" ) );
        litige.setVerrou( resultSet.getString( "verrou" ) );
        return litige;
    }

    /**
	 * Permet de récupérer les informations des problèmes signalés par le coursier 
	 * (différentes de la méthode mapCoursier)
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 *  
	 */
    private static Litige mapProbleme( ResultSet resultSet ) throws SQLException {
        Litige litige = new Litige();
        litige.setNumLitigeCoursier( resultSet.getString( "identifiant" ) );
        Litige litigeNotif = trouverLitigeCoursier( litige.getNumLitigeCoursier() );
        litige.setId( litigeNotif.getId() );
        litige.setDateCreation( resultSet.getString( "date_creation" ) );
        litige.setDate( litige.getDateCreation() );
        litige.setColis( resultSet.getString( "ref_notification_colis_id" ) );
        litige.setObjet( resultSet.getString( "type" ) );
        litige.setDescriptionCoursier( resultSet.getString( "note" ) );

        return litige;
    }

    /**
     * Permet de récupérer les informations des clients des litiges
     * @param resultSet
     * @return un objet Litige
     * @throws SQLException
     */
    private static Litige mapClient( ResultSet resultSet ) throws SQLException {
        Litige litige = new Litige();
        litige.setNom( resultSet.getString( "nom" ) );
        litige.setMail( resultSet.getString( "mail" ) );
        litige.setPrenom( resultSet.getString( "prenom" ) );
        litige.setICU( resultSet.getString( "ICU" ) );
        return litige;
    }

}
