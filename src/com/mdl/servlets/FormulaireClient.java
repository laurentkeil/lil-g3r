package com.mdl.servlets;
 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Commande;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.*;
import com.mdl.forms.InscriptionForm;
import com.mdl.pdf.EnvoyerFormulaire;
 
/**
 * Servlet permettant le contrôle des informations utiles pour l'envoi du bordereau d'envoi du colis
 * 
 *
 */
public class FormulaireClient extends HttpServlet {
	// attributs utiles pour la JSP
    public static final String ATT_USER 			= "utilisateur";
    public static final String ATT_FORM 			= "form";
    // Constantes de redirection
    public static final String VUE 					= "/WEB-INF/pagesJSP/client/envoiFormulaireClient.jsp";
    public static final String ACCES_CONNECTE_EMPL  = "/AccueilEmploye";
    public static final String ACCES_CONNEXION  	= "/ConnexionClient";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER 	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL 	= "sessionEmploye";
    // Champ du formulaire à récupérer
    private static final String CHAMP_ID          = "id";
         
    
    private UtilisateurDAO     utilisateurDao;
    private CommandeDAO        commandeDao;
    /**
     * Initialisation de la DAO de l'utilisateur, de la commande pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }
    
    /**
     * Méthode de contrôle des informations utiles pour l'envoi du bordereau d'envoi du colis
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{

    	HttpSession session = request.getSession();
    	if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
    		// Si un employé est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        }else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
        	// Si aucun utilisateur n'est connecté, il est redirigé vers sa page de connexion
        	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else{
        	// Appel de la méthode post pour le traitement des champs du formulaire
        	doPost(request,response);
        }
    	
    }
     
    /**
     * Méthode de contrôle des informations utiles pour l'envoi du bordereau d'envoi du colis
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{

    	request.setCharacterEncoding( "UTF-8" );
    	HttpSession session = request.getSession();
    	String id = getValeurChamp( request, CHAMP_ID );
    	String email = (String) session.getAttribute(ATT_SESSION_USER);
    	Utilisateur utilisateur = utilisateurDao.trouver(email);
    	Commande commande = commandeDao.trouver(id);
        EnvoyerFormulaire formulaire = new EnvoyerFormulaire();
        try {
			formulaire.EmailFormulaire(email, commande);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        request.setAttribute( ATT_USER, utilisateur );
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

