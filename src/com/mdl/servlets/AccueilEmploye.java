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
 * Servlet permettant d'afficher la page d'accueil de l'employé
 * 
 *
 */
public class AccueilEmploye extends HttpServlet {
	// Constantes utilisées pour la redirection
    public static final String  VUE              	  = "/WEB-INF/pagesJSP/employe/accueilEmploye.jsp";
    public static final String ACCES_CONNEXION  	  = "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_USER    = "/AccueilClient";
    // Attributs de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
 
    /**
     * Redirection vers la vue permettant d'afficher la page d'accueil de l'employé
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
     
    	HttpSession session = request.getSession();
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		// Si l'utilisateur est connecté, il est redirigé vers l'accueil du client
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            // Aucun attribut de session n'est déclaré, on est redirigé vers la page de connexion
        	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else{
        	// Sinon il est redirigé vers la page d'accueil de l'employé
        	this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
        
    }

}
