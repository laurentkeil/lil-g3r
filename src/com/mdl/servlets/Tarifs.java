package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet permettant le contrôle de l'affichage des données de tarifications
 * 
 *
 */
public class Tarifs extends HttpServlet {
	// Attribut de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/tarifs.jsp";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Attribut utile pour la page JSP
    public static final String ATT_USER  		   = "tarif";

    /**
     * Méthode permettant la redirection vers la page JSP pour l'affichage des données de tarification
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
 
    	String tarif;
        HttpSession session = request.getSession();
        // Permet d'afficher les bons menus en fonction des sessions
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	tarif = "employe";
        } else if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
            tarif = "client";
        } else {
            tarif = "visiteur";
        }
        request.setAttribute( ATT_USER, tarif );
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}