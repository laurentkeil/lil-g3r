package com.mdl.dao;

import static com.mdl.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import com.mdl.beans.Surfacturation;

/**
 * @see SurfacturationDAO
 */
public class SurfacturationDAOImpl implements SurfacturationDAO {

    private static final String FICHIER_PROPERTIES       = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_SURPLUS_BEL     = "surplus_bel";
    private static final String PROPERTY_SURPLUS_INT     = "surplus_int";
    private static final String PROPERTY_POIDS_BASE      = "poids_base";
    private static final String PROPERTY_NB_JOUR_SURFACT = "nb_jour_surfact";

    private static DAOFactory   daoFactory;
    private static final String SQL_TROUVER              = "SELECT CLIENT.identifiant AS identifiant_cli, COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.poids_reel, COLIS.poids_renseigne, COMMANDE.prix, "
                                                                 + "SURFACTURATION.ref_colis_id, COLIS.icu, SURFACTURATION.date_paiement_effectif "
                                                                 + "FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT   "
                                                                 + "WHERE CLIENT.mail = ? AND COLIS.identifiant = SURFACTURATION.ref_colis_id AND COMMANDE.identifiant = COLIS.ref_commande_id AND CLIENT.identifiant = COMMANDE.ref_client_id "
                                                                 + "AND SURFACTURATION.date_paiement_effectif IS NULL AND ROWNUM <= 1 ORDER BY SURFACTURATION.date_notification ASC ";

    private static final String SQL_LISTER               = "SELECT CLIENT.identifiant AS identifiant_cli, COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix "
                                                                 + " FROM SURFACTURATION, COLIS , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                 + " AND CLIENT.mail = ? ORDER BY SURFACTURATION.date_paiement_effectif desc, SURFACTURATION.date_notification asc";

    private static final String SQL_LISTERPAYEES         = "SELECT CLIENT.identifiant AS identifiant_cli, COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix"
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                 + " AND CLIENT.mail = ?"
                                                                 + " AND SURFACTURATION.date_paiement_effectif IS NOT NULL ORDER BY SURFACTURATION.date_paiement_effectif desc, SURFACTURATION.date_notification asc";

    private static final String SQL_LISTERIMPAYEES       = "SELECT CLIENT.identifiant AS identifiant_cli, COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix"
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                 + " AND CLIENT.mail = ?"
                                                                 + " AND SURFACTURATION.date_paiement_effectif IS NULL ORDER BY SURFACTURATION.date_paiement_effectif desc, SURFACTURATION.date_notification asc";

    private static final String SQL_TROUVER_BYICU        = "SELECT CLIENT.identifiant AS identifiant_cli, COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix"
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                 + " AND COLIS.ICU = ?";

    private static final String SQL_UPDATE_PAYER         = "UPDATE surfacturation SET date_paiement_effectif = SYSDATE, difference_prix = ? WHERE ref_colis_id IN (SELECT identifiant from colis where COLIS.ICU = ?)";

    private static final String SQL_LISTER_ALL           = "SELECT CLIENT.identifiant AS identifiant_cli, CLIENT.nom, CLIENT.prenom, CLIENT.mail, COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix "
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id ORDER BY CLIENT.mail, SURFACTURATION.date_notification asc";

    private static final String SQL_LISTER_ALL_PAYEES    = "SELECT CLIENT.identifiant AS identifiant_cli, CLIENT.nom, CLIENT.prenom, CLIENT.mail,COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix"
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                 + " AND SURFACTURATION.date_paiement_effectif IS NOT NULL ORDER BY CLIENT.mail, SURFACTURATION.date_notification asc";

    private static final String SQL_LISTER_ALL_IMPAYEES  = "SELECT CLIENT.identifiant AS identifiant_cli, CLIENT.nom, CLIENT.prenom, CLIENT.mail, COMMANDE.adr_dest_pays, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix"
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id"
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id"
                                                                 + " AND SURFACTURATION.date_paiement_effectif IS NULL ORDER BY CLIENT.mail, SURFACTURATION.date_notification asc";

    private static final String SQL_SELECT_RECH_MAIL_I     = "SELECT CLIENT.identifiant AS identifiant_cli, COMMANDE.adr_dest_pays, CLIENT.nom, CLIENT.prenom, CLIENT.mail, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix "
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id AND CLIENT.mail = ? "
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id AND SURFACTURATION.date_paiement_effectif IS NULL ORDER BY SURFACTURATION.date_notification asc";

    private static final String SQL_SELECT_RECH_NOM_I      = "SELECT CLIENT.identifiant AS identifiant_cli,COMMANDE.adr_dest_pays, CLIENT.nom, CLIENT.prenom, CLIENT.mail, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix "
                                                                 + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
                                                                 + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
                                                                 + " AND COMMANDE.identifiant = COLIS.ref_commande_id AND CLIENT.nom = ? "
                                                                 + " AND CLIENT.identifiant = COMMANDE.ref_client_id AND SURFACTURATION.date_paiement_effectif IS NULL ORDER BY SURFACTURATION.date_notification asc";
    private static final String SQL_SELECT_RECH_MAIL_P   = "SELECT CLIENT.identifiant AS identifiant_cli, COMMANDE.adr_dest_pays, CLIENT.nom, CLIENT.prenom, CLIENT.mail, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix "
													            + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
													            + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
													            + " AND COMMANDE.identifiant = COLIS.ref_commande_id AND CLIENT.mail = ? "
													            + " AND CLIENT.identifiant = COMMANDE.ref_client_id AND SURFACTURATION.date_paiement_effectif IS NOT NULL ORDER BY SURFACTURATION.date_notification asc";

    private static final String SQL_SELECT_RECH_NOM_P      = "SELECT CLIENT.identifiant AS identifiant_cli,COMMANDE.adr_dest_pays, CLIENT.nom, CLIENT.prenom, CLIENT.mail, SURFACTURATION.identifiant AS identifiant_sur, SURFACTURATION.date_notification, SURFACTURATION.date_paiement_effectif, SURFACTURATION.poids_reel, SURFACTURATION.ref_colis_id, COLIS.icu, COLIS.poids_renseigne, COMMANDE.prix "
													            + " FROM SURFACTURATION  , COLIS  , COMMANDE  , CLIENT  "
													            + " WHERE COLIS.identifiant = SURFACTURATION.ref_colis_id"
													            + " AND COMMANDE.identifiant = COLIS.ref_commande_id AND CLIENT.nom = ? "
													            + " AND CLIENT.identifiant = COMMANDE.ref_client_id AND SURFACTURATION.date_paiement_effectif IS NOT NULL ORDER BY SURFACTURATION.date_notification asc";

    SurfacturationDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /**
     * @see SurfacturationDAO
     */
    public Surfacturation trouver( String mail ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Surfacturation surfacturation = null;
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_TROUVER, false, mail );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel résultset retourné */
            if ( resultSet.next() ) {
                surfacturation = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return surfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public List<Surfacturation> lister( String email ) throws DAOException {
        List<Surfacturation> listeSurfacturation = new ArrayList<Surfacturation>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTER, false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel résultset retourné */
            while ( resultSet.next() ) {
                listeSurfacturation.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listeSurfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public List<Surfacturation> listerPayees( String email ) throws DAOException {
        List<Surfacturation> listeSurfacturation = new ArrayList<Surfacturation>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTERPAYEES, false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel résultset retourné */
            while ( resultSet.next() ) {
                listeSurfacturation.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listeSurfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public List<Surfacturation> listerImpayees( String email ) throws DAOException {
        List<Surfacturation> listeSurfacturation = new ArrayList<Surfacturation>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTERIMPAYEES,
                    false,
                    email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel résultset retourné */
            while ( resultSet.next() ) {
                listeSurfacturation.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listeSurfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public Surfacturation trouverSurfactCommande( String icu ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Surfacturation surfacturation = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_TROUVER_BYICU, false,
                    icu );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données de l'éventuel résultset retourné */
            if ( resultSet.next() ) {
                surfacturation = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return surfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public void payer( String icu, String prix ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                    SQL_UPDATE_PAYER, false, Double.parseDouble( prix ), icu );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourn� par la requ�te d'insertion */
            if ( statut == 0 ) {
                throw new DAOException(
                        "Echec de la mise à jour de la surfacturation, aucune ligne modifiée dans la table." );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see SurfacturationDAO
     */
    public List<Surfacturation> listerAll() throws DAOException {
        List<Surfacturation> listeSurfacturation = new ArrayList<Surfacturation>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTER_ALL, false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel résultset retourné */
            while ( resultSet.next() ) {
                listeSurfacturation.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listeSurfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public List<Surfacturation> listerAllPayees() throws DAOException {
        List<Surfacturation> listeSurfacturation = new ArrayList<Surfacturation>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTER_ALL_PAYEES,
                    false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel résultset retourné */
            while ( resultSet.next() ) {
                listeSurfacturation.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listeSurfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public List<Surfacturation> listerAllImpayees() throws DAOException {
        List<Surfacturation> listeSurfacturation = new ArrayList<Surfacturation>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_LISTER_ALL_IMPAYEES,
                    false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours des lignes de données de l'éventuel résultset retourné */
            while ( resultSet.next() ) {
                listeSurfacturation.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listeSurfacturation;
    }

    /**
     * @see SurfacturationDAO
     */
    public List<Surfacturation> trouverSurfactRech( String rech, String research, String surfact) throws DAOException {
        List<Surfacturation> listeSurfacturation = new ArrayList<Surfacturation>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connexion = daoFactory.getConnection();
            if (surfact.equals("p")){
	            	 if ( research.equals( "1" ) ) {
	                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
	                        SQL_SELECT_RECH_MAIL_P, true, rech );
	                resultSet = preparedStatement.executeQuery();
	            } else {
	                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
	                        SQL_SELECT_RECH_NOM_P, true, rech );
	                resultSet = preparedStatement.executeQuery();
	            }
            }else{
            	if ( research.equals( "1" ) ) {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_MAIL_I, true, rech );
                    resultSet = preparedStatement.executeQuery();
                } else {
                    preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                            SQL_SELECT_RECH_NOM_I, true, rech );
                    resultSet = preparedStatement.executeQuery();
                }
            }
           
            /* Parcours des lignes de données de l'éventuel résultset retourné */
            while ( resultSet.next() ) {
                listeSurfacturation.add( mapAll( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listeSurfacturation;
    }

    /**
     * 
     * @param resultSet
     * @return renvoie un bean surfacturation contenant les informations
     *         récupérées de la base de données.
     * @throws SQLException
     */
    private static Surfacturation map( ResultSet resultSet ) throws SQLException {

        // Permet d'aller rechercher les informations dans le fichier de
        // configuration
        Properties properties = new Properties();
        String CHAMP_POIDS_BASE;
        String CHAMP_SURPLUS_BEL;
        String CHAMP_SURPLUS_INT;
        String CHAMP_NB_JOUR_SURFACT;

        double POIDS_BASE = 5;
        double SURPLUS_BEL = 10;
        double SURPLUS_INT = 15;
        int NB_JOUR_SURFACT = 15;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        try {
            properties.load( fichierProperties );
            CHAMP_POIDS_BASE = properties.getProperty( PROPERTY_POIDS_BASE );
            CHAMP_SURPLUS_BEL = properties.getProperty( PROPERTY_SURPLUS_BEL );
            CHAMP_SURPLUS_INT = properties.getProperty( PROPERTY_SURPLUS_INT );
            CHAMP_NB_JOUR_SURFACT = properties.getProperty( PROPERTY_NB_JOUR_SURFACT );

            POIDS_BASE = Double.parseDouble( CHAMP_POIDS_BASE );
            SURPLUS_BEL = Double.parseDouble( CHAMP_SURPLUS_BEL );
            SURPLUS_INT = Double.parseDouble( CHAMP_SURPLUS_INT );
            NB_JOUR_SURFACT = Integer.parseInt( CHAMP_NB_JOUR_SURFACT );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        Surfacturation surfacturation = new Surfacturation();
        surfacturation.setId( resultSet.getInt( "identifiant_sur" ) );
        surfacturation.setId_client( resultSet.getInt( "identifiant_cli" ) );
        surfacturation.setPoids_reel( resultSet.getDouble( "poids_reel" ) );
        surfacturation.setPoids_renseigne( resultSet.getDouble( "poids_renseigne" ) );
        surfacturation.setPrix_before( resultSet.getDouble( "prix" ) );
        surfacturation.setPays( resultSet.getString( "adr_dest_pays" ) );

        /**
         * Calcule le montant de la surfacturation s'il y en a une suivant la
         * différence de poids constaté par un coursier.
         */

        /**
         * Calcule le montant de la surfacturation s'il y en a une suivant la
         * différence de poids constaté par un coursier.
         */
        if ( surfacturation.getPoids_renseigne() <= POIDS_BASE && surfacturation.getPoids_reel() > POIDS_BASE ) {
            if ( surfacturation.getPays().equals( "Belgique" ) ) {
                surfacturation.setPrix_surfact( SURPLUS_BEL );
            } else {
                surfacturation.setPrix_surfact( SURPLUS_INT );
            }
        } else {
            surfacturation.setPrix_surfact( 0 );
        }
        surfacturation.setPrix_total( surfacturation.getPrix_before() + surfacturation.getPrix_surfact() );

        surfacturation.setColis( resultSet.getString( "ref_colis_id" ) );
        surfacturation.setICU( resultSet.getString( "icu" ) );
        surfacturation.setDate_paiement( (Date) resultSet.getObject( "date_paiement_effectif" ) );
        Date dateNotif = (Date) resultSet.getObject( "date_notification" );
        surfacturation.setDate_notification( dateNotif );

        /**
         * Jour restants avant blocage du compte.
         */
        if ( surfacturation.getDate_paiement() == null ) {
            int jour = dateNotif.getDate(); // jour de notif de 1 a
                                            // 31
            int mois = dateNotif.getMonth(); // mois de notif de 0 a
                                             // 11
            int annee = dateNotif.getYear() + 1900; // annee de notif

            Calendar maintenant = new GregorianCalendar(); // date d'aujourd'hui
            Calendar dateNot = new GregorianCalendar( annee, mois, jour ); // date
                                                                           // de
                                                                           // notif
            /*
             * Compte la durée de non paiement / mets à 0 si plus de 15 jours.
             */
            int duree = 0;
            if ( annee == maintenant.get( Calendar.YEAR ) ) {
                duree = maintenant.get( Calendar.DAY_OF_YEAR ) - dateNot.get( Calendar.DAY_OF_YEAR );
                if ( NB_JOUR_SURFACT - duree < 1 ) {
                    surfacturation.setJourRestant( 0 );
                } else {
                    surfacturation.setJourRestant( NB_JOUR_SURFACT - duree );
                }
            } else {
                surfacturation.setJourRestant( 0 );
            }

        }

        return surfacturation;
    }

    private static Surfacturation mapAll( ResultSet resultSet ) throws SQLException {

        // Permet d'aller rechercher les informations dans le fichier de
        // configuration
        Properties properties = new Properties();
        String CHAMP_POIDS_BASE;
        String CHAMP_SURPLUS_BEL;
        String CHAMP_SURPLUS_INT;
        String CHAMP_NB_JOUR_SURFACT;

        double POIDS_BASE = 5;
        double SURPLUS_BEL = 10;
        double SURPLUS_INT = 15;
        int NB_JOUR_SURFACT = 15;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        try {
            properties.load( fichierProperties );
            CHAMP_POIDS_BASE = properties.getProperty( PROPERTY_POIDS_BASE );
            CHAMP_SURPLUS_BEL = properties.getProperty( PROPERTY_SURPLUS_BEL );
            CHAMP_SURPLUS_INT = properties.getProperty( PROPERTY_SURPLUS_INT );
            CHAMP_NB_JOUR_SURFACT = properties.getProperty( PROPERTY_NB_JOUR_SURFACT );

            POIDS_BASE = Double.parseDouble( CHAMP_POIDS_BASE );
            SURPLUS_BEL = Double.parseDouble( CHAMP_SURPLUS_BEL );
            SURPLUS_INT = Double.parseDouble( CHAMP_SURPLUS_INT );
            NB_JOUR_SURFACT = Integer.parseInt( CHAMP_NB_JOUR_SURFACT );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        Surfacturation surfacturation = new Surfacturation();
        surfacturation.setId( resultSet.getInt( "identifiant_sur" ) );
        surfacturation.setId_client( resultSet.getInt( "identifiant_cli" ) );
        surfacturation.setMail( resultSet.getString( "mail" ) );
        surfacturation.setPrenom( resultSet.getString( "prenom" ) );
        surfacturation.setNom( resultSet.getString( "nom" ) );
        surfacturation.setPays( resultSet.getString( "adr_dest_pays" ) );
        surfacturation.setPoids_reel( resultSet.getDouble( "poids_reel" ) );
        surfacturation.setPoids_renseigne( resultSet.getDouble( "poids_renseigne" ) );
        surfacturation.setPrix_before( resultSet.getDouble( "prix" ) );

        /**
         * Calcule le montant de la surfacturation s'il y en a une suivant la
         * différence de poids constaté par un coursier.
         */
        if ( surfacturation.getPoids_renseigne() <= POIDS_BASE && surfacturation.getPoids_reel() > POIDS_BASE ) {
            if ( surfacturation.getPays().equals( "Belgique" ) ) {
                surfacturation.setPrix_surfact( SURPLUS_BEL );
            } else {
                surfacturation.setPrix_surfact( SURPLUS_INT );
            }
        } else {
            surfacturation.setPrix_surfact( 0 );
        }
        surfacturation.setPrix_total( surfacturation.getPrix_before() + surfacturation.getPrix_surfact() );

        surfacturation.setColis( resultSet.getString( "ref_colis_id" ) );
        surfacturation.setICU( resultSet.getString( "icu" ) );
        surfacturation.setDate_paiement( (Date) resultSet.getObject( "date_paiement_effectif" ) );
        Date dateNotif = (Date) resultSet.getObject( "date_notification" );
        surfacturation.setDate_notification( dateNotif );

        /**
         * Jour restants avant blocage du compte.
         */
        if ( surfacturation.getDate_paiement() == null ) {
            int jour = dateNotif.getDate(); // jour de notif de 1 a
                                            // 31
            int mois = dateNotif.getMonth(); // mois de notif de 0 a
                                             // 11
            int annee = dateNotif.getYear() + 1900; // annee de notif

            Calendar maintenant = new GregorianCalendar(); // date d'aujourd'hui
            Calendar dateNot = new GregorianCalendar( annee, mois, jour ); // date
                                                                           // de
                                                                           // notif
            /*
             * Compte la durée de non paiement / mets à 0 si plus de 15 jours.
             */
            int duree = 0;
            if ( annee == maintenant.get( Calendar.YEAR ) ) {
                duree = maintenant.get( Calendar.DAY_OF_YEAR ) - dateNot.get( Calendar.DAY_OF_YEAR );
                if ( NB_JOUR_SURFACT - duree < 1 ) {
                    surfacturation.setJourRestant( 0 );
                } else {
                    surfacturation.setJourRestant( NB_JOUR_SURFACT - duree );
                }
            } else {
                surfacturation.setJourRestant( 0 );
            }

        }

        return surfacturation;
    }
}
