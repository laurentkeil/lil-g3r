package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasypt.util.text.*;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.FormValidationException;

/**
 * Servlet pour le contrôle de la redirection et la validation du compte
 * 
 *
 */
public class Confirmation extends HttpServlet {
	// Constantes pour la redirection
    public static final String  VUE             	  = "/WEB-INF/pagesJSP/confirmationInscription.jsp";
    public static final String  VUE_ERREUR            = "/WEB-INF/pagesJSP/erreur.jsp";
    public static final String ACCES_CONNECTE    	  = "/AccueilClient";
    public static final String ACCES_CONNECTE_EMPL    = "/AccueilEmploye";
    // Constante de la DAO Factory
    public static final String CONF_DAO_FACTORY = "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
    // Attributs à envoyer à la page JSP
    public static final String MESSAGE = "message";
    public static final String EMAIL = "email";
    
    private UtilisateurDAO     utilisateurDao;
    private StatutDAO 		   statutDao;

    /**
     * initialisation du DAO de l'utilisateur et du statut
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getStatutDao();
    }
    
	 /**
	  * Méthode pour rediriger vers la page JSP de la confirmation du compte et de la prise en charge de 
	  * la validation du compte dans la base de données.
	  * 
	  */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	
	    HttpSession session = request.getSession();
	    String message = null;
    	if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        }else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
	    	String confirmation = request.getParameter( "confirm" );	
	    	confirmation = confirmation.replaceAll("\\s", "+");
	    	// Permet de remplacer les espaces par des "+" qui ne sont pas pris en compte par les navigateurs
	    	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	        String myEncryptionPassword = "con/fir$mation d'1nscription";
	        textEncryptor.setPassword(myEncryptionPassword);
	        String email = null;
	        try{
	        	 email = textEncryptor.decrypt(confirmation);
	        	 // Vérification de l'argument passer dans l'URL
	        } catch (Exception e){}
	        finally{
	        	 boolean existe = utilisateurDao.inscription(email);
	 	        if (existe){
	 	        	Utilisateur client = utilisateurDao.trouver(email);
	 	        	statutDao.nouveauClient(client.getId()); 
	 	        	request.setAttribute(MESSAGE, message);
	 	 	        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	 	        }else{
	 	        	 this.getServletContext().getRequestDispatcher( VUE_ERREUR ).forward( request, response );
	 	        }
	        }
	    }else{
	    	// Autre client déjà connecté donc validation non prise en charge
	    	message = "non connecté";
	    	String email = (String) session.getAttribute( ATT_SESSION_USER );
	    	request.setAttribute(MESSAGE, message);
	    	request.setAttribute(EMAIL, email);
	    	this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	    }
    }

}
