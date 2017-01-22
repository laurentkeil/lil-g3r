package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet permettant le contrôle d'affichage 
 * 
 *
 */
public class ContactEntreprise extends HttpServlet {
	// Constante de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/contactEntreprise.jsp";
    // Attributs de sessions
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Attribut pour la page JSP
    public static final String ATT_USER  		   = "contact";

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	String contact;
        HttpSession session = request.getSession();
        // Permet d'afficher les menus selon les sessions
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	contact = "employe";
        } else if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
            contact = "client";
        } else {
            contact = "visiteur";
        }
        request.setAttribute( ATT_USER, contact );
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}