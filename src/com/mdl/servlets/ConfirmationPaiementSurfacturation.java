package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ConfirmationPaiementSurfacturation extends HttpServlet {
    public static final String VUE                 = "/WEB-INF/pagesJSP/client/confirmSurfact.jsp";
    public static final String ACCES_CONNEXION     = "/ConnexionClient";
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";

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
            // request.getRequestDispatcher( ACCES_CONNEXION ).forward( request,
            // response );
        } else {
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

}
