package com.mdl.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Commande;
import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.SurfacturationDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.pdf.EnvoyerFacture;
import com.mdl.pdf.EnvoyerFormulaire;

public class ConsulterCommande extends HttpServlet {

    /**
     * déclaration des constantes
     */
    public static final String  ACCES_CONNEXION           = "/ConnexionClient";
    public static final String  ATT_SESSION_USER          = "sessionUtilisateur";
    public static final String  ATT_COMS                  = "commandes";
    public static final String  ATT_COM                   = "commandeHist";
    public static final String  ATT_SURFACT               = "surfact";
    public static final String  ATT_ERREUR                = "erreur";
    public static final String  CONF_DAO_FACTORY          = "daofactory";
    public static final String  ATT_USER                  = "utilisateur";
    public static final String  ATT_NUMCOM                = "numCom";
    public static final String  ATT_SESSION_EMPL          = "sessionEmploye";
    public static final String  ACCES_CONNECTE_EMPL       = "/AccueilEmploye";

    /*
     * constantes pour les attributs de requetes correspondant à l'utilisation
     * du javascript
     */
    private static final String ATT_FICHIER               = "fichier";
    private static final String ATT_URL                   = "url";
    private static final String ATT_VALEUR_MONTANT        = "valeur_montant";
    public static final String  ATT_PRIX_FORFAIT          = "prix_forfait";
    public static final String  ATT_PRIX_MONTANT_BAS      = "prix_montant_bas";
    public static final String  ATT_PCT_MONTANT_BAS       = "pct_montant_bas";
    public static final String  ATT_PCT_MONTANT_HAUT      = "pct_montant_haut";
    private static final String ATT_PRIX_ACC              = "prix_acc";

    private static final String FICHIER_PROPERTIES        = "/com/mdl/config/configuration.properties";

    private static final String PROPERTY_FICHIER          = "fichier";
    private static final String PROPERTY_URL              = "url";
    private static final String PROPERTY_VALEUR_MONTANT   = "valeur_montant";
    private static final String PROPERTY_PRIX_FORFAIT     = "prix_forfait";
    private static final String PROPERTY_PRIX_MONTANT_BAS = "prix_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_BAS  = "pct_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_HAUT = "pct_montant_haut";
    private static final String PROPERTY_PRIX_ACC         = "prix_acc";

    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String  VUE                       = "/WEB-INF/pagesJSP/client/consulterCommande.jsp";
    public static final String  VUE_RETOUR                = "/WEB-INF/pagesJSP/client/historiqueCommandes.jsp";

    private UtilisateurDAO      utilisateurDao;
    private CommandeDAO         commandeDao;
    private SurfacturationDAO   surfacturationDao;

    /**
     * initialisation de la commande et du client et ses informations en base de
     * donnée
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        /* Récupération d'une instance de notre DAO Commande */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
        /* Récupération d'une instance de notre DAO surfacturation */
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getSurfacturationDao();
    }

    /**
     * affichage du l'historique, de la page jsp
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page de connexion */

        HttpSession session = request.getSession();

        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            String numCom = request.getParameter( ATT_NUMCOM );
            String email = (String) session.getAttribute( ATT_SESSION_USER );

            /*
             * robustesse / vérifie qu'on ne puisse pas accéder à une autre
             * commande d'un autre client/ renvoie un message d'erreur sinon
             */
            if ( commandeDao.existICU( numCom, email ) ) {
                Commande commande = commandeDao.trouver( numCom );
                Surfacturation surfacturation = surfacturationDao.trouverSurfactCommande( numCom );

                // Permet d'aller rechercher les informations dans le fichier de
                // configuration
                Properties properties = new Properties();
                String CHAMP_FICHIER;
                String CHAMP_URL;
                String CHAMP_PRIX_FORFAIT;
                String CHAMP_PRIX_MONTANT_BAS;
                String CHAMP_PCT_MONTANT_BAS;
                String CHAMP_PCT_MONTANT_HAUT;
                String CHAMP_PRIX_ACC;
                String CHAMP_VALEUR_MONTANT;

                double PRIX_FORFAIT;
                double PRIX_MONTANT_BAS;
                double PCT_MONTANT_BAS;
                double PCT_MONTANT_HAUT;
                double PRIX_ACC;
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
                    CHAMP_PRIX_FORFAIT = properties.getProperty( PROPERTY_PRIX_FORFAIT );
                    CHAMP_PRIX_MONTANT_BAS = properties.getProperty( PROPERTY_PRIX_MONTANT_BAS );
                    CHAMP_PCT_MONTANT_BAS = properties.getProperty( PROPERTY_PCT_MONTANT_BAS );
                    CHAMP_PCT_MONTANT_HAUT = properties.getProperty( PROPERTY_PCT_MONTANT_HAUT );
                    CHAMP_PRIX_ACC = properties.getProperty( PROPERTY_PRIX_ACC );
                    CHAMP_VALEUR_MONTANT = properties.getProperty( PROPERTY_VALEUR_MONTANT );

                    PRIX_FORFAIT = Double.parseDouble( CHAMP_PRIX_FORFAIT );
                    PRIX_MONTANT_BAS = Double.parseDouble( CHAMP_PRIX_MONTANT_BAS );
                    PCT_MONTANT_BAS = Double.parseDouble( CHAMP_PCT_MONTANT_BAS );
                    PCT_MONTANT_HAUT = Double.parseDouble( CHAMP_PCT_MONTANT_HAUT );
                    PRIX_ACC = Double.parseDouble( CHAMP_PRIX_ACC );
                    VALEUR_MONTANT = Double.parseDouble( CHAMP_VALEUR_MONTANT );

                    /*
                     * Placement en attribut de la requete des propriétés de
                     * configuration pour les utiliser
                     */
                    request.setAttribute( ATT_FICHIER, CHAMP_FICHIER );
                    request.setAttribute( ATT_URL, CHAMP_URL );
                    request.setAttribute( ATT_PRIX_FORFAIT, PRIX_FORFAIT );
                    request.setAttribute( ATT_PRIX_MONTANT_BAS, PRIX_MONTANT_BAS );
                    request.setAttribute( ATT_PCT_MONTANT_BAS, PCT_MONTANT_BAS );
                    request.setAttribute( ATT_PCT_MONTANT_HAUT, PCT_MONTANT_HAUT );
                    request.setAttribute( ATT_PRIX_ACC, PRIX_ACC );
                    request.setAttribute( ATT_VALEUR_MONTANT, VALEUR_MONTANT );

                } catch ( IOException e ) {
                    throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
                }

                /* Ajout du bean et de l'objet métier à l'objet requête */
                request.setAttribute( ATT_COM, commande );
                request.setAttribute( ATT_SURFACT, surfacturation );
            } else {
                /* Ajout du bean et de l'objet métier à l'objet requête */
                request.setAttribute( ATT_ERREUR, "Impossible d'accéder à cette commande" );
            }
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

    /**
     * affichage de l'historique et récupération des informations de
     * l'intéraction de l'utilisateur sur l'historique et redirection sur les
     * pages correspondantes.
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else if ( request.getParameter( "mail" ) != null ) {
            /* Ajout du bean et de l'objet métier à l'objet requête */

            /**
             * récupération de la commande
             */
            String numCom = request.getParameter( ATT_NUMCOM );
            Commande commande = commandeDao.trouver( numCom );

            EnvoyerFormulaire pdf = new EnvoyerFormulaire();
            /**
             * Envoie du formulaire pdf (bordereau d'envoi) par mail
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );

            try {
                pdf.EmailFormulaire( mail, commande );
            } catch ( Exception e ) {
            }
            /* Redirection vers l'historique */
            response.sendRedirect( request.getContextPath() + "/HistoriqueCommandes" );
        } else if ( request.getParameter( "facture" ) != null ) {
            /* Ajout du bean et de l'objet métier à l'objet requête */

            /**
             * récupération de la commande
             */
            String numCom = request.getParameter( ATT_NUMCOM );
            Commande commande = commandeDao.trouver( numCom );

            EnvoyerFacture pdf = new EnvoyerFacture();
            /**
             * Envoie du formulaire pdf (facture) par mail
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            Utilisateur utilisateur = utilisateurDao.trouver( mail );
            try {
                pdf.EmailFacture( mail, commande, utilisateur );
            } catch ( Exception e ) {
            }
            /* Redirection vers l'historique */
            response.sendRedirect( request.getContextPath() + "/HistoriqueCommandes" );
        } else if ( request.getParameter( "telechargementFact" ) != null ) {
            /* Ajout du bean et de l'objet métier à l'objet requête */

            /**
             * récupération de la commande
             */
            String numCom = request.getParameter( ATT_NUMCOM );
            Commande commande = commandeDao.trouver( numCom );

            EnvoyerFacture pdf = new EnvoyerFacture();
            /**
             * Envoie du formulaire pdf (facture) par mail
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            Utilisateur utilisateur = utilisateurDao.trouver( mail );

            ByteArrayOutputStream outputStream = null;
            try {
                outputStream = new ByteArrayOutputStream();
                pdf.GenererPDF( outputStream, commande, utilisateur, response );
            } catch ( Exception e ) {
            }
            /* Redirection vers l'historique */
            // response.sendRedirect( request.getContextPath() +
            // "/HistoriqueCommandes" );

        } else if ( request.getParameter( "telechargementBordereau" ) != null ) {
            /* Ajout du bean et de l'objet métier à l'objet requête */

            /**
             * récupération de la commande
             */
            String numCom = request.getParameter( ATT_NUMCOM );
            Commande commande = commandeDao.trouver( numCom );

            EnvoyerFormulaire pdf = new EnvoyerFormulaire();
            /**
             * Envoie du formulaire pdf (facture) par mail
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            Utilisateur utilisateur = utilisateurDao.trouver( mail );

            ByteArrayOutputStream outputStream = null;
            try {
                outputStream = new ByteArrayOutputStream();
                pdf.GenererPDF( outputStream, commande, response );
            } catch ( Exception e ) {
            }
            /* Redirection vers l'historique */
            // response.sendRedirect( request.getContextPath() +
            // "/HistoriqueCommandes" );
        } else {
            /* Redirection vers l'historique */
            response.sendRedirect( request.getContextPath() + "/HistoriqueCommandes" );
        }

    }
}
