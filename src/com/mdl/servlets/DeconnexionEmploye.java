package com.mdl.servlets;
 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mdl.beans.Employe;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.EmployeDAO;
/**
 * Servlet permettant de contrôler la déconnexion d'un employé
 * 
 *
 */
public class DeconnexionEmploye extends HttpServlet {
	// Constante de redirection
    public static final String VUE_ARRIVEE 			= "/WEB-INF/pagesJSP/employe/deconnexionEmploye.jsp";
    public static final String VUE_DECO 			= "/ConnexionEmploye";
    public static final String VUE_CO 				= "/AccueilEmploye";
    public static final String ACCES_CONNEXION 		= "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_USER  = "/AccueilClient";
    // Attributs utiles pour la page JSP
    public static final String ATT_USER 			= "utilisateur";
    public static final String ATT_FORM 			= "form";
    private static final String CHAMP_DECO 	 		= "deco";
    // Constante de la DAO Factory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER 	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL 	= "sessionEmploye";
    public static final String  ATT_SESSION_ID 		= "sessionIdEmpl";
    
    /**
     * Méthode permettant la redirection vers la page de déconnexion
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        /* Affichage de la page d'inscription */
    	HttpSession session = request.getSession();
   	 
        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            /* Redirection vers la page publique */
        	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        	//request.getRequestDispatcher( ACCES_CONNEXION ).forward( request, response );
        }else{
        	this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        }
        
    }
     
    /**
     * Méthode permettant de contrôler les informations pour la déconnexion
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
         HttpSession session = request.getSession();
         
         String deco = getValeurChamp( request, CHAMP_DECO );
         if ( deco.equals("Non")) {
        	 // Cas où il ne veut pas se déconnecter
             response.sendRedirect( request.getContextPath() + VUE_CO );
         } else {
             session.setAttribute( ATT_SESSION_EMPL, null );
             session.setAttribute( ATT_SESSION_ID, null );
             // fermeture des sessions
             response.sendRedirect( request.getContextPath() + VUE_DECO );
         }
    }

	 /**
	  * Méthode utilitaire qui retourne NULL si un champ est vide, et son contenu sinon.
	  * @param request
	  * @param nomChamp
	  * @return la valeur
	  * 
	  */ 
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }
    
}
