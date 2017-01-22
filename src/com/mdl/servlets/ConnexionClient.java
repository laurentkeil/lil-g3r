package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.ConnexionClientForm;

/**
 * Servlet contrôlant la connexion d'un client sur l'application-web
 * 
 *
 */
public class ConnexionClient extends HttpServlet {
	// constantes de redirection
    public static final String VUE_ARRIVEE         = "/WEB-INF/pagesJSP/connexionClient.jsp";
    public static final String VUE_SORTIE          = "/AccueilClient";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    public static final String ACCES_CONNECTE      = "/AccueilClient";
    // Constante pour la DAO Factory
    public static final String CONF_DAO_FACTORY    = "daofactory";
    // Attributs utiles pour les pages JSP
    public static final String ATT_USER            = "utilisateur";
    public static final String ATT_FORM            = "form";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";

    private UtilisateurDAO     utilisateurDao;

    
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }
    /**
     * Redirige vers la page de connexion
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
       
        HttpSession session = request.getSession();

        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	// Si un employé est déjà connecté, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            // Si l'utilisateur n'est pas encore connecté, il accède à la page de connexion
            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        } else {
        	// Sinon l'utilisateur est redirigé vers sa page d'accueil.
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE );
        }
    }

    /**
     * Méthode permettant de contrôler les informations issues du formulaire de connexion
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        ConnexionClientForm form = new ConnexionClientForm( utilisateurDao );
        Utilisateur utilisateur = form.connecterUtilisateur( request );
        String email = utilisateur.getEmail();
        HttpSession session = request.getSession();

        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );
        if ( form.getErreurs().isEmpty() ) {
            session.setAttribute( ATT_SESSION_USER, email );
            response.sendRedirect( request.getContextPath() + VUE_SORTIE );
        } else {
            session.setAttribute( ATT_SESSION_USER, null );

            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        }
    }

}