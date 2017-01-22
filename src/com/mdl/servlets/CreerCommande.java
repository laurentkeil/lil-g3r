package com.mdl.servlets;

import hlbexpress.routing.facade.RoutingCommande;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Commande;
import com.mdl.beans.Statut_client;
import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.SurfacturationDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.CommandeForm;

/**
 * Servlet de création de commande. Affichage d'un formulaire et récupération
 * des informations encodées par l'utilisateur.
 * 
 */
public class CreerCommande extends HttpServlet {
    /**
     * déclaration des constantes
     */
    public static final String  ATT_DATEAUJ                  = "dateAuj";
    public static final String  ATT_COMMANDE                 = "commande";
    public static final String  ATT_CARNET                   = "carnet_adresses";
    public static final String  ATT_TAILLE                   = "taille";
    public static final String  ATT_NOM                      = "nom";
    public static final String  ATT_PRENOM                   = "prenom";
    public static final String  ATT_RUE                      = "rue";
    public static final String  ATT_NUM                      = "num";
    public static final String  ATT_BOITE                    = "boite";
    public static final String  ATT_CODE                     = "code";
    public static final String  ATT_LOC                      = "loc";
    public static final String  ATT_PAYS                     = "pays";
    public static final String  ATT_TEL                      = "tel";
    public static final String  ATT_LISTEPAYS                = "listePays";

    /*
     * constantes pour les attributs de requetes correspondant à l'utilisation
     * du javascript
     */
    private static final String ATT_FICHIER                  = "fichier";
    private static final String ATT_URL                      = "url";
    private static final String ATT_VALEUR_MONTANT           = "valeur_montant";
    public static final String  ATT_PRIX_BASE_BELGIQUE_BAS   = "prix_base_bel_bas";
    public static final String  ATT_PRIX_BASE_INT_HAUT       = "prix_base_int_haut";
    private static final String ATT_PRIX_BASE_AUTRE          = "prix_base_autre";
    public static final String  ATT_PRIX_FORFAIT             = "prix_forfait";
    public static final String  ATT_PRIX_MONTANT_BAS         = "prix_montant_bas";
    public static final String  ATT_PCT_MONTANT_BAS          = "pct_montant_bas";
    public static final String  ATT_PCT_MONTANT_HAUT         = "pct_montant_haut";
    public static final String  ATT_POIDS_BASE               = "poids_base";
    private static final String ATT_PRIX_ACC                 = "prix_acc";

    public static final String  ATT_SESSION_COMMANDE         = "sessionCommande";
    public static final String  ATT_FREE                     = "free";
    public static final String  PRIX_BEFORE                  = "prixbefore";
    public static final String  ATT_FORM                     = "form";
    public static final String  ATT_SESSION_USER             = "sessionUtilisateur";
    public static final String  CONF_DAO_FACTORY             = "daofactory";
    public static final String  ACCES_CONNEXION              = "/ConnexionClient";
    public static final String  ATT_SESSION_EMPL             = "sessionEmploye";
    public static final String  ACCES_CONNECTE_EMPL          = "/AccueilEmploye";

    private static final String FICHIER_PROPERTIES           = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_AVANCE_MIN          = "avance_min";
    private static final String PROPERTY_PREMIUM_MIN         = "premium_min";
    private static final String PROPERTY_SURFACT_AVANCE      = "surfact_avance";
    private static final String PROPERTY_SURFACT_PREMIUM     = "surfact_premium";
    private static final String PROPERTY_LITIGE_TORT_AVANCE  = "litige_tort_avance";
    private static final String PROPERTY_LITIGE_TORT_PREMIUM = "litige_tort_premium";

    private static final String PROPERTY_FICHIER             = "fichier";
    private static final String PROPERTY_URL                 = "url";
    private static final String PROPERTY_PRIX_BASE_AUTRE     = "prix_autre";
    private static final String PROPERTY_PRIX_BEL_BAS        = "prix_bel_bas";
    private static final String PROPERTY_PRIX_INT_HAUT       = "prix_int_haut";
    private static final String PROPERTY_SURPLUS_BEL         = "surplus_bel";
    private static final String PROPERTY_SURPLUS_INT         = "surplus_int";
    private static final String PROPERTY_PRIX_FORFAIT        = "prix_forfait";
    private static final String PROPERTY_PRIX_MONTANT_BAS    = "prix_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_BAS     = "pct_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_HAUT    = "pct_montant_haut";
    private static final String PROPERTY_POIDS_BASE          = "poids_base";
    private static final String PROPERTY_PRIX_ACC            = "prix_acc";
    private static final String PROPERTY_COLIS_COM           = "nb_colis_com";
    private static final String PROPERTY_NB_JOUR_SURFACT     = "nb_jour_surfact";
    private static final String PROPERTY_NB_LITIGE_TORT      = "nb_litige_tort";
    private static final String PROPERTY_MONTANT_MAX_REMB    = "montant_max_remb";
    private static final String PROPERTY_VALEUR_MONTANT      = "valeur_montant";

    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String  VUE_FORM                     = "/WEB-INF/pagesJSP/client/passageCommande.jsp";
    public static final String  VUE_SUCCES                   = "/WEB-INF/pagesJSP/client/confirmerCommande.jsp";
    public static final String  VUE_BLOCAGE                  = "/WEB-INF/pagesJSP/client/afficherBlocage.jsp";

    private UtilisateurDAO      utilisateurDao;
    private CommandeDAO         commandeDao;
    private SurfacturationDAO   surfacturationDao;
    private StatutDAO           statutDao;

    /**
     * initialisation de l'utilisateur et ses informations en base de donnée +
     * la commande
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Commande */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        /* Récupération d'une instance de notre DAO surfacturation */
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getSurfacturationDao();
        /* Récupération d'une instance de notre DAO statut */
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getStatutDao();
    }

    /**
     * affichage du formulaire, de la page jsp
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();

        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté et donc on redirige l'utilisateur
         * vers la page de connexion, sinon on affiche le formulaire de création
         * de commande.
         */
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            String email = (String) session.getAttribute( ATT_SESSION_USER );

            /**
             * acces en bd a la liste de surfacturation (payees impayées ou
             * toutes suivant les informations provenant du client)
             */
            List<Surfacturation> surfact = null;
            surfact = surfacturationDao.listerImpayees( email );
            /**
             * Itère sur la liste des surfacturations pour vérifier si il y en a
             * des non-payées depuis + de 15 jours après leur notification. Si
             * oui, blocage du compte.
             */
            Iterator j = surfact.iterator();
            boolean continuer = true;
            while ( j.hasNext() && continuer ) {
                Surfacturation surf = (Surfacturation) j.next();
                if ( surf.getJourRestant() == 0 && surf.getDate_paiement() == null ) {
                    continuer = false;
                    statutDao.bloquerCompte( surf.getId_client(),
                            "surfacturation" );
                }
            }

            /**
             * Récupération du statut du client et vérification que son compte
             * n'est pas bloqué.
             */
            int id_client = utilisateurDao.trouver( email ).getId();
            Statut_client statut = statutDao.trouver( id_client );

            /*
             * Si oui, redirection vers une vue affichant son blocage.
             */
            if ( statut.getStatut().equals( "0" ) ) {
                // Permet d'aller rechercher les informations dans le fichier de
                // configuration
                Properties properties = new Properties();
                String CHAMP_NB_JOUR_SURFACT;
                String CHAMP_NB_LITIGE_TORT;

                double NB_JOUR_SURFACT;
                double NB_LITIGE_TORT;

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

                if ( fichierProperties == null ) {
                    throw new ServletException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
                }

                try {
                    properties.load( fichierProperties );
                    CHAMP_NB_JOUR_SURFACT = properties.getProperty( PROPERTY_NB_JOUR_SURFACT );
                    CHAMP_NB_LITIGE_TORT = properties.getProperty( PROPERTY_NB_LITIGE_TORT );

                    NB_JOUR_SURFACT = Double.parseDouble( CHAMP_NB_JOUR_SURFACT );
                    NB_LITIGE_TORT = Double.parseDouble( CHAMP_NB_LITIGE_TORT );

                    if ( NB_JOUR_SURFACT < 0 )
                        NB_JOUR_SURFACT = 0;
                    if ( NB_LITIGE_TORT < 0 )
                        NB_LITIGE_TORT = 0;
                } catch ( IOException e ) {
                    throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
                }
                if ( statut.getCause().equals( "surfacturation" ) ) {
                    request.setAttribute( "cause", "Surfacturation impayée endéans les " + NB_JOUR_SURFACT
                            + " jours imposés." );
                } else {
                    request.setAttribute( "cause",
                            "Au moins " + NB_LITIGE_TORT
                                    + " litiges ont été considérés comme étant de votre responsabilité." );
                }
                this.getServletContext().getRequestDispatcher( VUE_BLOCAGE ).forward( request, response );

            } else {
                // Permet d'aller rechercher les informations dans le fichier de
                // configuration
                Properties properties = new Properties();
                String CHAMP_AVANCE_MIN;
                String CHAMP_PREMIUM_MIN;
                String CHAMP_SURFACT_AVANCE;
                String CHAMP_SURFACT_PREMIUM;
                String CHAMP_LITIGE_TORT_AVANCE;
                String CHAMP_LITIGE_TORT_PREMIUM;

                String CHAMP_PRIX_BASE_BELGIQUE_BAS;
                String CHAMP_PRIX_BASE_INT_HAUT;
                String CHAMP_PRIX_AUTRE;
                String CHAMP_PRIX_FORFAIT;
                String CHAMP_PRIX_MONTANT_BAS;
                String CHAMP_PCT_MONTANT_BAS;
                String CHAMP_PCT_MONTANT_HAUT;
                String CHAMP_POIDS_BASE;
                String CHAMP_PRIX_ACC;
                String CHAMP_VALEUR_MONTANT;

                double AVANCE_MIN;
                double PREMIUM_MIN;
                double SURFACT_AVANCE;
                double SURFACT_PREMIUM;
                double LITIGE_TORT_AVANCE;
                double LITIGE_TORT_PREMIUM;

                double PRIX_BASE_BELGIQUE_BAS;
                double PRIX_BASE_INT_HAUT;
                double PRIX_BASE_AUTRE;
                double PRIX_FORFAIT;
                double PRIX_MONTANT_BAS;
                double PCT_MONTANT_BAS;
                double PCT_MONTANT_HAUT;
                double POIDS_BASE;
                double PRIX_ACC;
                double VALEUR_MONTANT;

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

                if ( fichierProperties == null ) {
                    throw new ServletException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
                }

                try {
                    properties.load( fichierProperties );
                    CHAMP_AVANCE_MIN = properties.getProperty( PROPERTY_AVANCE_MIN );
                    CHAMP_PREMIUM_MIN = properties.getProperty( PROPERTY_PREMIUM_MIN );
                    CHAMP_SURFACT_AVANCE = properties.getProperty( PROPERTY_SURFACT_AVANCE );
                    CHAMP_SURFACT_PREMIUM = properties.getProperty( PROPERTY_SURFACT_PREMIUM );
                    CHAMP_LITIGE_TORT_AVANCE = properties.getProperty( PROPERTY_LITIGE_TORT_AVANCE );
                    CHAMP_LITIGE_TORT_PREMIUM = properties.getProperty( PROPERTY_LITIGE_TORT_PREMIUM );

                    CHAMP_PRIX_BASE_BELGIQUE_BAS = properties.getProperty( PROPERTY_PRIX_BEL_BAS );
                    CHAMP_PRIX_BASE_INT_HAUT = properties.getProperty( PROPERTY_PRIX_INT_HAUT );
                    CHAMP_PRIX_AUTRE = properties.getProperty( PROPERTY_PRIX_BASE_AUTRE );
                    CHAMP_PRIX_FORFAIT = properties.getProperty( PROPERTY_PRIX_FORFAIT );
                    CHAMP_PRIX_MONTANT_BAS = properties.getProperty( PROPERTY_PRIX_MONTANT_BAS );
                    CHAMP_PCT_MONTANT_BAS = properties.getProperty( PROPERTY_PCT_MONTANT_BAS );
                    CHAMP_PCT_MONTANT_HAUT = properties.getProperty( PROPERTY_PCT_MONTANT_HAUT );
                    CHAMP_POIDS_BASE = properties.getProperty( PROPERTY_POIDS_BASE );
                    CHAMP_PRIX_ACC = properties.getProperty( PROPERTY_PRIX_ACC );
                    CHAMP_VALEUR_MONTANT = properties.getProperty( PROPERTY_VALEUR_MONTANT );

                    AVANCE_MIN = Double.parseDouble( CHAMP_AVANCE_MIN );
                    PREMIUM_MIN = Double.parseDouble( CHAMP_PREMIUM_MIN );
                    SURFACT_AVANCE = Double.parseDouble( CHAMP_SURFACT_AVANCE );
                    SURFACT_PREMIUM = Double.parseDouble( CHAMP_SURFACT_PREMIUM );
                    LITIGE_TORT_AVANCE = Double.parseDouble( CHAMP_LITIGE_TORT_AVANCE );
                    LITIGE_TORT_PREMIUM = Double.parseDouble( CHAMP_LITIGE_TORT_PREMIUM );

                    PRIX_BASE_BELGIQUE_BAS = Double.parseDouble( CHAMP_PRIX_BASE_BELGIQUE_BAS );
                    PRIX_BASE_INT_HAUT = Double.parseDouble( CHAMP_PRIX_BASE_INT_HAUT );
                    PRIX_BASE_AUTRE = Double.parseDouble( CHAMP_PRIX_AUTRE );
                    PRIX_FORFAIT = Double.parseDouble( CHAMP_PRIX_FORFAIT );
                    PRIX_MONTANT_BAS = Double.parseDouble( CHAMP_PRIX_MONTANT_BAS );
                    PCT_MONTANT_BAS = Double.parseDouble( CHAMP_PCT_MONTANT_BAS );
                    PCT_MONTANT_HAUT = Double.parseDouble( CHAMP_PCT_MONTANT_HAUT );
                    POIDS_BASE = Double.parseDouble( CHAMP_POIDS_BASE );
                    PRIX_ACC = Double.parseDouble( CHAMP_PRIX_ACC );
                    VALEUR_MONTANT = Double.parseDouble( CHAMP_VALEUR_MONTANT );

                    /*
                     * Placement en attribut de la requete des propriétés de
                     * configuration pour les utiliser en javascript
                     */
                    request.setAttribute( ATT_PRIX_BASE_BELGIQUE_BAS, PRIX_BASE_BELGIQUE_BAS );
                    request.setAttribute( ATT_PRIX_BASE_INT_HAUT, PRIX_BASE_INT_HAUT );
                    request.setAttribute( ATT_PRIX_BASE_AUTRE, PRIX_BASE_AUTRE );
                    request.setAttribute( ATT_PRIX_FORFAIT, PRIX_FORFAIT );
                    request.setAttribute( ATT_PRIX_MONTANT_BAS, PRIX_MONTANT_BAS );
                    request.setAttribute( ATT_PCT_MONTANT_BAS, PCT_MONTANT_BAS );
                    request.setAttribute( ATT_PCT_MONTANT_HAUT, PCT_MONTANT_HAUT );
                    request.setAttribute( ATT_POIDS_BASE, POIDS_BASE );
                    request.setAttribute( ATT_PRIX_ACC, PRIX_ACC );
                    request.setAttribute( ATT_VALEUR_MONTANT, VALEUR_MONTANT );

                    if ( SURFACT_AVANCE < 0 )
                        SURFACT_AVANCE = 0;
                    if ( SURFACT_PREMIUM < 0 )
                        SURFACT_PREMIUM = 0;
                    if ( LITIGE_TORT_AVANCE < 0 )
                        LITIGE_TORT_AVANCE = 0;
                    if ( LITIGE_TORT_PREMIUM < 0 )
                        LITIGE_TORT_PREMIUM = 0;
                } catch ( IOException e ) {
                    throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
                }

                int nbSurfactImpayees = commandeDao.nbSurfactImpayees( email );
                int chiffreAffaire = commandeDao.chiffreAffaire( email );
                int nbLitigesTord = commandeDao.nbLitigesTort( email );

                /*
                 * gestion du statut du compte
                 */
                if ( chiffreAffaire <= PREMIUM_MIN && chiffreAffaire >= AVANCE_MIN
                        && nbSurfactImpayees <= SURFACT_AVANCE
                        && nbLitigesTord <= LITIGE_TORT_AVANCE ) {
                    statutDao.setStatut( id_client, 2 ); // avancé
                } else if ( chiffreAffaire > PREMIUM_MIN && nbSurfactImpayees <= SURFACT_PREMIUM
                        && nbLitigesTord <= LITIGE_TORT_PREMIUM ) {
                    statutDao.setStatut( id_client, 3 ); // premium
                } else {
                    statutDao.setStatut( id_client, 1 ); // normal
                }

                statut = statutDao.trouver( id_client );
                /*
                 * récupération du carnet d'adresse en BD
                 */
                List<List<String>> carnet_adresse = commandeDao.listerCarnet( email );

                /*
                 * Création des listes des champs d'une adresse du carnet
                 * d'adresses afin de les récupérer dans la jsp
                 */
                List<String> nom = new ArrayList<String>();
                List<String> prenom = new ArrayList<String>();
                List<String> rue = new ArrayList<String>();
                List<String> num = new ArrayList<String>();
                List<String> boite = new ArrayList<String>();
                List<String> code = new ArrayList<String>();
                List<String> loc = new ArrayList<String>();
                List<String> pays = new ArrayList<String>();
                List<String> tel = new ArrayList<String>();
                /*
                 * itération sur le carnet d'adresses et mise en place des
                 * listes de champs des adresses.
                 */
                Iterator i = carnet_adresse.iterator();
                int taille = 0;
                while ( i.hasNext() ) {
                    List<String> adresse = (List<String>) i.next();
                    Iterator y = adresse.iterator();
                    nom.add( (String) y.next() );
                    prenom.add( (String) y.next() );
                    rue.add( (String) y.next() );
                    num.add( (String) y.next() );
                    boite.add( (String) y.next() );
                    code.add( (String) y.next() );
                    loc.add( (String) y.next() );
                    pays.add( (String) y.next() );
                    tel.add( (String) y.next() );
                    taille = taille + 1;
                }
                /*
                 * mise en place des attributs dans la requête de la page
                 */
                request.setAttribute( ATT_CARNET, carnet_adresse );
                request.setAttribute( ATT_NOM, nom );
                request.setAttribute( ATT_PRENOM, prenom );
                request.setAttribute( ATT_RUE, rue );
                request.setAttribute( ATT_NUM, num );
                request.setAttribute( ATT_BOITE, boite );
                request.setAttribute( ATT_CODE, code );
                request.setAttribute( ATT_LOC, loc );
                request.setAttribute( ATT_PAYS, pays );
                request.setAttribute( ATT_TEL, tel );
                request.setAttribute( ATT_TAILLE, taille );

                /*
                 * Construit l'algo de routage avec le WS
                 */
                RoutingCommande routing = new RoutingCommande();
                /*
                 * Récupère la liste des pays d'Europe du WS
                 */
                List<String> listePays = routing.getListPays();
                request.setAttribute( ATT_LISTEPAYS, listePays );

                /*
                 * A la réception d'une requête GET, simple affichage du
                 * formulaire
                 */
                this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
            }

        }

    }

    /**
     * affichage du formulaire de création de commande et récupération des
     * informations encodés par l'utilisateur et redirection sur des vues
     * suivant le résultat de la création de la commande.
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        request.setCharacterEncoding( "UTF-8" );
        HttpSession session = request.getSession();

        /* Préparation de l'objet formulaire */
        CommandeForm form = new CommandeForm( utilisateurDao );

        String email = (String) session.getAttribute( ATT_SESSION_USER );
        Utilisateur utilisateur = utilisateurDao.trouver( email );

        /* Traitement de la requête et récupération du bean en résultant */
        Commande commande = form.creerCommande( request, session );

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_COMMANDE, commande );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            Properties properties = new Properties();

            String CHAMP_FICHIER;
            String CHAMP_URL;
            String CHAMP_PRIX_BASE_BELGIQUE_BAS;
            String CHAMP_PRIX_BASE_INT_HAUT;
            String CHAMP_PRIX_AUTRE;
            String CHAMP_PRIX_FORFAIT;
            String CHAMP_PRIX_MONTANT_BAS;
            String CHAMP_PCT_MONTANT_BAS;
            String CHAMP_PCT_MONTANT_HAUT;
            String CHAMP_POIDS_BASE;
            String CHAMP_PRIX_ACC;
            String CHAMP_SURPLUS_BEL;
            String CHAMP_SURPLUS_INT;
            String CHAMP_COLIS_COM;
            String CHAMP_MONTANT_MAX_REMB;
            String CHAMP_VALEUR_MONTANT;

            double PRIX_BASE_BELGIQUE_BAS;
            double PRIX_BASE_INT_HAUT;
            double PRIX_BASE_AUTRE;
            double PRIX_FORFAIT;
            double PRIX_MONTANT_BAS;
            double PCT_MONTANT_BAS;
            double PCT_MONTANT_HAUT;
            double POIDS_BASE;
            double PRIX_ACC;
            double COLIS_COM;
            double SURPLUS_BEL;
            double SURPLUS_INT;
            double MONTANT_MAX_REMB;
            double VALEUR_MONTANT;

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

            if ( fichierProperties == null ) {
                throw new ServletException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
            }

            try {
                properties.load( fichierProperties );
                CHAMP_FICHIER = properties.getProperty( PROPERTY_FICHIER );
                CHAMP_URL = properties.getProperty( PROPERTY_URL );
                CHAMP_PRIX_BASE_BELGIQUE_BAS = properties.getProperty( PROPERTY_PRIX_BEL_BAS );
                CHAMP_PRIX_BASE_INT_HAUT = properties.getProperty( PROPERTY_PRIX_INT_HAUT );
                CHAMP_PRIX_AUTRE = properties.getProperty( PROPERTY_PRIX_BASE_AUTRE );
                CHAMP_PRIX_FORFAIT = properties.getProperty( PROPERTY_PRIX_FORFAIT );
                CHAMP_PRIX_MONTANT_BAS = properties.getProperty( PROPERTY_PRIX_MONTANT_BAS );
                CHAMP_PCT_MONTANT_BAS = properties.getProperty( PROPERTY_PCT_MONTANT_BAS );
                CHAMP_PCT_MONTANT_HAUT = properties.getProperty( PROPERTY_PCT_MONTANT_HAUT );
                CHAMP_POIDS_BASE = properties.getProperty( PROPERTY_POIDS_BASE );
                CHAMP_PRIX_ACC = properties.getProperty( PROPERTY_PRIX_ACC );
                CHAMP_SURPLUS_BEL = properties.getProperty( PROPERTY_SURPLUS_BEL );
                CHAMP_SURPLUS_INT = properties.getProperty( PROPERTY_SURPLUS_INT );
                CHAMP_COLIS_COM = properties.getProperty( PROPERTY_COLIS_COM );
                CHAMP_MONTANT_MAX_REMB = properties.getProperty( PROPERTY_MONTANT_MAX_REMB );
                CHAMP_VALEUR_MONTANT = properties.getProperty( PROPERTY_VALEUR_MONTANT );

                PRIX_BASE_BELGIQUE_BAS = Double.parseDouble( CHAMP_PRIX_BASE_BELGIQUE_BAS );
                PRIX_BASE_INT_HAUT = Double.parseDouble( CHAMP_PRIX_BASE_INT_HAUT );
                PRIX_BASE_AUTRE = Double.parseDouble( CHAMP_PRIX_AUTRE );
                PRIX_FORFAIT = Double.parseDouble( CHAMP_PRIX_FORFAIT );
                PRIX_MONTANT_BAS = Double.parseDouble( CHAMP_PRIX_MONTANT_BAS );
                PCT_MONTANT_BAS = Double.parseDouble( CHAMP_PCT_MONTANT_BAS );
                PCT_MONTANT_HAUT = Double.parseDouble( CHAMP_PCT_MONTANT_HAUT );
                POIDS_BASE = Double.parseDouble( CHAMP_POIDS_BASE );
                PRIX_ACC = Double.parseDouble( CHAMP_PRIX_ACC );
                SURPLUS_BEL = Double.parseDouble( CHAMP_SURPLUS_BEL );
                SURPLUS_INT = Double.parseDouble( CHAMP_SURPLUS_INT );
                COLIS_COM = Double.parseDouble( CHAMP_COLIS_COM );
                MONTANT_MAX_REMB = Double.parseDouble( CHAMP_MONTANT_MAX_REMB );
                VALEUR_MONTANT = Double.parseDouble( CHAMP_VALEUR_MONTANT );

                /*
                 * Placement en attribut de la requete des propriétés de
                 * configuration pour les utiliser en javascript
                 */
                request.setAttribute( ATT_FICHIER, CHAMP_FICHIER );
                request.setAttribute( ATT_URL, CHAMP_URL );
                request.setAttribute( ATT_FICHIER, CHAMP_FICHIER );
                request.setAttribute( ATT_URL, CHAMP_URL );
                request.setAttribute( ATT_PRIX_BASE_BELGIQUE_BAS, PRIX_BASE_BELGIQUE_BAS );
                request.setAttribute( ATT_PRIX_BASE_INT_HAUT, PRIX_BASE_INT_HAUT );
                request.setAttribute( ATT_PRIX_BASE_AUTRE, PRIX_BASE_AUTRE );
                request.setAttribute( ATT_PRIX_FORFAIT, PRIX_FORFAIT );
                request.setAttribute( ATT_PRIX_MONTANT_BAS, PRIX_MONTANT_BAS );
                request.setAttribute( ATT_PCT_MONTANT_BAS, PCT_MONTANT_BAS );
                request.setAttribute( ATT_PCT_MONTANT_HAUT, PCT_MONTANT_HAUT );
                request.setAttribute( ATT_POIDS_BASE, POIDS_BASE );
                request.setAttribute( ATT_PRIX_ACC, PRIX_ACC );
                request.setAttribute( ATT_VALEUR_MONTANT, VALEUR_MONTANT );

            } catch ( IOException e ) {
                throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
            }

            Statut_client statut = statutDao.trouver( utilisateur.getId() );
            /**
             * gestion du statut du compte.
             */
            if ( statut.getStatut().equals( "2" ) ) { // avancé
                commande.setStatutCompte( 2 );
                /*
                 * envoi gratuit tous les 12 envois pour le compte avancé.
                 * recherche l'id du dernier colis rendu gratuit, return 0 si il
                 * n'existe pas
                 */
                int idLastColisFree = commandeDao.getIdLastColis( email );
                int nbComPassee = 0;
                if ( idLastColisFree == 0 ) {
                    nbComPassee = commandeDao.nbComForFree( email );
                } else {
                    nbComPassee = commandeDao.nbComForFree( email, idLastColisFree );
                }
                if ( nbComPassee >= ( COLIS_COM - 1 ) ) { // La commande est
                                                          // rendu gratuite.
                    commande.setPrix( (double) 0 );
                    request.setAttribute( ATT_FREE, "HLB-Express vous fait bénéficier d'une commande gratuite." );
                }
            } else if ( statut.getStatut().equals( "3" ) ) { // premium
                commande.setStatutCompte( 3 );
                request.setAttribute( PRIX_BEFORE, commande.getPrix() );
                /*
                 * Enlève le prix du poids en surplus
                 */
                if ( commande.getPoids() > POIDS_BASE ) {
                    if ( commande.getAdresse_pays_destinataire().equals( "Belgique" ) ) {
                        commande.setPrixBase( commande.getPrixBase() - 10 );
                        commande.setPrix( commande.getPrix() - SURPLUS_BEL );
                    } else {
                        commande.setPrixBase( commande.getPrixBase() - SURPLUS_INT );
                        commande.setPrix( commande.getPrix() - SURPLUS_INT );
                    }
                }
                /*
                 * enlève le prix de l'assurance en surplus si il existe une
                 * assurance moins cher
                 */
                commande.setPrix( commande.getPrix() - commande.getPrixAssurance() );
                if ( !commande.getTypeAssurance().equals( "aucune" ) ) {

                    if ( commande.getValeurEstimee() > MONTANT_MAX_REMB ) {
                        /*
                         * si c'est plus de 300 €, il peut choisir entre risquer
                         * un remboursement moindre et payer moins ou etre
                         * rembourser full mais payer plus
                         */
                        if ( commande.getTypeAssurance().equals( "forfait" ) ) {
                            commande.setTypeAssurance( "forfait" );
                            commande.setPrixAssurance( PRIX_FORFAIT );
                        } else {
                            commande.setTypeAssurance( "montant" );
                            commande.setPrixAssurance( Math.min( commande.getValeurEstimee() * PCT_MONTANT_HAUT,
                                    commande.getValeurEstimee() * PCT_MONTANT_BAS + PRIX_MONTANT_BAS ) );
                        }
                    } else { /*
                              * si c'est moins de 300€, on prend d'office la
                              * meilleur formule
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
                    commande.setPrix( commande.getPrix() + commande.getPrixAssurance() );
                }
            } else {
                commande.setStatutCompte( 1 );
            }

            session.setAttribute( ATT_SESSION_COMMANDE, commande );
            /*
             * Si aucune erreur, alors affichage de la fiche récapitulative pour
             * confirmer la commande
             */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /**
             * récupération du carnet d'adresse en BD
             */
            List<List<String>> carnet_adresse = commandeDao.listerCarnet( email );

            /*
             * Création des listes des champs d'une adresse du carnet d'adresses
             * afin de les récupérer dans la jsp
             */
            List<String> nom = new ArrayList<String>();
            List<String> prenom = new ArrayList<String>();
            List<String> rue = new ArrayList<String>();
            List<String> num = new ArrayList<String>();
            List<String> boite = new ArrayList<String>();
            List<String> code = new ArrayList<String>();
            List<String> loc = new ArrayList<String>();
            List<String> pays = new ArrayList<String>();
            List<String> tel = new ArrayList<String>();
            /*
             * itération sur le carnet d'adresses et mise en place des listes de
             * champs des adresses.
             */
            Iterator i = carnet_adresse.iterator();
            int taille = 0;
            while ( i.hasNext() ) {
                List<String> adresse = (List<String>) i.next();
                Iterator y = adresse.iterator();
                nom.add( (String) y.next() );
                prenom.add( (String) y.next() );
                rue.add( (String) y.next() );
                num.add( (String) y.next() );
                boite.add( (String) y.next() );
                code.add( (String) y.next() );
                loc.add( (String) y.next() );
                pays.add( (String) y.next() );
                tel.add( (String) y.next() );
                taille = taille + 1;
            }
            /**
             * mise en place des attributs dans la requête de la page
             */
            request.setAttribute( ATT_CARNET, carnet_adresse );
            request.setAttribute( ATT_NOM, nom );
            request.setAttribute( ATT_PRENOM, prenom );
            request.setAttribute( ATT_RUE, rue );
            request.setAttribute( ATT_NUM, num );
            request.setAttribute( ATT_BOITE, boite );
            request.setAttribute( ATT_CODE, code );
            request.setAttribute( ATT_LOC, loc );
            request.setAttribute( ATT_PAYS, pays );
            request.setAttribute( ATT_TEL, tel );
            request.setAttribute( ATT_TAILLE, taille );

            /*
             * Construit l'algo de routage avec le WS
             */
            RoutingCommande routing = new RoutingCommande();

            /*
             * Récupère la liste des pays d'Europe du WS
             */
            List<String> listePays = routing.getListPays();
            request.setAttribute( ATT_LISTEPAYS, listePays );

            /* ré-affichage du formulaire de création avec les erreurs ou succès */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }

    }
}