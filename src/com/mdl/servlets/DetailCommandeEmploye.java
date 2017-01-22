package com.mdl.servlets;

import java.io.IOException;

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

/**
 * Servlet permettant de contrôler les informations utiles pour l'affichage des détails de la commande
 * 
 *
 */
public class DetailCommandeEmploye extends HttpServlet {
	// Constantes de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/employe/detailCommande.jsp";
    public static final String ACCES_CONNEXION     = "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY    = "daofactory";
    // Attributs utiles pour la page JSP
    public static final String ATT_LIST_COMMANDE   = "commandeList";
    public static final String ATT_LIST_DETAIL     = "listDetail";
    public static final String ATT_COMMANDE        = "commande";
    public static final String ATT_CLIENT          = "client";
    public static final String ATT_SUR             = "surfacturation";
    public static final String ATT_MAIL_CLIENT     = "mailClient";

    private CommandeDAO        commandeDao;
    private UtilisateurDAO     utilisateurDao;
    private SurfacturationDAO  surfacturationDao;

    /**
     * Initialisation du DAO de l'utilisateur, de la commande et de surfacturation permettant l'interaction
     * avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getSurfacturationDao();
    }

    /**
     * Méthode permettant de rediriger vers la page JSP pour les détails d'une commande donnée
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );

        if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
        	// Si un utilisateur est connecté, il est redirigé vers sa page d'accueils
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            // Si l'employé n'est pas connecté, il est redirigé vers sa page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else {
        	// Recherche de la commande 
            Commande commande = new Commande();
            String icu = request.getParameter( "ICU" );
            commande = commandeDao.trouver( icu );
            Surfacturation surfacturation = surfacturationDao.trouverSurfactCommande( icu );
            Utilisateur client = utilisateurDao.trouver( commande.getClient() );
            request.setAttribute( ATT_COMMANDE, commande );
            request.setAttribute( ATT_CLIENT, client );
            request.setAttribute( ATT_SUR, surfacturation );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

}
