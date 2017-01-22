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
import com.mdl.forms.InscriptionForm;
import com.mdl.forms.ReponseLitigeClientForm;
import com.mdl.mail.Email;
import com.mdl.pdf.EnvoyerFormulaire;

/**
 * Servlet contrôlant les informations relatives à l'envoi d'une réponse à un litige par un employé
 * ou par un client
 * 
 *
 */
public class ReponseLitigeClient extends HttpServlet {
	// Constantes de redirection
    public static final String VUE_ERREUR 			= "/WEB-INF/pagesJSP/client/consulterLitigeClient.jsp";
    public static final String  VUE_CLIENT	     	= "/ConsulterLitigeClient";
    public static final String  VUE_EMPLOYE	     	= "/ConsulterLitigesCat?statut=0";
    public static final String ACCES_CONNEXION  	= "/ConnexionClient";
    // Attributs utiles pour la page JSP
	public static final String ATT_LITIGE 			= "litige";
    public static final String ATT_FORM 			= "form";
    // Champs du formulaire à récupérer
    public static final String CHAMP_MAIL 			= "mail";
    public static final String CHAMP_ID 			= "idLitige"; 
    public static final String CHAMP_COLIS 			= "colis";
    // Attributs de session
    public static final String ATT_SESSION_USER 	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPLOYE	= "sessionEmploye";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    
    private LitigeDAO litigeDao;
    
    /**
     * Initialisation de la DAO des litiges pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.litigeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getLitigeDao();
    }
    
    /**
     * Méthode contrôlant les informations issues du formulaire afin de les traiter correctement en fonction 
     * d'un client ou d'un employé
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        
    	request.setCharacterEncoding( "UTF-8" );
    	
    	HttpSession session = request.getSession();
    	
    	if ( session.getAttribute( ATT_SESSION_EMPLOYE ) == null ) {
    		// Si un employé n'est pas connecté, on vérifie si un utilisateur l'est.
    		if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
            }else{
            	// Si l'utilisateur est connecté, on insère sa réponse au litige
            	ReponseLitigeClientForm form = new ReponseLitigeClientForm(litigeDao);
            	
            	form.repondreLitige( request );
            	if ( form.getErreurs().isEmpty()) {
                    response.sendRedirect( request.getContextPath() + VUE_CLIENT );
                } else {
                	request.setAttribute( ATT_FORM, form );
                    response.sendRedirect( request.getContextPath() + VUE_CLIENT );
                }
            }
        }else{
        	// Sinon, l'employé est connecté et la réponse vient de lui
        	ReponseLitigeClientForm form = new ReponseLitigeClientForm(litigeDao);
        	form.repondreLitige( request );
        	String mail = getValeurChamp( request, CHAMP_MAIL );
        	
        	String id = getValeurChamp( request, CHAMP_ID );
        	String colis = getValeurChamp( request, CHAMP_COLIS );
        	if ( form.getErreurs().isEmpty()) {
        		try {
					Email.envoiReponseLitige(mail, id, colis);
				} catch (Exception e) {
					e.printStackTrace();
				}
                response.sendRedirect( request.getContextPath() + VUE_EMPLOYE );
            } else {
            	request.setAttribute( ATT_FORM, form );
                response.sendRedirect( request.getContextPath() + VUE_EMPLOYE );
            }
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
