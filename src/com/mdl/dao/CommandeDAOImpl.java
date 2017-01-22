package com.mdl.dao;

import static com.mdl.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mdl.beans.Commande;

/**
 * @see CommandeDAO
 */
public class CommandeDAOImpl implements CommandeDAO {
    private static DAOFactory   daoFactory;

    private static final String FICHIER_PROPERTIES         = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_PRIX_BEL_BAS      = "prix_bel_bas";
    private static final String PROPERTY_PRIX_AUTRE        = "prix_autre";
    private static final String PROPERTY_PRIX_INT_HAUT     = "prix_int_haut";
    private static final String PROPERTY_PRIX_CANTON       = "prix_canton";
    private static final String PROPERTY_PRIX_FORFAIT      = "prix_forfait";
    private static final String PROPERTY_PRIX_MONTANT_BAS  = "prix_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_BAS   = "pct_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_HAUT  = "pct_montant_haut";
    private static final String PROPERTY_PRIX_SANS_ASS     = "prix_sans_ass";
    private static final String PROPERTY_PRIX_ACC          = "prix_acc";
    private static final String PROPERTY_PRIX_SANS_ACC     = "prix_sans_ass";
    private static final String PROPERTY_POIDS_BASE        = "poids_base";
    private static final String PROPERTY_VALEUR_MONTANT    = "valeur_montant";
    private static final String PROPERTY_SURPLUS_BEL       = "surplus_bel";
    private static final String PROPERTY_SURPLUS_INT       = "surplus_int";

    /* sélectionne par une requete sql la liste des commandes du client */
    private static final String SQL_SELECT_LISTECOM        = "SELECT CLI.mail, COM.identifiant as id_commande, to_char(COM.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, "
                                                                   + "COM.adr_pick_nom, COM.adr_pick_prenom, COM.adr_pick_rue, COM.adr_pick_num, COM.adr_pick_boite, COM.adr_pick_code_postal, COM.adr_pick_localite, COM.telephone_expediteur,"
                                                                   + " COM.adr_dest_nom, COM.adr_dest_prenom, COM.adr_dest_rue, COM.adr_dest_num, COM.adr_dest_boite,"
                                                                   + " COM.adr_dest_code_postal, COM.adr_dest_localite, COM.adr_dest_pays, COM.telephone_destinataire,"
                                                                   + " COM.date_pickup, COM.prix, COM.frais_port, COM.statut_compte, COL.accuse_reception, COL.poids_renseigne, COL.longueur, COL.largeur, COL.hauteur, COL.valeur_estimee, COL.ref_assurance_id, COL.ICU, COL.identifiant as id_colis, "
                                                                   + " (SELECT CASE "
                                                                   + " WHEN COLI.identifiant NOT IN (SELECT ref_situation_colis_id FROM SITUATION_COLIS) THEN -1  "
                                                                   + " WHEN COLI.identifiant IN (SELECT ref_situation_colis_id FROM SITUATION_COLIS) THEN (SELECT SIT.statut FROM COLIS , SITUATION_COLIS SIT WHERE COLIS.identifiant = SIT.ref_situation_colis_id AND COL.ICU = COLIS.ICU) "
                                                                   + " END"
                                                                   + " FROM COLIS COLI"
                                                                   + " WHERE COLI.ICU = COL.ICU) AS statut "
                                                                   + " FROM COMMANDE COM, "
                                                                   + " CLIENT CLI, COLIS COL WHERE CLI.mail = ? AND COM.ref_client_id = CLI.identifiant AND COL.ref_commande_id = COM.identifiant ORDER BY statut asc, date_enregistrement desc";

    /*
     * sélectionne par une requete sql la liste des commandes de tous les
     * clients
     */
    private static final String SQL_SELECT_ALL             = "SELECT CLIENT.mail, COMMANDE.identifiant as id_commande, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, "
                                                                   + "COMMANDE.adr_pick_nom, COMMANDE.adr_pick_prenom, COMMANDE.adr_pick_rue, COMMANDE.adr_pick_num, COMMANDE.adr_pick_boite, COMMANDE.adr_pick_code_postal, COMMANDE.adr_pick_localite, COMMANDE.telephone_expediteur,"
                                                                   + " COMMANDE.adr_dest_nom, COMMANDE.adr_dest_prenom, COMMANDE.adr_dest_rue, COMMANDE.adr_dest_num, COMMANDE.adr_dest_boite,"
                                                                   + " COMMANDE.adr_dest_code_postal, COMMANDE.adr_dest_localite, COMMANDE.adr_dest_pays, COMMANDE.telephone_destinataire,"
                                                                   + " COMMANDE.date_pickup, COMMANDE.prix, COMMANDE.frais_port, COMMANDE.statut_compte, COL.accuse_reception, COL.poids_renseigne, COL.longueur, COL.largeur, COL.hauteur, COL.valeur_estimee, COL.ref_assurance_id, COL.ICU, COL.identifiant as id_colis, "
                                                                   + " (SELECT CASE "
                                                                   + " WHEN COLI.identifiant NOT IN (SELECT ref_situation_colis_id FROM SITUATION_COLIS) THEN -1  "
                                                                   + " WHEN COLI.identifiant IN (SELECT ref_situation_colis_id FROM SITUATION_COLIS) THEN (SELECT SIT.statut FROM COLIS , SITUATION_COLIS SIT WHERE COLIS.identifiant = SIT.ref_situation_colis_id AND COL.ICU = COLIS.ICU) "
                                                                   + " END"
                                                                   + " FROM COLIS COLI"
                                                                   + " WHERE COLI.ICU = COL.ICU) AS statut "
                                                                   + " FROM COMMANDE, "
                                                                   + " CLIENT , COLIS COL WHERE COMMANDE.ref_client_id = CLIENT.identifiant AND COL.ref_commande_id = COMMANDE.identifiant ORDER BY mail asc, statut asc, date_enregistrement desc";

    /* sélectionne par une requete sql une commande */
    private static final String SQL_SELECT_COM             = "SELECT CLIENT.mail, COMMANDE.identifiant as id_commande, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, "
                                                                   + "COMMANDE.adr_pick_nom, COMMANDE.adr_pick_prenom, COMMANDE.adr_pick_rue, COMMANDE.adr_pick_num, COMMANDE.adr_pick_boite, COMMANDE.adr_pick_code_postal, COMMANDE.adr_pick_localite, COMMANDE.telephone_expediteur,"
                                                                   + " COMMANDE.adr_dest_nom, COMMANDE.adr_dest_prenom, COMMANDE.adr_dest_rue, COMMANDE.adr_dest_num, COMMANDE.adr_dest_boite,"
                                                                   + " COMMANDE.adr_dest_code_postal, COMMANDE.adr_dest_localite, COMMANDE.adr_dest_pays, COMMANDE.telephone_destinataire,"
                                                                   + " COMMANDE.date_pickup, COMMANDE.prix, COMMANDE.frais_port, COMMANDE.statut_compte, COL.accuse_reception, COL.poids_renseigne, COL.longueur, COL.largeur, COL.hauteur, COL.valeur_estimee, COL.ref_assurance_id, COL.ICU, COL.identifiant as id_colis, "
                                                                   + " (SELECT CASE "
                                                                   + " WHEN COLI.identifiant NOT IN (SELECT ref_situation_colis_id FROM SITUATION_COLIS) THEN -1  "
                                                                   + " WHEN COLI.identifiant IN (SELECT ref_situation_colis_id FROM SITUATION_COLIS) THEN (SELECT SIT.statut FROM COLIS , SITUATION_COLIS SIT WHERE COLIS.identifiant = SIT.ref_situation_colis_id AND COL.ICU = COLIS.ICU) "
                                                                   + " END"
                                                                   + " FROM COLIS COLI"
                                                                   + " WHERE COLI.ICU = COL.ICU) AS statut "
                                                                   + " FROM COMMANDE, CLIENT , "
                                                                   + "COLIS COL WHERE COMMANDE.ref_client_id = CLIENT.identifiant AND COL.ICU = ? AND COL.ref_commande_id = COMMANDE.identifiant ";

    /* Sélectionne les adresses dans le carnet d'adresse dans la bd */
    private static final String SQL_SELECT_CARNETADR       = "SELECT CARNET_ADRESSE.nom, CARNET_ADRESSE.prenom, CARNET_ADRESSE.rue, CARNET_ADRESSE.num, CARNET_ADRESSE.boite, CARNET_ADRESSE.code_postal, CARNET_ADRESSE.localite, CARNET_ADRESSE.pays, CARNET_ADRESSE.telephone, CARNET_ADRESSE.identifiant "
                                                                   + " FROM CARNET_ADRESSE , CLIENT "
                                                                   + " WHERE ref_carnet_client_id = CLIENT.identifiant"
                                                                   + " AND CLIENT.mail = ? ORDER BY CARNET_ADRESSE.nom, CARNET_ADRESSE.prenom";

    /*
     * Sélectionne les adresses associées à un nom dans le carnet d'adresse dans
     * la bd
     */
    private static final String SQL_SELECT_CARNETADR_BYNOM = "SELECT CARNET_ADRESSE.nom, CARNET_ADRESSE.prenom, CARNET_ADRESSE.rue, CARNET_ADRESSE.num, CARNET_ADRESSE.boite, CARNET_ADRESSE.code_postal, CARNET_ADRESSE.localite, CARNET_ADRESSE.pays, CARNET_ADRESSE.telephone, CARNET_ADRESSE.identifiant "
                                                                   + " FROM CARNET_ADRESSE , CLIENT "
                                                                   + " WHERE ref_carnet_client_id = CLIENT.identifiant"
                                                                   + " AND CLIENT.mail = ? AND CARNET_ADRESSE.nom = ?";

    /* Insert de la commande dans la bd */
    private static final String SQL_INSERT_COMMANDE        = "INSERT INTO COMMANDE (date_enregistrement, adr_pick_nom, adr_pick_prenom, adr_pick_rue, adr_pick_num, "
                                                                   + "adr_pick_boite, adr_pick_code_postal, adr_pick_localite, telephone_expediteur, adr_dest_nom, adr_dest_prenom, adr_dest_rue, "
                                                                   + "adr_dest_num, adr_dest_boite, adr_dest_code_postal, adr_dest_localite, adr_dest_pays, telephone_destinataire, "
                                                                   + "date_pickup, prix, frais_port, statut_compte, ref_client_id, ref_centre_source_id, ref_centre_dest_id) "
                                                                   + "VALUES (to_date(to_char(sysdate,'DD-MM-YYYY HH24:MI:SS'),'DD-MM-YYYY HH24:MI:SS' ), ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /* Insert du colis de la commande dans la bd */
    private static final String SQL_INSERT_COLIS           = "INSERT INTO COLIS (poids_renseigne, longueur, largeur, hauteur, valeur_estimee, "
                                                                   + "accuse_reception, ref_commande_id, ref_assurance_id, ICU) "
                                                                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /* Insert d'une adresse dans le carnet d'adresse dans la bd */
    private static final String SQL_INSERT_CARNETADR       = "INSERT INTO CARNET_ADRESSE"
                                                                   + " (nom, prenom, pays, code_postal, localite, num, rue, boite, telephone, ref_carnet_client_id)"
                                                                   + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /* DELETE d'une adresse dans le carnet d'adresse dans la bd */
    private static final String SQL_DELETEADRCARNET        = "DELETE FROM CARNET_ADRESSE"
                                                                   + " WHERE identifiant = ?";

    private static final String SQL_EXISTICU               = "SELECT COLIS.ICU, CLIENT.mail"
                                                                   + " FROM CLIENT , COMMANDE , COLIS "
                                                                   + " WHERE CLIENT.mail = ?"
                                                                   + " AND COLIS.ref_commande_id = COMMANDE.identifiant"
                                                                   + " AND COLIS.icu = ?";

    private static final String SQL_ISICU                  = "SELECT *"
                                                                   + " FROM COLIS "
                                                                   + " WHERE ICU = ?";

    private static final String SQL_NBSURFACTIMPAYEES      = "SELECT COUNT(*)"
                                                                   + " FROM SURFACTURATION , COLIS , COMMANDE , CLIENT  "
                                                                   + "WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id  AND COMMANDE.identifiant = COLIS.ref_commande_id AND CLIENT.identifiant = COMMANDE.ref_client_id "
                                                                   + "AND ("
                                                                   + " (SURFACTURATION.date_paiement_effectif IS NOT NULL AND (SURFACTURATION.date_paiement_effectif - SURFACTURATION.date_notification) > 15 )"
                                                                   + " OR "
                                                                   + "(SURFACTURATION.date_paiement_effectif IS NULL AND ( sysdate - SURFACTURATION.date_notification) > 15) )"
                                                                   + " AND (sysdate - COMMANDE.date_enregistrement) < 365 "
                                                                   + " AND CLIENT.mail = ? ";

    private static final String SQL_CHIFFREAFFAIRE         = "SELECT SUM(prix) + COALESCE(SUM(difference_prix), 0) AS prix"
                                                                   + " FROM ((COMMANDE INNER JOIN CLIENT ON CLIENT.identifiant = COMMANDE.ref_client_id)"
                                                                   + " INNER JOIN COLIS ON COMMANDE.identifiant = COLIS.ref_commande_id ) LEFT OUTER JOIN SURFACTURATION ON COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                   + " WHERE EXTRACT (DAY FROM systimestamp - COMMANDE.date_enregistrement) < 365"
                                                                   + " AND CLIENT.mail = ?";

    private static final String SQL_NBLITIGETORT           = "SELECT COUNT(*) "
                                                                   + "FROM LITIGE, COLIS , COMMANDE , CLIENT  "
                                                                   + "WHERE COLIS.identifiant = LITIGE.ref_colis_id "
                                                                   + "AND COMMANDE.identifiant = COLIS.ref_commande_id "
                                                                   + "AND CLIENT.identifiant = COMMANDE.ref_client_id "
                                                                   + "AND LITIGE.responsabilite_client = 1 AND EXTRACT (DAY FROM systimestamp - COMMANDE.date_enregistrement) < 365 "
                                                                   + "AND CLIENT.mail = ? ";

    // est-ce que le client a déjà eu un colis gratuit, insérer mail du client
    // NULL si non, un chiffre si oui (garder le chiffre!)
    private static final String SQL_IDLASTCOLIS            = "SELECT STATUT_CLIENT.ref_statut_colis_id"
                                                                   + " FROM STATUT_CLIENT , CLIENT "
                                                                   + " WHERE STATUT_CLIENT.ref_statut_client_id = CLIENT.identifiant"
                                                                   + " AND CLIENT.mail = ?";
    // si c'est NULL, insérer mail client, retourne le nbr de commande passée
    // depuis l'inscription
    private static final String SQL_NBCOLIS_BYMAIL         = "SELECT COUNT(COMMANDE.identifiant) AS nbcolis"
                                                                   + " FROM COMMANDE , CLIENT "
                                                                   + " WHERE CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                   + " AND CLIENT.mail = ?";
    // si c'est un chiffre, insérer le mail du client et le chiffre retenu de la
    // requête précédente, retourne le nbr de commande passée depuis la dernière
    // gratuite
    private static final String SQL_NBCOLIS_BYID           = "SELECT COUNT( COMMANDE.identifiant ) AS nbcolis"
                                                                   + " FROM COLIS , COMMANDE , CLIENT "
                                                                   + " WHERE CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                   + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                   + " AND CLIENT.mail = ?"
                                                                   + " AND to_date(to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') > "
                                                                   + "( SELECT to_date(to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement "
                                                                   + "  FROM COMMANDE, COLIS"
                                                                   + " WHERE COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                   + " AND COLIS.identifiant = ? )";

    private static final String SQL_SELECTIDCOLIS          = "SELECT identifiant"
                                                                   + " FROM COLIS"
                                                                   + " WHERE ref_commande_id = ?";

    private static final String SQL_SELECT_BEL             = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, COALESCE(SITUATION_COLIS.statut,-1) AS statut"
                                                                   + " FROM (((CLIENT INNER JOIN COMMANDE ON (CLIENT.identifiant = COMMANDE.ref_client_id)) INNER JOIN COLIS ON "
                                                                   + " (COMMANDE.identifiant = COLIS.ref_commande_id))  LEFT OUTER JOIN SITUATION_COLIS  ON (COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id))  "
                                                                   + "WHERE COMMANDE.ref_centre_source_id NOT IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) "
                                                                   + "AND COMMANDE.ref_centre_dest_id NOT IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) ORDER BY CLIENT.mail, statut asc, date_enregistrement desc";

    private static final String SQL_SELECT_ENT             = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, COALESCE(SITUATION_COLIS.statut,-1) AS statut, (SELECT adr_localite FROM CENTRE_DISPATCHING WHERE identifiant = COMMANDE.ref_centre_source_id) AS pays "
                                                                   + "FROM CLIENT INNER JOIN COMMANDE ON (CLIENT.identifiant = COMMANDE.ref_client_id) INNER JOIN COLIS ON (COMMANDE.identifiant = COLIS.ref_commande_id) LEFT OUTER JOIN SITUATION_COLIS  ON (COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id) "
                                                                   + "WHERE COMMANDE.ref_centre_source_id IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) ORDER BY CLIENT.mail, statut asc, date_enregistrement desc";

    private static final String SQL_SELECT_SOR             = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, SITUATION_COLIS.statut,  "
                                                                   + "(SELECT adr_localite FROM CENTRE_DISPATCHING WHERE identifiant = COMMANDE.ref_centre_dest_id) AS pays "
                                                                   + "FROM CLIENT , COMMANDE , COLIS , SITUATION_COLIS  "
                                                                   + "WHERE CLIENT.identifiant = COMMANDE.ref_client_id AND COMMANDE.identifiant = COLIS.ref_commande_id "
                                                                   + "AND COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id AND COMMANDE.ref_centre_dest_id IN "
                                                                   + "(SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) ORDER BY CLIENT.mail, statut asc, date_enregistrement desc";

    private static final String SQL_SELECT_LISTECOM_BEL    = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, COALESCE(SITUATION_COLIS.statut,-1) AS statut "
                                                                   + "FROM CLIENT INNER JOIN COMMANDE ON (CLIENT.identifiant = COMMANDE.ref_client_id) INNER JOIN COLIS ON (COMMANDE.identifiant = COLIS.ref_commande_id) LEFT OUTER JOIN SITUATION_COLIS  ON (COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id) "
                                                                   + "WHERE CLIENT.mail = ? "
                                                                   + "AND COMMANDE.ref_centre_source_id NOT IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) "
                                                                   + "AND COMMANDE.ref_centre_dest_id NOT IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) ORDER BY CLIENT.mail, statut asc, date_enregistrement desc";

    private static final String SQL_SELECT_LISTECOM_ENT    = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, COALESCE(SITUATION_COLIS.statut,-1) AS statut, (SELECT adr_localite FROM CENTRE_DISPATCHING WHERE identifiant = COMMANDE.ref_centre_source_id) AS pays "
                                                                   + "FROM CLIENT INNER JOIN COMMANDE ON (CLIENT.identifiant = COMMANDE.ref_client_id) INNER JOIN COLIS ON (COMMANDE.identifiant = COLIS.ref_commande_id) LEFT OUTER JOIN SITUATION_COLIS  ON (COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id) "
                                                                   + "WHERE CLIENT.mail = ? AND COMMANDE.ref_centre_source_id IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) GROUP BY statut asc ORDER BY date_enregistrement ORDER BY statut asc, date_enregistrement desc";

    private static final String SQL_SELECT_LISTECOM_SOR    = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, SITUATION_COLIS.statut,  "
                                                                   + "(SELECT adr_localite FROM CENTRE_DISPATCHING WHERE identifiant = COMMANDE.ref_centre_dest_id) AS pays "
                                                                   + "FROM CLIENT , COMMANDE , COLIS , SITUATION_COLIS  "
                                                                   + "WHERE CLIENT.mail = ? AND CLIENT.identifiant = COMMANDE.ref_client_id AND COMMANDE.identifiant = COLIS.ref_commande_id "
                                                                   + "AND COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id AND COMMANDE.ref_centre_dest_id IN "
                                                                   + "(SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) ORDER BY statut asc, date_enregistrement desc";

    private static final String SQL_SELECT_COM_BEL         = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, COALESCE(SITUATION_COLIS.statut,-1) AS statut "
                                                                   + "FROM CLIENT INNER JOIN COMMANDE ON (CLIENT.identifiant = COMMANDE.ref_client_id) INNER JOIN COLIS ON (COMMANDE.identifiant = COLIS.ref_commande_id)  LEFT OUTER JOIN SITUATION_COLIS  ON (COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id) "
                                                                   + "WHERE COLIS.ICU = ?  "
                                                                   + "AND COMMANDE.ref_centre_source_id NOT IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2) "
                                                                   + "AND COMMANDE.ref_centre_dest_id NOT IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2)";

    private static final String SQL_SELECT_COM_ENT         = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, COALESCE(SITUATION_COLIS.statut,-1) AS statut, (SELECT adr_localite FROM CENTRE_DISPATCHING WHERE identifiant = COMMANDE.ref_centre_source_id) AS pays "
                                                                   + "FROM CLIENT INNER JOIN COMMANDE ON (CLIENT.identifiant = COMMANDE.ref_client_id) INNER JOIN COLIS ON (COMMANDE.identifiant = COLIS.ref_commande_id) LEFT OUTER JOIN SITUATION_COLIS  ON (COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id) "
                                                                   + "WHERE COLIS.ICU = ? AND COMMANDE.ref_centre_source_id IN (SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2)";

    private static final String SQL_SELECT_COM_SOR         = "SELECT CLIENT.mail, COLIS.ICU, to_char(COMMANDE.date_enregistrement, 'DD-MM-YYYY HH24:MI:SS') as date_enregistrement, COMMANDE.prix, SITUATION_COLIS.statut,  "
                                                                   + "(SELECT adr_localite FROM CENTRE_DISPATCHING WHERE identifiant = COMMANDE.ref_centre_dest_id) AS pays "
                                                                   + "FROM CLIENT , COMMANDE , COLIS , SITUATION_COLIS  "
                                                                   + "WHERE COLIS.ICU = ? AND CLIENT.identifiant = COMMANDE.ref_client_id AND COMMANDE.identifiant = COLIS.ref_commande_id "
                                                                   + "AND COLIS.identifiant = SITUATION_COLIS.ref_situation_colis_id AND COMMANDE.ref_centre_dest_id IN "
                                                                   + "(SELECT identifiant FROM CENTRE_DISPATCHING WHERE centre_frontiere = 2)";

    private static final String SQL_NOTIFIER_ETRANGER_1	  = "update situation_colis set statut = 6 where ref_situation_colis_id = (select identifiant from COLIS where ICU_etranger = ?)";

    private static final String SQL_NOTIFIER_ETRANGER_2	  = "insert into operation_colis "
														    	  + "(type, ref_colis_id, ref_centre_id) values "
														    	  + "(4,(select identifiant from colis where icu_etranger= ?), (select ref_centre_dest_id from commande, colis where colis.ref_commande_id = commande.identifiant and icu_etranger = ?))";
														    
    CommandeDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /**
     * @see CommandeDAO
     */
    public List<Commande> lister( String email ) throws DAOException {
        List<Commande> listeCommandes = new ArrayList<Commande>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_LISTECOM,
                    false, email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel ResulSet retourné */
            while ( resultSet.next() ) {
                listeCommandes.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return listeCommandes;
    }

    /**
     * @see CommandeDAO
     */
    public List<Commande> listerAll() throws DAOException {
        List<Commande> listeCommandes = new ArrayList<Commande>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_ALL,
                    false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel ResulSet retourné */
            while ( resultSet.next() ) {
                listeCommandes.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return listeCommandes;
    }

    /**
     * @see CommandeDAO
     */
    public Commande trouver( String icu ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Commande commande = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_COM, false,
                    icu );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                commande = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return commande;
    }

    /**
     * @see CommandeDAO
     */
    public List<List<String>> listerCarnet( String email ) throws DAOException {
        List<List<String>> listeCarnet = new ArrayList<List<String>>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_CARNETADR,
                    false, email );
            resultSet = preparedStatement.executeQuery();

            /* Parcours des lignes de données de l'éventuel ResulSet retourné */
            /**
             * place dans l'attribut carnet_adresse du bean commande la liste
             * des adresses du carnet d'adresse du client
             */
            while ( resultSet.next() ) {
                List<String> adresse = new ArrayList<String>();
                adresse.add( resultSet.getString( "nom" ) );
                adresse.add( resultSet.getString( "prenom" ) );
                adresse.add( resultSet.getString( "rue" ) );
                adresse.add( resultSet.getString( "num" ) );
                adresse.add( resultSet.getString( "boite" ) );
                adresse.add( resultSet.getString( "code_postal" ) );
                adresse.add( resultSet.getString( "localite" ) );
                adresse.add( resultSet.getString( "pays" ) );
                adresse.add( resultSet.getString( "telephone" ) );
                adresse.add( resultSet.getString( "identifiant" ) );
                listeCarnet.add( adresse );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return listeCarnet;
    }

    /**
     * @see CommandeDAO
     */
    public List<List<String>> selectCarnet( String email, String nom ) throws DAOException {
        List<List<String>> listeCarnet = new ArrayList<List<String>>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                    SQL_SELECT_CARNETADR_BYNOM,
                    false, email, nom );
            resultSet = preparedStatement.executeQuery();

            /* Parcours des lignes de données de l'éventuel ResulSet retourné */
            /**
             * place dans l'attribut carnet_adresse du bean commande la liste
             * des adresses du carnet d'adresse du client
             */
            while ( resultSet.next() ) {
                List<String> adresse = new ArrayList<String>();
                adresse.add( resultSet.getString( "nom" ) );
                adresse.add( resultSet.getString( "prenom" ) );
                adresse.add( resultSet.getString( "rue" ) );
                adresse.add( resultSet.getString( "num" ) );
                adresse.add( resultSet.getString( "boite" ) );
                adresse.add( resultSet.getString( "code_postal" ) );
                adresse.add( resultSet.getString( "localite" ) );
                adresse.add( resultSet.getString( "pays" ) );
                adresse.add( resultSet.getString( "telephone" ) );
                adresse.add( resultSet.getString( "identifiant" ) );
                listeCarnet.add( adresse );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return listeCarnet;
    }

    @Override
    /**
     * @see commandeDAO
     */
    public void deleteCarnet( int id ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_DELETEADRCARNET,
                    false,
                    id );
            preparedStatement.execute();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

    }

    /**
     * @see CommandeDAO
     */
    public boolean verifICU( String icu ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_ISICU, false,
                    icu );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return true;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return false;
    }

    /**
     * @see CommandeDAO
     */
    public boolean existICU( String icu, String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_EXISTICU, false,
                    email, icu );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return true;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return false;
    }

    /**
     * @see CommandeDAO
     */
    public int chiffreAffaire( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_CHIFFREAFFAIRE,
                    false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt( "prix" );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }

    /**
     * @see CommandeDAO
     */
    public int nbSurfactImpayees( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_NBSURFACTIMPAYEES,
                    false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt( "COUNT(*)" );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }

    /**
     * @see CommandeDAO
     */
    public int nbLitigesTort( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_NBLITIGETORT,
                    false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt( "COUNT(*)" );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }

    /**
     * @see CommandeDAO
     */
    public int getIdLastColis( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_IDLASTCOLIS,
                    false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt( "ref_statut_colis_id" );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }

    /**
     * @see CommandeDAO
     */
    public int nbComForFree( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_NBCOLIS_BYMAIL,
                    false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt( "nbcolis" );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }

    /**
     * @see CommandeDAO
     */
    public int getIdColis( int id_commande ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECTIDCOLIS,
                    false,
                    id_commande );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt( "identifiant" );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }

    /**
     * @see CommandeDAO
     */
    public int nbComForFree( String email, int col_id ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_NBCOLIS_BYID,
                    false, email, col_id );
            resultSet = preparedStatement.executeQuery();

            /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                int nbcolis = resultSet.getInt( "nbcolis" );
                return nbcolis;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }

    /**
     * @see CommandeDAO
     */
    public int creer( Commande commande, Integer id_client ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        /**
         * Insert des informations sur une commande
         */
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            String generatedColumns[] = { "identifiant" };
            /* Préparation des informations à mettre dans la requête */
            preparedStatement = connexion.prepareStatement( SQL_INSERT_COMMANDE, generatedColumns );
            preparedStatement.setString( 1, commande.getNom_expediteur() );
            preparedStatement.setString( 2, commande.getPrenom_expediteur() );
            preparedStatement.setString( 3, commande.getAdresse_rue_expediteur() );
            preparedStatement.setString( 4, commande.getAdresse_num_expediteur() );
            preparedStatement.setString( 5, commande.getAdresse_boite_expediteur() );
            preparedStatement.setString( 6, commande.getAdresse_code_expediteur() );
            preparedStatement.setString( 7, commande.getAdresse_loc_expediteur() );
            preparedStatement.setString( 8, commande.getTel_expediteur() );
            preparedStatement.setString( 9, commande.getNom_destinataire() );
            preparedStatement.setString( 10, commande.getPrenom_destinataire() );
            preparedStatement.setString( 11, commande.getAdresse_rue_destinataire() );
            preparedStatement.setString( 12, commande.getAdresse_num_destinataire() );
            preparedStatement.setString( 13, commande.getAdresse_boite_destinataire() );
            preparedStatement.setString( 14, commande.getAdresse_code_destinataire() );
            preparedStatement.setString( 15, commande.getAdresse_loc_destinataire() );
            preparedStatement.setString( 16, commande.getAdresse_pays_destinataire() );
            preparedStatement.setString( 17, commande.getTel_destinataire() );
            java.sql.Timestamp timeStampDatePickup = new Timestamp( commande.getDatePickup().getTime() );
            preparedStatement.setTimestamp( 18, timeStampDatePickup );
            preparedStatement.setDouble( 19, commande.getPrix() );
            preparedStatement.setDouble( 20, commande.getPrixCentreTraverse() );
            preparedStatement.setInt( 21, commande.getStatutCompte() );
            preparedStatement.setInt( 22, id_client );
            preparedStatement.setInt( 23, commande.getCentre_exp_id() );
            preparedStatement.setInt( 24, commande.getCentre_dest_id() );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création de la commande, aucune ligne ajoutée dans la table." );
            }
            /* Récupération de l'id auto-généré lors de l'insertion */

            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                /*
                 * Puis initialisation de la propriété id du bean Commande avec
                 * sa valeur
                 */
                commande.setId( valeursAutoGenerees.getInt( 1 ) );
            } else {
                throw new DAOException( "Echec de la création de la commande en bd, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }

        /**
         * Insert des informations sur le colis de la commande
         */
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            int ass = -1;
            if ( commande.getTypeAssurance().equals( "forfait" ) ) {
                ass = 1;
            } else if ( commande.getTypeAssurance().equals( "montant" ) ) {
                ass = 2;
            } else {
                ass = 3;
            }

            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_COLIS, true,
                    commande.getPoids(), commande.getDimension_longueur(), commande.getDimension_largeur(),
                    commande.getDimension_hauteur(),
                    commande.getValeurEstimee(), commande.getAccuseReception(), commande.getId(), ass,
                    commande.getIcu() );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requete d'insertion */
            if ( statut == 0 ) {
                throw new DAOException(
                        "Echec de la création du colis de la commande, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

        if ( commande.isAdd_carnet_exp() ) {
            /**
             * Insert de l'adresse d'expédition dans le carnet d'adresses
             * associés à un client au moment de la création de la commande en
             * BD seulement si la case a été cochée.
             */
            try {
                /* Récupération d'une connexion depuis la Factory */
                connexion = daoFactory.getConnection();

                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_CARNETADR,
                        true,
                        commande.getNom_expediteur(), commande.getPrenom_expediteur(), "Belgique",
                        commande.getAdresse_code_expediteur(), commande.getAdresse_loc_expediteur(),
                        commande.getAdresse_num_expediteur(), commande.getAdresse_rue_expediteur(),
                        commande.getAdresse_boite_expediteur(), commande.getTel_expediteur(), id_client );

                int statut = preparedStatement.executeUpdate();
                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException(
                            "Echec de la création d'une adresse dans le carnet, aucune ligne ajoutée dans la table." );
                }
            } catch ( SQLException e ) {
                throw new DAOException( e );
            } finally {
                fermeturesSilencieuses( preparedStatement, connexion );
            }
        }

        if ( commande.isAdd_carnet_dest() ) {
            /**
             * Insert de l'adresse de destination dans le carnet d'adresses
             * associés à un client au moment de la création de la commande en
             * BD seulement si la case a été cochée.
             */
            try {
                /* Récupération d'une connexion depuis la Factory */
                connexion = daoFactory.getConnection();
                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_CARNETADR,
                        true,
                        commande.getNom_destinataire(), commande.getPrenom_destinataire(),
                        commande.getAdresse_pays_destinataire(), commande.getAdresse_code_destinataire(),
                        commande.getAdresse_loc_destinataire(), commande.getAdresse_num_destinataire(),
                        commande.getAdresse_rue_destinataire(), commande.getAdresse_boite_destinataire(),
                        commande.getTel_destinataire(), id_client );

                int statut = preparedStatement.executeUpdate();
                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException(
                            "Echec de la création d'une adresse dans le carnet, aucune ligne ajoutée dans la table." );
                }
            } catch ( SQLException e ) {
                throw new DAOException( e );
            } finally {
                fermeturesSilencieuses( preparedStatement, connexion );
            }
        }
        return commande.getId();
    }

    /**
     * @see CommandeDAO
     */
    public void addCarnet( List<String> carnet, int id_client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        /**
         * Insert de l'adresse dans le carnet d'adresses associés à un client en
         * BD
         */
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();

            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT_CARNETADR,
                    true,
                    carnet.get( 0 ), carnet.get( 1 ), carnet.get( 2 ), carnet.get( 3 ), carnet.get( 4 ),
                    carnet.get( 5 ),
                    carnet.get( 6 ), carnet.get( 7 ), carnet.get( 8 ), id_client );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException(
                        "Echec de la création d'une adresse dans le carnet, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

    }

    /**
     * @see CommandeDAO
     */
    public List<Commande> trouverCommandeRech( String rech, String research, String mail ) throws DAOException {
        List<Commande> commandeList = new ArrayList<Commande>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String pays = "autre";
        /**
         * Retrouve les commandes en fonction de l'objet de recherche (mail ou
         * icu)
         */
        try {
            connexion = daoFactory.getConnection();
            if ( research.equals( "1" ) ) {
                // Cas où on recherche par mail
                if ( mail.equals( "all" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_LISTECOM, true, rech );
                } else if ( mail.equals( "Bel" ) ) {
                    pays = "bel";
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_LISTECOM_BEL, true, rech );
                } else if ( mail.equals( "Ent" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_LISTECOM_ENT, true, rech );
                } else {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_LISTECOM_SOR, true, rech );
                }

            } else {
                // Cas où on recherche par ICU
                if ( mail.equals( "all" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_COM, true, rech );
                } else if ( mail.equals( "Bel" ) ) {
                    pays = "bel";
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_COM_BEL, true, rech );
                } else if ( mail.equals( "Ent" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_COM_ENT, true, rech );
                } else {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_COM_SOR, true, rech );
                }
            }
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                // Permet de trouver les informations de la requête en fonction
                // des commandes à lister

                if ( mail.equals( "Bel" ) || mail.equals( "Ent" ) || mail.equals( "Sor" ) ) {

                    commandeList.add( mapComEmpl( resultSet, pays ) );
                } else {
                    commandeList.add( map( resultSet ) );
                }
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return commandeList;
    }

    /**
     * @see CommandeDAO
     */
    public List<Commande> listerBel() throws DAOException {
        List<Commande> listeCommandes = new ArrayList<Commande>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_BEL,
                    false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel ResulSet retourné */
            while ( resultSet.next() ) {
                listeCommandes.add( mapComEmpl( resultSet, "bel" ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return listeCommandes;
    }

    /**
     * @see CommandeDAO
     */
    public List<Commande> listerEnt() throws DAOException {
        List<Commande> listeCommandes = new ArrayList<Commande>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_ENT,
                    false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel ResulSet retourné */
            while ( resultSet.next() ) {
                listeCommandes.add( mapComEmpl( resultSet, "autre" ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return listeCommandes;
    }

    /**
     * @see CommandeDAO
     */
    public List<Commande> listerSor() throws DAOException {
        List<Commande> listeCommandes = new ArrayList<Commande>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_SOR,
                    false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel ResulSet retourné */
            while ( resultSet.next() ) {
                listeCommandes.add( mapComEmpl( resultSet, "autre" ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return listeCommandes;
    }

    public void notifier( String icu ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_NOTIFIER_ETRANGER_1,
                    false,
                    icu );
            preparedStatement.execute();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_NOTIFIER_ETRANGER_2,
                    false,
                    icu, icu );
            preparedStatement.execute();
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

    }
    /**
     * 
     * @param resultSet
     * @return Place les résultats d'une reqûete sql de sélection dans le bean
     *         de commande
     * @throws SQLException
     */
    private static Commande map( ResultSet resultSet ) throws SQLException {

        // Permet d'aller rechercher les informations dans le fichier de
        // configuration
        Properties properties = new Properties();
        String CHAMP_SURPLUS_BEL;
        String CHAMP_SURPLUS_INT;
        String CHAMP_PRIX_BEL_BAS;
        String CHAMP_PRIX_AUTRE;
        String CHAMP_PRIX_INT_HAUT;
        String CHAMP_PRIX_CANTON;
        String CHAMP_PRIX_FORFAIT;
        String CHAMP_PRIX_MONTANT_BAS;
        String CHAMP_PCT_MONTANT_BAS;
        String CHAMP_PCT_MONTANT_HAUT;
        String CHAMP_PRIX_SANS_ASS;
        String CHAMP_PRIX_ACC;
        String CHAMP_PRIX_SANS_ACC;
        String CHAMP_POIDS_BASE;
        String CHAMP_VALEUR_MONTANT;

        double SURPLUS_BEL = 10.0;
        double SURPLUS_INT = 15.0;
        double PRIX_BEL_BAS = 10.0;
        double PRIX_INT_HAUT = 35.0;
        double PRIX_AUTRE = 20.0;
        double PRIX_CANTON = 5.0;
        double PRIX_FORFAIT = 15.0;
        double PRIX_MONTANT_BAS = 7.0;
        double PCT_MONTANT_BAS = 0.03;
        double PCT_MONTANT_HAUT = 0.05;
        double PRIX_SANS_ASS = 0.0;
        double PRIX_ACC = 2.5;
        double PRIX_SANS_ACC = 0.0;
        double POIDS_BASE = 5.0;
        double VALEUR_MONTANT = 2000.0;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        try {
            properties.load( fichierProperties );
            CHAMP_SURPLUS_BEL = properties.getProperty( PROPERTY_SURPLUS_BEL );
            CHAMP_SURPLUS_INT = properties.getProperty( PROPERTY_SURPLUS_INT );
            CHAMP_PRIX_BEL_BAS = properties.getProperty( PROPERTY_PRIX_BEL_BAS );
            CHAMP_PRIX_AUTRE = properties.getProperty( PROPERTY_PRIX_AUTRE );
            CHAMP_PRIX_INT_HAUT = properties.getProperty( PROPERTY_PRIX_INT_HAUT );
            CHAMP_PRIX_CANTON = properties.getProperty( PROPERTY_PRIX_CANTON );
            CHAMP_PRIX_FORFAIT = properties.getProperty( PROPERTY_PRIX_FORFAIT );
            CHAMP_PRIX_MONTANT_BAS = properties.getProperty( PROPERTY_PRIX_MONTANT_BAS );
            CHAMP_PCT_MONTANT_BAS = properties.getProperty( PROPERTY_PCT_MONTANT_BAS );
            CHAMP_PCT_MONTANT_HAUT = properties.getProperty( PROPERTY_PCT_MONTANT_HAUT );
            CHAMP_PRIX_SANS_ASS = properties.getProperty( PROPERTY_PRIX_SANS_ASS );
            CHAMP_PRIX_ACC = properties.getProperty( PROPERTY_PRIX_ACC );
            CHAMP_PRIX_SANS_ACC = properties.getProperty( PROPERTY_PRIX_SANS_ACC );
            CHAMP_POIDS_BASE = properties.getProperty( PROPERTY_POIDS_BASE );
            CHAMP_VALEUR_MONTANT = properties.getProperty( PROPERTY_VALEUR_MONTANT );

            SURPLUS_BEL = Double.parseDouble( CHAMP_SURPLUS_BEL );
            SURPLUS_INT = Double.parseDouble( CHAMP_SURPLUS_INT );
            PRIX_BEL_BAS = Double.parseDouble( CHAMP_PRIX_BEL_BAS );
            PRIX_AUTRE = Double.parseDouble( CHAMP_PRIX_AUTRE );
            PRIX_INT_HAUT = Double.parseDouble( CHAMP_PRIX_INT_HAUT );
            PRIX_CANTON = Double.parseDouble( CHAMP_PRIX_CANTON );
            PRIX_FORFAIT = Double.parseDouble( CHAMP_PRIX_FORFAIT );
            PRIX_MONTANT_BAS = Double.parseDouble( CHAMP_PRIX_MONTANT_BAS );
            PCT_MONTANT_BAS = Double.parseDouble( CHAMP_PCT_MONTANT_BAS );
            PCT_MONTANT_HAUT = Double.parseDouble( CHAMP_PCT_MONTANT_HAUT );
            PRIX_SANS_ASS = Double.parseDouble( CHAMP_PRIX_SANS_ASS );
            PRIX_ACC = Double.parseDouble( CHAMP_PRIX_ACC );
            PRIX_SANS_ACC = Double.parseDouble( CHAMP_PRIX_SANS_ACC );
            POIDS_BASE = Double.parseDouble( CHAMP_POIDS_BASE );
            VALEUR_MONTANT = Double.parseDouble( CHAMP_VALEUR_MONTANT );

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        Commande commande = new Commande();
        commande.setDatePickup( (Date) resultSet.getObject( "date_pickup" ) );
        commande.setDate_enregistrement( resultSet.getString( "date_enregistrement" ) );
        commande.setNom_expediteur( resultSet.getString( "adr_pick_nom" ) );
        commande.setClient( resultSet.getString( "mail" ) );
        commande.setIdColis( resultSet.getString( "id_colis" ) );

        commande.setPrenom_expediteur( resultSet.getString( "adr_pick_prenom" ) );
        commande.setAdresse_rue_expediteur( resultSet.getString( "adr_pick_rue" ) );
        commande.setAdresse_num_expediteur( resultSet.getString( "adr_pick_num" ) );
        commande.setAdresse_boite_expediteur( resultSet.getString( "adr_pick_boite" ) );
        commande.setAdresse_code_expediteur( resultSet.getString( "adr_pick_code_postal" ) );
        commande.setAdresse_num_expediteur( resultSet.getString( "adr_pick_num" ) );
        commande.setAdresse_loc_expediteur( resultSet.getString( "adr_pick_localite" ) );
        commande.setTel_expediteur( resultSet.getString( "telephone_expediteur" ) );

        commande.setAdresse_rue_destinataire( resultSet.getString( "adr_dest_rue" ) );
        commande.setAdresse_num_destinataire( resultSet.getString( "adr_dest_num" ) );
        commande.setAdresse_boite_destinataire( resultSet.getString( "adr_dest_boite" ) );
        commande.setAdresse_code_destinataire( resultSet.getString( "adr_dest_code_postal" ) );
        commande.setAdresse_loc_destinataire( resultSet.getString( "adr_dest_localite" ) );
        commande.setAdresse_pays_destinataire( resultSet.getString( "adr_dest_pays" ) );
        commande.setNom_destinataire( resultSet.getString( "adr_dest_nom" ) );
        commande.setPrenom_destinataire( resultSet.getString( "adr_dest_prenom" ) );
        commande.setTel_destinataire( resultSet.getString( "telephone_destinataire" ) );

        commande.setId( resultSet.getInt( "id_commande" ) );
        commande.setAccuseReception( resultSet.getBoolean( "accuse_reception" ) );
        commande.setDimension_hauteur( resultSet.getInt( "hauteur" ) );
        commande.setDimension_longueur( resultSet.getInt( "longueur" ) );
        commande.setDimension_largeur( resultSet.getInt( "largeur" ) );
        commande.setValeurEstimee( resultSet.getDouble( "valeur_estimee" ) );
        commande.setPoids( resultSet.getDouble( "poids_renseigne" ) );
        commande.setStatutCompte( resultSet.getInt( "statut_compte" ) );

        if ( resultSet.getInt( "ref_assurance_id" ) == 1 ) {
            commande.setTypeAssurance( "forfait" );
        } else if ( resultSet.getInt( "ref_assurance_id" ) == 2 ) {
            commande.setTypeAssurance( "montant" );
        } else {
            commande.setTypeAssurance( "aucune" );
        }

        /*
         * Gestion du prix de la commande !!
         */
        commande.setPrix( resultSet.getDouble( "prix" ) );

        if ( commande.getAdresse_pays_destinataire().equals( "Belgique" ) && commande.getPoids() <= POIDS_BASE ) {
            commande.setPrixBase( PRIX_BEL_BAS );
        } else if ( !commande.getAdresse_pays_destinataire().equals( "Belgique" ) && commande.getPoids() > POIDS_BASE ) {
            commande.setPrixBase( PRIX_INT_HAUT );
        } else {
            commande.setPrixBase( PRIX_AUTRE );
        }

        // {min entre centre traversés et 4}
        commande.setPrixCentreTraverse( resultSet.getDouble( "frais_port" ) );

        if ( commande.getTypeAssurance().equals( "forfait" ) ) {
            commande.setPrixAssurance( PRIX_FORFAIT );
        } else if ( commande.getTypeAssurance().equals( "montant" ) && commande.getValeurEstimee() < VALEUR_MONTANT ) {
            commande.setPrixAssurance( PRIX_MONTANT_BAS + PCT_MONTANT_BAS * commande.getValeurEstimee() );
        } else if ( commande.getTypeAssurance().equals( "montant" ) && commande.getValeurEstimee() >= VALEUR_MONTANT ) {
            commande.setPrixAssurance( PCT_MONTANT_HAUT * commande.getValeurEstimee() );
        } else {
            commande.setPrixAssurance( PRIX_SANS_ASS );
        }

        if ( commande.getAccuseReception() ) {
            commande.setPrixAccuse( PRIX_ACC );
        } else {
            commande.setPrixAccuse( PRIX_SANS_ACC );
        }

        /*
         * gestion du statut du compte.
         */
        if ( commande.getStatutCompte() == 3 ) { // premium
            /*
             * Enlève le prix du poids en surplus
             */
            if ( commande.getPoids() > POIDS_BASE ) {
                if ( commande.getAdresse_pays_destinataire().equals( "Belgique" ) ) {
                    commande.setPrixBase( commande.getPrixBase() - SURPLUS_BEL );
                } else {
                    commande.setPrixBase( commande.getPrixBase() - SURPLUS_INT );
                }
            }
            /*
             * enlève le prix de l'assurance en surplus si il existe une
             * assurance moins cher
             */
            if ( PRIX_FORFAIT < Math.min( commande.getValeurEstimee() * PCT_MONTANT_HAUT,
                    commande.getValeurEstimee() * PCT_MONTANT_BAS + PRIX_MONTANT_BAS ) ) {
                commande.setTypeAssurance( "forfait" );
                commande.setPrixAssurance( PRIX_FORFAIT );
            } else {
                commande.setTypeAssurance( "montant" );
                commande.setPrixAssurance( Math.min( commande.getValeurEstimee() * PCT_MONTANT_HAUT,
                        commande.getValeurEstimee() * PCT_MONTANT_BAS + PRIX_MONTANT_BAS ) );
            }
        }

        String icu = resultSet.getString( "ICU" );
        commande.setIcu( icu );

        /*
         * Gestion du statut d'une commande
         */
        String statut = resultSet.getString( "statut" );
        switch ( Integer.parseInt( statut ) )
        {
        case -1:
            commande.setStatut( "En attente de prise en charge" );
            break;
        case 0:
            commande.setStatut( "Pick-up prévu" );
            break;
        case 1:
            commande.setStatut( "Collecté" );
            break;
        case 2:
            commande.setStatut( "En attente de dépot par le client au centre" );
            break;
        case 3:
            commande.setStatut( "En cours de transit" );
            break;
        case 4:
            commande.setStatut( "En attente de livraison" );
            break;
        case 5:
            commande.setStatut( "En attente de 2e livraison" );
            break;
        case 6:
            commande.setStatut( "Livré" );
            break;
        case 7:
            commande.setStatut( "Livré à un partenaire" );
            break;
        case 8:
            commande.setStatut( "Retour vers le client pour impossibilité de livraison" );
            break;
        case 9:
            commande.setStatut( "En attente au centre pour récupération par l'expéditeur" );
            break;
        case 10:
            commande.setStatut( "Détruit" );
            break;
        case 11:
            commande.setStatut( "Perdu" );
            break;
        case 12:
            commande.setStatut( "En attente au centre pour récupération par le destinataire" );
            break;
        default:
            commande.setStatut( "Statut inconnu" );
        }

        return commande;
    }

    /**
     * Méthode pour récupérer les données utiles pour lister les commandes par
     * l'employé
     * 
     * @param resultSet
     * @return Place les résultats d'une reqûete sql de sélection dans le bean
     *         de commande
     * @throws SQLException
     */
    private static Commande mapComEmpl( ResultSet resultSet, String pays ) throws SQLException {
        Commande commande = new Commande();
        commande.setClient( resultSet.getString( "mail" ) );
        commande.setIcu( resultSet.getString( "ICU" ) );
        commande.setDate_enregistrement( resultSet.getString( "date_enregistrement" ) );
        String statut = resultSet.getString( "statut" );
        switch ( Integer.parseInt( statut ) )
        {
        case -1:
            commande.setStatut( "En attente de prise en charge" );
            break;
        case 0:
            commande.setStatut( "Pick-up prévu" );
            break;
        case 1:
            commande.setStatut( "Collecté" );
            break;
        case 2:
            commande.setStatut( "En attente de dépot par le client au centre" );
            break;
        case 3:
            commande.setStatut( "En cours de transit" );
            break;
        case 4:
            commande.setStatut( "En attente de livraison" );
            break;
        case 5:
            commande.setStatut( "En attente de 2e livraison" );
            break;
        case 6:
            commande.setStatut( "Livré" );
            break;
        case 7:
            commande.setStatut( "Livré à un partenaire" );
            break;
        case 8:
            commande.setStatut( "Retour vers le client pour impossibilité de livraison" );
            break;
        case 9:
            commande.setStatut( "En attente au centre pour récupération par l'expéditeur" );
            break;
        case 10:
            commande.setStatut( "Détruit" );
            break;
        case 11:
            commande.setStatut( "Perdu" );
            break;
        case 12:
            commande.setStatut( "En attente au centre pour récupération par le destinataire" );
            break;
        default:
            commande.setStatut( "Statut inconnu" );
        }
        commande.setPrix( resultSet.getDouble( "prix" ) );
        if ( pays.equals( "bel" ) ) {
            commande.setPays( null );
        } else {
            commande.setPays( resultSet.getString( "pays" ) );
        }
        return commande;
    }

}
