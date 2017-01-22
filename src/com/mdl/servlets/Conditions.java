package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet permettant de rediriger vers la vue affichant les conditions générales d'utilisation
 * 
 *
 */
public class Conditions extends HttpServlet {
	// Constante de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/conditions.jsp";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Attribut à envoyer à la page JSP
    public static final String ATT_USER  		   = "condition";

    /**
     * Redirection vers la page JSP pour les conditions générales d'utilisation
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        
    	String condition;
        HttpSession session = request.getSession();
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	// Si l'employé est connecté, son menu sera affiché
        	condition = "employe";
        } else if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
        	// Si le client est connecté, son menu sera affiché
            condition = "client";
        } else {
        	// Sinon le menu visiteur sera affiché
            condition = "visiteur";
        }
        request.setAttribute( ATT_USER, condition );
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}