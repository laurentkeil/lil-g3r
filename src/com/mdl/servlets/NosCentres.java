package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet permettant le contrôle de l'affichage des données des centres
 * 
 *
 */
public class NosCentres extends HttpServlet {
	// Attribut de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/nosCentres.jsp";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Attribut utile pour la page JSP
    public static final String ATT_USER  		   = "centres";

    /**
     * Méthode permettant la redirection vers la page JSP pour l'affichage des données des centres de HLB-Express
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

    	String centres;
        HttpSession session = request.getSession();
        // Permet d'afficher les bons menus en fonction des sessions
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	centres = "employe";
        } else if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
            centres = "client";
        } else {
            centres = "visiteur";
        }
        request.setAttribute( ATT_USER, centres );
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}