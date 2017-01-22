package com.mdl.servlets;

import hlbexpress.routing.facade.RoutingCommande;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;

/**
 * Servlet de modification du carnet d'adresses. Affichage d'une liste et
 * récupération des manipulations faites par l'utilisateur.
 * 
 */
public class Carnet extends HttpServlet {
    /**
     * déclaration des constantes
     */
    public static final String ATT_COMMANDE         = "commande";
    public static final String ATT_CARNET           = "carnet_adresses";
    public static final String ATT_TAILLE           = "taille";
    public static final String ATT_NOM              = "nom";
    public static final String ATT_PRENOM           = "prenom";
    public static final String ATT_RUE              = "rue";
    public static final String ATT_NUM              = "num";
    public static final String ATT_BOITE            = "boite";
    public static final String ATT_CODE             = "code";
    public static final String ATT_LOC              = "loc";
    public static final String ATT_PAYS             = "pays";
    public static final String ATT_TEL              = "tel";
    public static final String ATT_ID               = "id";
    public static final String ATT_LISTEPAYS        = "listePays";

    public static final String ATT_SESSION_COMMANDE = "sessionCommande";
    public static final String ATT_SESSION_USER     = "sessionUtilisateur";
    public static final String CONF_DAO_FACTORY     = "daofactory";
    public static final String ACCES_CONNEXION      = "/ConnexionClient";
    public static final String ATT_SESSION_EMPL     = "sessionEmploye";
    public static final String ACCES_CONNECTE_EMPL  = "/AccueilEmploye";

    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String VUE_LISTE            = "/WEB-INF/pagesJSP/client/carnet.jsp";

    private UtilisateurDAO     utilisateurDao;
    private CommandeDAO        commandeDao;

    /**
     * initialisation de l'utilisateur et ses informations en base de donnée +
     * la commande
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Commande */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }

    /**
     * affichage du formulaire, de la page jsp
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();

        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté et donc on redirige l'utilisateur
         * vers la page de connexion, sinon on affiche le carnet.
         */
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            String email = (String) session.getAttribute( ATT_SESSION_USER );

            // supprime l'adresse du carnet avec son id
            if ( request.getParameter( "id" ) != null ) {
                commandeDao.deleteCarnet( Integer.parseInt( request.getParameter( "id" ) ) );
            }

            /*
             * récupération du carnet d'adresse en BD
             */
            List<List<String>> carnet_adresse = commandeDao.listerCarnet( email );

            /*
             * mise en place des attributs dans la requête de la page
             */
            request.setAttribute( ATT_CARNET, carnet_adresse );

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
             * A la réception d'une requête GET, simple affichage du formulaire
             */
            this.getServletContext().getRequestDispatcher( VUE_LISTE ).forward( request, response );

        }

    }

    /**
     * affichage du carnet d adresses et récupération des informations encodés
     * par l'utilisateur et redirection sur des vues suivant le résultat de la
     * création de la commande.
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        request.setCharacterEncoding( "UTF-8" );
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute( ATT_SESSION_USER );

        /*
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else if ( request.getParameter( "rech" ) != null ) { // recherche
                                                               // d'une adresse
                                                               // par nom

            String nomRech = request.getParameter( "rech" );
            /*
             * récupération du carnet d'adresse en BD
             */
            List<List<String>> adresse = commandeDao.selectCarnet( email, nomRech );
            /*
             * mise en place des attributs dans la requête de la page
             */
            request.setAttribute( ATT_CARNET, adresse );

            /*
             * Construit l'algo de routage avec le WS
             */
            RoutingCommande routing = new RoutingCommande();

            /*
             * Récupère la liste des pays d'Europe du WS
             */
            List<String> listePays = routing.getListPays();
            request.setAttribute( ATT_LISTEPAYS, listePays );
            /* ré-affichage du formulaire et du carnet */
            this.getServletContext().getRequestDispatcher( VUE_LISTE ).forward( request, response );

        } else {
            /* Ajoute une adresse dans le carnet */

            Utilisateur utilisateur = utilisateurDao.trouver( email );
            List<String> carnet = new ArrayList();
            carnet.add( request.getParameter( "nom_destinataire" ) );
            carnet.add( request.getParameter( "prenom_destinataire" ) );
            carnet.add( request.getParameter( "adresse_pays_destinataire" ) );
            carnet.add( request.getParameter( "adresse_code_destinataire" ) );
            carnet.add( request.getParameter( "adresse_loc_destinataire" ) );
            carnet.add( request.getParameter( "adresse_num_destinataire" ) );
            carnet.add( request.getParameter( "adresse_rue_destinataire" ) );
            carnet.add( request.getParameter( "adresse_boite_destinataire" ) );
            carnet.add( request.getParameter( "tel_destinataire" ) );
            commandeDao.addCarnet( carnet, utilisateur.getId() );

            /*
             * récupération du carnet d'adresse en BD
             */
            List<List<String>> carnet_adresse = commandeDao.listerCarnet( email );

            /*
             * mise en place des attributs dans la requête de la page
             */
            request.setAttribute( ATT_CARNET, carnet_adresse );

            /*
             * Construit l'algo de routage avec le WS
             */
            RoutingCommande routing = new RoutingCommande();

            /*
             * Récupère la liste des pays d'Europe du WS
             */
            List<String> listePays = routing.getListPays();
            request.setAttribute( ATT_LISTEPAYS, listePays );
            /* ré-affichage du formulaire et du carnet */
            this.getServletContext().getRequestDispatcher( VUE_LISTE ).forward( request, response );
        }

    }
}
