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
import com.mdl.dao.DAOFactory;
import com.mdl.dao.LitigeDAO;
import com.mdl.forms.CreationLitigeClientForm;
import com.mdl.forms.CreationLitigeCoursierForm;
import com.mdl.forms.InscriptionForm;
import com.mdl.mail.Email;
import com.mdl.pdf.EnvoyerFormulaire;

/**
 * Servlet permettant de contrôler les informations utiles pour la création de litiges à partir 
 * de problèmes signalés par des coursiers.
 * 
 *
 */
public class CreationLitigeCoursier extends HttpServlet {
	// Attributs utiles pour la page JSP
	public static final String ATT_LITIGE = "litige";
    public static final String ATT_FORM = "form";
    public static final String ATT_COM = "colis";
    // Constantes de redirection
    public static final String  VUE             	= "/WEB-INF/pagesJSP/client/creationLitigeClient.jsp";
    public static final String  VUE_DEPART      	= "/HistoriqueCommandes";
    public static final String ACCES_CONNEXION  	= "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_USER  = "/AccueilClient";
    public static final String  VUE_EMPLOYE	     	= "/ConsulterLitigeCoursier?nouveau=0";
    // Attributs de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
    // Constante du DAOFactory
    public static final String CONF_DAO_FACTORY = "daofactory";
    
    private LitigeDAO litigeDao;
    
    /**
     * Initialisation de la DAO des litiges pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.litigeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getLitigeDao();
    }
    
    /**
     * Méthode permettant de rediriger vers la page JSP de création du litige
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    
    	HttpSession session = request.getSession();
    	request.setCharacterEncoding( "UTF-8" );
       
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		// Si un utilisateur est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            // Si aucun employé n'est connecté, il est redirigé vers la page de connexion
        	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else{
        	// Sinon il est redirigé vers la méthode doPost
        	doPost(request,response);
        }
        
    }
    
    /**
     * Méthode permettant de contrôler les données issues du formulaire de création du litige
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    	request.setCharacterEncoding( "UTF-8" );
    	CreationLitigeCoursierForm form = new CreationLitigeCoursierForm(litigeDao);
        
        Litige litige = form.creerLitige( request );
        String mail = litige.getMail();
        String colis = litige.getColis();
        String id = litige.getId();
        try {
        	// Envoi de l'adresse mail
			Email.envoiReponseLitige(mail, id, colis);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_LITIGE, litige );
        response.sendRedirect( request.getContextPath() + VUE_EMPLOYE );
    }
    

}
