package com.mdl.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Litige;
import com.mdl.beans.Statut_client;
import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.SurfacturationDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.forms.RechercheClientForm;
import com.mdl.forms.RechercheLitigeForm;
import com.mdl.forms.RechercheSurfacturationForm;
import com.mdl.parser.Convertion;

/**
 * Servlet permettant de contrôler les données permettant la consultation des surfacturations des clients
 * 
 *
 */
public class ConsulterSurfacturationEmploye extends HttpServlet {
	// Attributs de redirection
    public static final String VUE             		= "/WEB-INF/pagesJSP/employe/listerSurfacturation.jsp";
    public static final String VUE_DETAIL       	= "/WEB-INF/pagesJSP/employe/consulterProfilClient.jsp";
    public static final String ACCES_CONNEXION  	= "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_EMPL  = "/AccueilEmploye";
    // Attributs de sessions
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
    // Constante du DAOFactory
    public static final String CONF_DAO_FACTORY = "daofactory";
    // Attributs utiles pour la page JSP
    public static final String ATT_LIST_SURFACTURATION  = "surfactList";
    public static final String ATT_SURFACT 				= "surfact";
    public static final String ATT_LIST_DETAIL  		= "listDetail";
    public static final String ATT_CLIENT       		= "client";
    public static final String ATT_STATUT       		= "statut";
    public static final String ATT_SUR     	    		= "surfacturation";
    public static final String ATT_USER         		= "utilisateur";
    public static final String ATT_MAIL_CLIENT      	= "mailClient";

    private UtilisateurDAO        utilisateurDao;
    private StatutDAO 			  statutDao;
    private SurfacturationDAO 	  surfacturationDao;

    /**
     * Initisalition de la DAO de l'utilisateur, du statut et de la surfacturation pour l'interaction avec la
     * base de données
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getStatutDao();
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getSurfacturationDao();
    }

    /**
     * Méthode permettant de se rediriger vers la page JSP en affichant les surfacturations
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page de connexion */

        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        
        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connect�.
         */
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		// Si l'utilisateur est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            // Si l'employé n'est pas connecté, il est redirigé vers la page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else {
        	 String surfact = request.getParameter( "surfact" );
        	 List<Surfacturation> surfactList = new ArrayList<Surfacturation>();
        	 if (surfact.equals("all")){
        		 // lister toutes les surfacturations
        		surfactList = surfacturationDao.listerAll();
        	 }else if (surfact.equals("p")){
        		 // lister les surfacturations payées
        		 surfactList = surfacturationDao.listerAllPayees();
        	 }else if (surfact.equals("i")){
        		 // lister les surfacturations impayées
        		 surfactList = surfacturationDao.listerAllImpayees();
        	 }
        	 request.setAttribute( ATT_SURFACT, surfact );
	         request.setAttribute( ATT_LIST_SURFACTURATION, surfactList );
             this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }
    
    /**
     * Méthode permettant de contrôler les informations issues du formulaire
     * 
     */
   public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {

        	RechercheSurfacturationForm form = new RechercheSurfacturationForm(surfacturationDao);
            List<Surfacturation> surfactList = form.rechercheSurfact( request );
            request.setAttribute( ATT_LIST_SURFACTURATION, surfactList );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        		
        }
    }
    	
}
