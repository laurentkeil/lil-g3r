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

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.InscriptionForm;
import com.mdl.forms.ConnexionClientForm;
 
/**
 * Servlet permettant de contrôler la déconnexion d'un client
 * 
 *
 */
public class DeconnexionClient extends HttpServlet {
	// Attributs utiles pour la JSP
    public static final String ATT_USER 	= "utilisateur";
    public static final String ATT_FORM 	= "form";
    private static final String CHAMP_DECO 	= "deco";
    // Constante de redirection
    public static final String VUE_ARRIVEE  		= "/WEB-INF/pagesJSP/client/deconnexionClient.jsp";
    public static final String VUE_DECO 			= "/Accueil";
    public static final String VUE_CO 				= "/AccueilClient";
    public static final String ACCES_CONNEXION 		= "/ConnexionClient";
    public static final String ACCES_CONNECTE_EMPL 	= "/AccueilEmploye";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    // Attributs de session
    public static final String  ATT_SESSION_USER  	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL   	= "sessionEmploye";
    
    /**
     * Méthode permettant la redirection vers la page de déconnexion
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
      
    	HttpSession session = request.getSession();
   	 
    	if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
    		// Si l'employé est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        }else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
           // Si aucun utilisation n'est connecté, il est redirigé vers sa page de connexion
        	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else{
        	// Sinon, il accède à la page de déconnexion
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
             session.setAttribute( ATT_SESSION_USER, null );
             // Fermeture de la session
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
