package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet de l'affichage de l'accueil pour le visiteur. 
 * 
 * 
 */
public class Accueil extends HttpServlet {
	// Constante de la servlet pour les vue et les attributs
    public static final String VUE                 = "/WEB-INF/pagesJSP/accueil.jsp";
    public static final String ACCES_CONNECTE      = "/AccueilClient";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";

    /**
     * Affichage de l'accueil du visiteur
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	// Si la session de l'employé existe, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
        	// Si la session de l'utilisateur n'existe pas, il accède à l'accueil
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        } else {
        	// Sinon il est redirigé vers la page d'accueil du client (connecté)
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE );
        }
    }
}