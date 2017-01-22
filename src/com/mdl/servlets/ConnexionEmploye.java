package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Employe;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.EmployeDAO;
import com.mdl.forms.ConnexionEmployeForm;

/**
 * Servlet contrôlant la connexion d'un employé sur l'application-web
 * 
 *
 */
public class ConnexionEmploye extends HttpServlet {
	// constantes de redirection
    public static final String VUE_ARRIVEE         = "/WEB-INF/pagesJSP/employe/connexionEmploye.jsp";
    public static final String VUE_SORTIE          = "/AccueilEmploye";
    public static final String ACCES_CONNECTE      = "/AccueilEmploye";
    public static final String ACCES_CONNECTE_USER = "/AccueilClient";
    // Constante pour la DAO Factory
    public static final String CONF_DAO_FACTORY    = "daofactory";
 // Attributs utiles pour les pages JSP
    public static final String ATT_USER            = "employe";
    public static final String ATT_FORM            = "form";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    public static final String ATT_SESSION_ID      = "sessionIdEmpl";

    private EmployeDAO         employeDao;

    /**
     * Initialisation de l'utilisateur pour la base de données
     * 
     */
    public void init() throws ServletException {
        this.employeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getEmployeDao();
    }

    /**
     * Redirige vers la page de connexion
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page d'inscription */
        HttpSession session = request.getSession();
        if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
            // Si l'utilisateur est déjà connecté, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        } else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
        	// Si l'employé n'est pas encore connecté, il accède à sa page de connexion
            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        } else {
        	// Si un employé est déjà connecté, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE );
        }
    }
    
    /**
     * Méthode permettant de contrôler les informations issues du formulaire de connexion
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        ConnexionEmployeForm form = new ConnexionEmployeForm( employeDao );
        Employe employe = form.connecterEmploye( request );
        String email = employe.getEmail();
        HttpSession session = request.getSession();

        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, employe );
        if ( form.getErreurs().isEmpty() ) {
            session.setAttribute( ATT_SESSION_EMPL, email );
            employe = employeDao.trouver( email );
            int id = employe.getId();
            session.setAttribute( ATT_SESSION_ID, id );
            response.sendRedirect( request.getContextPath() + VUE_SORTIE );
        } else {
            session.setAttribute( ATT_SESSION_EMPL, null );
            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        }
    }

}