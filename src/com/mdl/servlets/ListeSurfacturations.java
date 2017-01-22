package com.mdl.servlets;

import java.io.ByteArrayOutputStream;
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

import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.SurfacturationDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.pdf.EnvoyerFacture;

/**
 * Servlet de listing des surfacturations s'il y en a (en fonction d'un
 * dépassement de poids).
 */
public class ListeSurfacturations extends HttpServlet {

    /**
     * déclaration des constantes
     */
    public static final String  ACCES_CONNEXION     = "/ConnexionClient";
    public static final String  ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String  ATT_SURFACT         = "surfact";
    public static final String  ATT_NUMCOM          = "icu";
    public static final String  ATT_STATUT          = "statut";
    public static final String  CONF_DAO_FACTORY    = "daofactory";
    public static final String  ATT_SESSION_EMPL    = "sessionEmploye";
    public static final String  ACCES_CONNECTE_EMPL = "/AccueilEmploye";

    /*
     * constantes pour les attributs de requetes correspondant à l'utilisation
     * du javascript
     */
    private static final String ATT_FICHIER         = "fichier";
    private static final String ATT_URL             = "url";

    private static final String FICHIER_PROPERTIES  = "/com/mdl/config/configuration.properties";

    private static final String PROPERTY_FICHIER    = "fichier";
    private static final String PROPERTY_URL        = "url";

    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String  VUE                 = "/WEB-INF/pagesJSP/client/listeSurfacturations.jsp";

    private SurfacturationDAO   surfacturationDao;
    private StatutDAO           statutDao;
    private UtilisateurDAO      utilisateurDao;
    private CommandeDAO         commandeDao;

    /**
     * initialisation de la commande et ses informations en base de donnée
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO surfacturation */
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getSurfacturationDao();
        /* Récupération d'une instance de notre DAO statut */
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getStatutDao();
        /* Récupération d'une instance de notre DAO utilisateur */
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        /* Récupération d'une instance de notre DAO Commande */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    /**
     * affichage du la liste des surfacturations, de la page jsp
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
            String email = (String) session.getAttribute( ATT_SESSION_USER );

            String statut = request.getParameter( ATT_STATUT );
            /**
             * acces en bd a la liste de surfacturation (payees impayées ou
             * toutes suivant les informations provenant du client)
             */
            List<Surfacturation> surfact = null;
            if ( statut.equals( "1" ) ) {
                surfact = surfacturationDao.listerPayees( email );
            } else if ( statut.equals( "2" ) ) {
                surfact = surfacturationDao.listerImpayees( email );
            } else {
                surfact = surfacturationDao.lister( email );
            }

            /**
             * Itère sur la liste des surfacturations pour vérifier si il y en a
             * des non-payées depuis + de 15 jours après leur notification. Si
             * oui, blocage du compte.
             */
            Iterator i = surfact.iterator();
            boolean continuer = true;
            while ( i.hasNext() && continuer ) {
                Surfacturation surf = (Surfacturation) i.next();
                if ( surf.getJourRestant() == 0 && surf.getDate_paiement() == null ) {
                    continuer = false;
                    statutDao.bloquerCompte( surf.getId_client(),
                            "surfacturation" );
                }
            }

            // Permet d'aller rechercher les informations dans le fichier de
            // configuration
            Properties properties = new Properties();
            String CHAMP_FICHIER;
            String CHAMP_URL;

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

            if ( fichierProperties == null ) {
                throw new ServletException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
            }

            try {
                properties.load( fichierProperties );
                CHAMP_FICHIER = properties.getProperty( PROPERTY_FICHIER );
                CHAMP_URL = properties.getProperty( PROPERTY_URL );

                /*
                 * Placement en attribut de la requete des propriétés de
                 * configuration pour les utiliser
                 */
                request.setAttribute( ATT_FICHIER, CHAMP_FICHIER );
                request.setAttribute( ATT_URL, CHAMP_URL );

            } catch ( IOException e ) {
                throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
            }

            request.setAttribute( ATT_STATUT, statut );
            request.setAttribute( ATT_SURFACT, surfact );

            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

    /**
     * affichage de la surfacturation en fonction de l'icu de commande encodé
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
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
        } else if ( request.getParameter( "email" ) != null ) {
            /* Ajout du bean et de l'objet métier à l'objet requête */

            /**
             * récupération de la surfacturation
             */
            String icu = request.getParameter( ATT_NUMCOM );
            Surfacturation surfacturation = surfacturationDao.trouverSurfactCommande( icu );
            /**
             * Récupération du mail utilisateur en session
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            /**
             * Récupération du client en BD
             */
            Utilisateur utilisateur = utilisateurDao.trouver( mail );

            /**
             * Envoie du formulaire pdf (surfacture) par mail
             */
            EnvoyerFacture pdf2 = new EnvoyerFacture();
            try {
                pdf2.EmailFacture( mail, surfacturation, utilisateur );
            } catch ( Exception e ) {
            }

            /* Redirection vers la liste des surfacturations */
            response.sendRedirect( request.getContextPath() + "/ListeSurfacturations?statut="
                    + request.getParameter( ATT_STATUT ) );
        } else if ( request.getParameter( "telechSurfact" ) != null ) {
            /* Ajout du bean et de l'objet métier à l'objet requête */

            /**
             * récupération de la surfacturation
             */
            String icu = request.getParameter( ATT_NUMCOM );
            Surfacturation surfacturation = surfacturationDao.trouverSurfactCommande( icu );
            /**
             * Récupération du mail utilisateur en session
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            /**
             * Récupération du client en BD
             */
            Utilisateur utilisateur = utilisateurDao.trouver( mail );

            EnvoyerFacture pdf = new EnvoyerFacture();
            ByteArrayOutputStream outputStream = null;

            try {
                outputStream = new ByteArrayOutputStream();
                pdf.GenererPDF( outputStream, surfacturation, utilisateur, response );
            } catch ( Exception e ) {
            }

        } else {
            String email = (String) session.getAttribute( ATT_SESSION_USER );
            String statut = request.getParameter( ATT_STATUT );
            /**
             * acces en bd a une surfacturation suivant l'icu de la commande
             * (payees impayées ou toutes suivant les informations provenant du
             * client)
             */
            String icu = request.getParameter( "rech" );
            List<Surfacturation> surfact = new ArrayList<Surfacturation>();
            /*
             * robustesse / vérifie qu'on ne puisse pas accéder à une autre
             * commande / verifie que l icu existe
             */
            if ( commandeDao.existICU( icu, email ) ) {
                Surfacturation surf = surfacturationDao.trouverSurfactCommande( icu );
                surfact.add( surf );
                request.setAttribute( ATT_SURFACT, surfact );
            }

            request.setAttribute( ATT_STATUT, statut );

            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }
}
