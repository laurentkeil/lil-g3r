package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Servlet de l'accueil du client lorsque qu'il est connecté
 * 
 *
 */
public class AccueilClient extends HttpServlet {
	// Constantes utilisées pour la redirection vers d'autres pages.
    public static final String  VUE                   = "/WEB-INF/pagesJSP/client/accueilClient.jsp";
    public static final String ACCES_CONNEXION  	  = "/ConnexionClient";
    public static final String ACCES_CONNECTE_EMPL    = "/AccueilEmploye";
    // Attributs de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
 
    /**
     * Redirection vers la vue utilisée pour l'affichage de la page d'accueil du client
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
       
    	HttpSession session = request.getSession();
    	 
        
    	if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
    		// Si l'employé est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        }else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            // Si aucun attribut de session n'est défini, le visiteur est redirigé vers sa page d'accueil
        	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else{
        	// Sinon, le client est redirigé vers sa page d'accueil
        	this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
        
    }

}
