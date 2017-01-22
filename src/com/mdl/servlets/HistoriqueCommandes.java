package com.mdl.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Commande;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;

/**
 * Servlet de listing des commandes. Affichage d'un historique des commandes
 * avec les informations entrées par l'utilisateur.
 */
public class HistoriqueCommandes extends HttpServlet {

    /**
     * déclaration des constantes
     */
    public static final String ACCES_CONNEXION     = "/ConnexionClient";
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_COMS            = "commandes";
    public static final String ATT_COM             = "commande";
    public static final String ATT_SESSION_COM     = "commandeHist";
    public static final String CONF_DAO_FACTORY    = "daofactory";
    public static final String ATT_USER            = "utilisateur";
    public static final String ATT_NUMCOM          = "numCom";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";

    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String VUE                 = "/WEB-INF/pagesJSP/client/historiqueCommandes.jsp";
    public static final String VUE_DETAIL          = "/WEB-INF/pagesJSP/client/consulterCommande.jsp";

    private CommandeDAO        commandeDao;

    /**
     * initialisation de la commande et ses informations en base de donnée
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Commande */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
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
            String email = (String) session.getAttribute( ATT_SESSION_USER );

            List<Commande> commandes = commandeDao.lister( email );

            request.setAttribute( ATT_COMS, commandes );

            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

    /**
     * affichage de la commande en fonction de l'icu de commande encodé
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
        } else {
            String email = (String) session.getAttribute( ATT_SESSION_USER );
            /**
             * acces en bd a une commande suivant l'icu de la commande
             */
            String icu = request.getParameter( "rech" );
            List<Commande> commandes = new ArrayList<Commande>();
            /*
             * robustesse / vérifie qu'on ne puisse pas accéder à une autre
             * commande / verifie que l icu existe
             */
            if ( commandeDao.existICU( icu, email ) ) {
                Commande commande = commandeDao.trouver( icu );
                /* Ajout du bean et de l'objet métier à l'objet requête */
                commandes.add( commande );
                request.setAttribute( ATT_COMS, commandes );
            }

            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }
}
