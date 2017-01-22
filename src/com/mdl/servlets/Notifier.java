package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.mdl.beans.Commande;
import com.mdl.beans.Litige;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.LitigeDAO;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.CreationLitigeClientForm;
import com.mdl.forms.FermetureLitigeClientForm;
import com.mdl.forms.InscriptionForm;
import com.mdl.forms.ReponseLitigeClientForm;
import com.mdl.mail.Email;
import com.mdl.pdf.EnvoyerFormulaire;

/**
 * Servlet permettant de contrôler les informations utiles pour la fermeture d'un litige par un employé
 * 
 *
 */
public class Notifier extends HttpServlet {
	// Attributs de la page JSP
    public static final String ATT_CONFIRM 	= "confirm";
    public static final String CHAMP_ICU	= "icu";
    // Constante de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/notifier.jsp";
    // Constante de la DAO Factory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER 	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL 	= "sessionEmploye";
    
    private CommandeDAO commandeDao;
    
    /**
     * Initialisation de la DAO des litiges, du statut et de l'utilisateur pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }
    
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

	    boolean confirm = false;
	    request.setAttribute( ATT_CONFIRM, confirm );
    	this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
            
    }       
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        
	    request.setCharacterEncoding( "UTF-8" );
	   	
	    HttpSession session = request.getSession();
	    	
	    String icu = getValeurChamp( request, CHAMP_ICU );
	    commandeDao.notifier(icu);
	    boolean confirm = true;
	    request.setAttribute( ATT_CONFIRM, confirm );
	    this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        
    	

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
