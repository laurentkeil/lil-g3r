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
public class FermetureLitige extends HttpServlet {
	// Attributs de la page JSP
	public static final String ATT_LITIGE 	= "litige";
    public static final String ATT_FORM 	= "form";
    public static final String CHAMP_MAIL 	= "mail";
    public static final String CHAMP_ID 	= "idLitige";
    public static final String CHAMP_COLIS 	= "colis";
    // Constante de redirection
    public static final String  VUE_EMPLOYE	    	= "/ConsulterLitigesCat?statut=0";
    public static final String ACCES_CONNEXION  	= "/ConnexionClient";
    public static final String ACCES_CONNECTE_USER  = "/AccueilClient";
    // Constante de la DAO Factory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER 	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL 	= "sessionEmploye";
    
    private LitigeDAO litigeDao;
    private StatutDAO statutDao;
    private UtilisateurDAO utilisateurDao;
    
    /**
     * Initialisation de la DAO des litiges, du statut et de l'utilisateur pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.litigeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getLitigeDao();
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getStatutDao();
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }
    
    /**
     * Méthode contrôlant les informations relatives à la fermeture d'un litige
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        
    	request.setCharacterEncoding( "UTF-8" );
    	
    	HttpSession session = request.getSession();
    	
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
    		response.sendRedirect( request.getContextPath() + ACCES_CONNEXION ); 
        }else{
        	FermetureLitigeClientForm form = new FermetureLitigeClientForm(litigeDao);
        	boolean mauvais = form.fermerLitige( request );
        	String mail = getValeurChamp( request, CHAMP_MAIL );
        	String id = getValeurChamp( request, CHAMP_ID );
        	String colis = getValeurChamp( request, CHAMP_COLIS );
        	if (mauvais){
        		// Vérifie si le client a plus de 3 litiges en tort
        		String cause = "litige";
        		Utilisateur client = utilisateurDao.trouver(mail);
        		statutDao.bloquerCompte(client.getId(), cause);
        	}
        	if ( form.getErreurs().isEmpty()) {
        		try {
					Email.envoiFermetureLitige(mail, id, colis);
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
